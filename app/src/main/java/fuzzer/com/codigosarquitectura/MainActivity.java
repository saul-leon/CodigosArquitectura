package fuzzer.com.codigosarquitectura;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch activarGuardia;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activarGuardia = (Switch) findViewById(R.id.activarGuardia);



        if(isServiceRunning()){
            activarGuardia.setChecked(true);
        }else{
            activarGuardia.setChecked(false);
        }

        activarGuardia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("swich activarGuardia",isChecked+"");
                if(isChecked){
                    Toast.makeText(MainActivity.this, "Guardia on", Toast.LENGTH_SHORT).show();
                    start();

                }else{
                    Toast.makeText(MainActivity.this, "Guardia off", Toast.LENGTH_SHORT).show();
                    stop();
                    finishAndRemoveTask();
                }
            }
        });

    }

    public void start() {
        intent = new Intent(this, CBWatcherService.class);
        startService(intent);
    }

    public void stop() {
        ActivityManager am = (ActivityManager) getSystemService(MainActivity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses("fuzzer.com.codigosarquitectura.CBWatcherService");

        intent = new Intent(this, CBWatcherService.class);
        stopService(intent);
    }

    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("fuzzer.com.codigosarquitectura.CBWatcherService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
