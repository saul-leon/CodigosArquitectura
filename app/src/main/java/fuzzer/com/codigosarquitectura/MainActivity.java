package fuzzer.com.codigosarquitectura;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Switch activarGuardia;

    private TextToSpeech t1;

    Intent intent;

    private DatePicker datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activarGuardia = (Switch) findViewById(R.id.activarGuardia);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar maxDate = Calendar.getInstance();

        maxDate.set(Calendar.HOUR_OF_DAY, 23);
        maxDate.set(Calendar.MINUTE, 59);
        maxDate.set(Calendar.SECOND, 59);


        datePicker.setMaxDate(maxDate.getTimeInMillis());
        int fecha[] = obtenerFechaDesdeSharePreference();
        Log.e("Deberia poner" ,fecha[0]+" "+fecha[1]+" "+fecha[2]);
        datePicker.init(fecha[2], fecha[1]-1, fecha[0], new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.e("Date -->>>", "Year=" + year + " Month=" + (monthOfYear + 1) + " day=" + dayOfMonth);

                SharedPreferences.Editor editor = getSharedPreferences("FECHA_INICIAL", MODE_PRIVATE).edit();

                editor.putInt("DAY", dayOfMonth);
                editor.putInt("MONTH", monthOfYear + 1);
                editor.putInt("YEAR", year);
                editor.apply();
                Toast.makeText(MainActivity.this, "Fecha inicial cambiada correctamente", Toast.LENGTH_SHORT).show();

            }
        });
        datePicker.setMaxDate(maxDate.getTimeInMillis());


//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.e("GET TOKEN", " TOKEN <>>>: " + refreshedToken);

        if (isServiceRunning()) {
            activarGuardia.setChecked(true);
        } else {
            activarGuardia.setChecked(false);
        }


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.e(getClass().getName(), "Estado TTS " + status);
                Log.e(getClass().getName(), "TextToSpeech.ERROR " + TextToSpeech.ERROR);
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });


        activarGuardia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("swich activarGuardia", isChecked + "");
                if (isChecked) {
                    t1.speak(MensajesPredefinidos.guardiaOn, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(MainActivity.this, "Guardia on", Toast.LENGTH_SHORT).show();
                    start();

                } else {
                    t1.speak(MensajesPredefinidos.guardiaOff, TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(MainActivity.this, "Guardia off", Toast.LENGTH_SHORT).show();
                    stop();
                    finishAndRemoveTask();
                }
            }
        });


        //set spinner data
        mostrarDatosenSpinner();
    }

    private int[] obtenerFechaDesdeSharePreference() {
        int[] dia = new int[3];
        Calendar c = Calendar.getInstance();

        SharedPreferences fechaPreferencia = getSharedPreferences("FECHA_INICIAL", MODE_PRIVATE);
        dia[0] = fechaPreferencia.getInt("DAY", c.get(Calendar.DAY_OF_MONTH));
        dia[1] = fechaPreferencia.getInt("MONTH", c.get(Calendar.MONTH));
        dia[2] = fechaPreferencia.getInt("YEAR", c.get(Calendar.YEAR));
        Log.e("fecha guardada",dia[0]+" "+dia[1]+" "+dia[2]);
        return dia;
    }

    public void mostrarDatosenSpinner() {
        ArrayList<String> listaOpciones = new ArrayList<>();
        listaOpciones.add("Fuzzer machine");
        listaOpciones.add("Development machine");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaOpciones);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

    }

    public void start() {

        if (!isServiceRunning()) {
            Log.e("start service >>", "Siempre inicia el servicio chunche");
            intent = new Intent(this, CBWatcherService.class);
            startService(intent);
            this.finish();
            if (whatsappIsInstaled()) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(launchIntent);
            } else {
                Toast.makeText(this, "Por favor descarga WhatsApp", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean whatsappIsInstaled() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        return intent != null ? true : false;
    }

    public void stop() {
        ActivityManager am = (ActivityManager) getSystemService(MainActivity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses("fuzzer.com.codigosarquitectura.CBWatcherService");
        am.killBackgroundProcesses("fuzzer.com.codigosarquitectura.FirebaseBackGroundService");

        intent = new Intent(this, CBWatcherService.class);
        stopService(intent);

        intent = new Intent(this, FirebaseBackGroundService.class);
        stopService(intent);
    }

    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("fuzzer.com.codigosarquitectura.CBWatcherService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        if (!isServiceRunning()) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(1);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {

        Toast.makeText(this, ";)", Toast.LENGTH_SHORT).show();
        this.finish();

        super.onPause();
    }
}
