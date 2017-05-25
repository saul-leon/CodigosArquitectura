package fuzzer.com.codigosarquitectura;


import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CBWatcherService extends Service {


    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            Log.e("-->>", "Cacho algo");
            performClipboardCheck();
        }
    };

    @Override
    public void onCreate() {
        Log.e("-->>", "inicio servicio");
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performClipboardCheck() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            if (cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {


                Log.e("*** CACHADOR ***", cd.getItemAt(0).getText().toString());

                String textoCopiado = cd.getItemAt(0).getText().toString().trim();

                Pattern pattern = Pattern.compile("[0-9]{10}");
                Matcher matcher = pattern.matcher(textoCopiado);
                String numero = "";
                if (matcher.find()) {
                    numero = matcher.group(0);
                }
                Log.e("Numero encontrado->", numero);

                Toast.makeText(this, numero, Toast.LENGTH_SHORT).show();

//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                String fInicial = sdf.format(new Date());
//
//                Calendar c = Calendar.getInstance();
//                c.setTime(new Date());
//                c.add(Calendar.DATE, 1);
//
//
//                String fFinal = sdf.format(c.getTime());
//                Log.e("fecha", fInicial + "    " + fFinal);

                consumirServicioDeMemo(numero);
            }
        }
    }


    public void consumirServicioDeMemo(String numero){

    }

}
