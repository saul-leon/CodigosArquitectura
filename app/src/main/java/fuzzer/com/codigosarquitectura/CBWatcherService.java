package fuzzer.com.codigosarquitectura;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fuzzer.com.codigosarquitectura.restAPI.adapter.RestApiAdapter;
import fuzzer.com.codigosarquitectura.restAPI.constantes.ConstantesRestAPI;
import fuzzer.com.codigosarquitectura.restAPI.endpoints.Endpoints;
import fuzzer.com.codigosarquitectura.restAPI.models.Datos;
import fuzzer.com.codigosarquitectura.restAPI.models.FirebaseRequestNotification;
import fuzzer.com.codigosarquitectura.restAPI.models.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CBWatcherService extends Service {


    Context context;


    ClipboardManager clipboardManager;
    ClipData clipData;
    ClipData.Item item;

    //declare Listener
    ClipboardManager.OnPrimaryClipChangedListener listener;


    @Override
    public void onCreate() {


        context = this;
        Log.e("-->>", "inicio servicio");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.rataicon);
        Intent intent2 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.rataicon));
        builder.setContentTitle("Guardia: SMS y VOZ");
        builder.setContentText("El servicio está activo");
        builder.setSubText("by Fuzzer");
        builder.setOngoing(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());

/*     con esto capura los ddatos de la notificacion en modo texto y lo envia a la activity de lista de codigos
        Intent intent = new Intent(context, ListaCodigos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("data", data);
        startActivity(intent);
*/

    }


//    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
//        public void onPrimaryClipChanged() {
//            Log.e("-->>", "Cacho algo");
//            performClipboardCheck();
//        }
//    };

    @Override
    public void onDestroy() {
        Log.e("-->>", "servicio destruido on destroy");


        clipboardManager.removePrimaryClipChangedListener(listener);
        stopSelf();

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(1);


        super.onDestroy();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);


        clipboardManager.addPrimaryClipChangedListener(listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                clipData = clipboardManager.getPrimaryClip();

                try {
                    if (clipData.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.e("*** CACHADOR ***", clipData.getItemAt(0).getText().toString());

                        String textoCopiado = clipData.getItemAt(0).getText().toString().trim();
                        textoCopiado = textoCopiado.replaceAll("\\s+", "");
                        textoCopiado = textoCopiado.replaceAll("-", "");


                        Log.e("*** TEXTO TRATADO ***", textoCopiado);
                        Pattern pattern = Pattern.compile("[0-9]{10}");
                        Matcher matcher = pattern.matcher(textoCopiado);
                        String numero = "";
                        if (matcher.find()) {
                            numero = matcher.group(0);
                            Toast.makeText(context, "numero encontrado: " + numero, Toast.LENGTH_SHORT).show();
                            consumirServicioDeMemo(numero);
                        } else {
                            Toast.makeText(context, "no hay un numero valido", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("Numero encontrado->", numero);
                    }

                } catch (NullPointerException e) {

                }

            }

        });


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void consumirServicioDeMemo(String numero) {

        consumoServicio(numero);
    }


    public void consumoServicio(String numero) {

        Log.e("->", "consume servicio");


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String fInicial = sdf.format(new Date());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);
        String fFinal = sdf.format(c.getTime());



        fInicial = obtenerFechaPreferencia();
        Log.e("fecha", fInicial + "    " + fFinal);


        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();


        FirebaseRequestNotification pedirCofigo = new FirebaseRequestNotification(new Datos(fInicial, fFinal, "521" + numero, FirebaseInstanceId.getInstance().getToken(), getTelefono()), ConstantesRestAPI.idFirebaseFront);

        Call<Respuesta> listaCodigosCall = endpoints.obtenerDatosTransaccion(pedirCofigo);

        //   Call<Respuesta> listaCodigosCall = endpoints.obtenerDatosTransaccion(fInicial, fFinal, "521" + numero, new Date().toString(), FirebaseInstanceId.getInstance().getToken());
        listaCodigosCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Log.e("respuesta servicio", response.body().getSuccess() + "");
                Log.e("respuesta servicio", response.body().getFailure() + "");

                if (response.body().getSuccess() == 1) {
                    Toast.makeText(context, "Espera Acertuniano !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ocurrio un error al mandar la peticion al servidor de Arquitectura :(", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.e("->", "error en el consumo");
                Log.e("->", t.getMessage());
                Toast.makeText(context, "Ocurrio un error consumo de servicio de google, verifíca tu conexión de red ;)", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private String obtenerFechaPreferencia() {

        SharedPreferences fechaPreferencia = getSharedPreferences("FECHA_INICIAL", MODE_PRIVATE);
        Calendar c = Calendar.getInstance();
        return fechaPreferencia.getInt("DAY", c.get(Calendar.DAY_OF_MONTH))+"-"+fechaPreferencia.getInt("MONTH", c.get(Calendar.MONTH))+"-"+fechaPreferencia.getInt("YEAR", c.get(Calendar.YEAR));

    }

    private String getTelefono() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String phonenum, IMEI;
        try {
            phonenum = telephonyManager.getLine1Number();

        } catch (Exception e) {
            phonenum = "Error!!";
        }
        return phonenum;
    }

}
