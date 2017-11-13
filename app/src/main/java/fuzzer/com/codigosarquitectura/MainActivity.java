package fuzzer.com.codigosarquitectura;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private Switch activarGuardia;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // __ TTS __
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                Log.i("TextToSpeech", "Estado TTS " + status);
                Log.i("TextToSpeech", "TextToSpeech.ERROR " + TextToSpeech.ERROR);

                if (status != TextToSpeech.ERROR)
                    tts.setLanguage(Locale.getDefault());
            }
        });

        // __ Date Picker __

        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.HOUR_OF_DAY, 23);
        maxDate.set(Calendar.MINUTE, 59);
        maxDate.set(Calendar.SECOND, 59);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setMaxDate(maxDate.getTimeInMillis());

        int fecha[] = obtenerFechaDesdeSharePreference();
        Log.i("onCreate" ,"fecha: " + fecha[0] +"-"+fecha[1]+"-"+fecha[2]);

        datePicker.init(fecha[2], fecha[1]-1, fecha[0], new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Log.e("datePicker.init", "Year=" + year + " Month=" + (monthOfYear + 1) + " day=" + dayOfMonth);

            SharedPreferences.Editor editor = getSharedPreferences("FECHA_INICIAL", MODE_PRIVATE).edit();

            editor.putInt("DAY", dayOfMonth);
            editor.putInt("MONTH", monthOfYear + 1);
            editor.putInt("YEAR", year);
            editor.apply();

            Toast.makeText(
                MainActivity.this,
                "Fecha inicial cambiada correctamente",
                Toast.LENGTH_SHORT
            ).show();

            }
        });

        // __ Switch Toogle __

        activarGuardia = (Switch) findViewById(R.id.activarGuardia);
        activarGuardia.setChecked(isServiceRunning());
        activarGuardia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.i("activarGuardia.setOnCheckedChangeListener", "isChecked: " + isChecked);

                if (isChecked) {
                    speak(MensajesPredefinidos.guardiaOn);
                    Toast.makeText(MainActivity.this, "Guardia Activada", Toast.LENGTH_SHORT).show();
                    start();
                } else {
                    speak(MensajesPredefinidos.guardiaOff);
                    Toast.makeText(MainActivity.this, "Guardia Desactivada", Toast.LENGTH_SHORT).show();
                    stop();
                    finishAndRemoveTask();
                }

            }
        });

        // __ Spinner __
        mostrarDatosenSpinner();
    }

    private void speak(String mensaje){
        tts.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null);
    }

    private int[] obtenerFechaDesdeSharePreference() {

        SharedPreferences fechaPreferencia = getSharedPreferences("FECHA_INICIAL", MODE_PRIVATE);
        Calendar c = Calendar.getInstance();

        int[] fecha = {
            fechaPreferencia.getInt("DAY",   c.get(Calendar.DAY_OF_MONTH)),
            fechaPreferencia.getInt("MONTH", c.get(Calendar.MONTH)),
            fechaPreferencia.getInt("YEAR",  c.get(Calendar.YEAR))
        };

        // Log.i("obtenerFechaDesdeSharePreference","fecha guardada: " + fecha[0]+" "+fecha[1]+" "+fecha[2]);

        return fecha;

    }

    public void mostrarDatosenSpinner() {

        ArrayList<String> listaOpciones = new ArrayList<>();
        listaOpciones.add("Fuzzer machine");
        listaOpciones.add("Development machine");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaOpciones);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

    }

    public void start() {

        if ( !isServiceRunning() ) {

            Log.i("start", "Iniciando el servicio de la Rata");
            startService(new Intent(this, CBWatcherService.class));
            this.finish();

            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");

            if ( launchIntent == null ) {
                Toast.makeText(this, "Instala WhatsApp, por favor", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(launchIntent);
            }
        }

    }

    public void stop() {

        ActivityManager am = (ActivityManager) getSystemService(MainActivity.ACTIVITY_SERVICE);

        am.killBackgroundProcesses("fuzzer.com.codigosarquitectura.CBWatcherService");
        am.killBackgroundProcesses("fuzzer.com.codigosarquitectura.FirebaseBackGroundService");

        stopService(new Intent(this, CBWatcherService.class));
        stopService(new Intent(this, FirebaseBackGroundService.class));

    }

    public boolean isServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if ("fuzzer.com.codigosarquitectura.CBWatcherService".equals(service.service.getClassName()))
                return true;

        return false;
    }

    @Override
    protected void onResume() {
        if (!isServiceRunning()) {
            NotificationManager nMgr = (NotificationManager) getApplicationContext()
                                        .getSystemService(Context.NOTIFICATION_SERVICE);
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
