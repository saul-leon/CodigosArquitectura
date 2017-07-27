package fuzzer.com.codigosarquitectura;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Switch activarGuardia;

    private TextToSpeech t1;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activarGuardia = (Switch) findViewById(R.id.activarGuardia);


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
                Log.e(getClass().getName(),"Estado TTS "+status);
                Log.e(getClass().getName(),"TextToSpeech.ERROR "+TextToSpeech.ERROR);
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
    public void mostrarDatosenSpinner(){
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
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
            startActivity(launchIntent);
        }
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
