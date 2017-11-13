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
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private Context context;
    private ClipData clipData;
    private ClipboardManager clipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener listener;

    private static final int ID_NOTIFICACIONES = 1;
    private NotificationManager notifyManager;

    @Override
    public void onCreate() {

        context = this;

        Log.i("onCreate", "[!] Starting");

        // __ Notificaciones __

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                0
            )
        );

        builder.setSmallIcon(R.drawable.rataicon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.rataicon));
        builder.setContentTitle("Guardia: SMS y VOZ");
        builder.setContentText("El servicio está activo");
        builder.setSubText("by Fuzzer");
        builder.setOngoing(true);

        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.notify(ID_NOTIFICACIONES, builder.build());

    /* * *
     * Capura los datos de la notificacion en modo texto
     * y lo envia a la activity de lista de codigos
     *

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

        Log.i("onDestroy", "[!] Destroying");

        clipboardManager.removePrimaryClipChangedListener(listener);
        notifyManager.cancel(ID_NOTIFICACIONES);

        stopSelf();
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

                if ( clipData.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) &&
                     clipData.getItemAt(0) != null) {

                    String textoCopiado = clipData.getItemAt(0).getText().toString().trim();

                    Log.i("Clipboard", "Original: \""+ textoCopiado +"\"");

                    textoCopiado = textoCopiado
                                        .replaceAll("\\s+", "")
                                        .replaceAll("-", "");

                    Log.i("Clipboard", "Tratado: \""+ textoCopiado +"\"");

                    Matcher matcher = ( Pattern.compile("[0-9]{10}") ).matcher(textoCopiado);

                    if ( matcher.find() ) {

                        String numero = matcher.group(0);
                        Toast.makeText(context, "Número encontrado \""+ numero +"\"", Toast.LENGTH_SHORT).show();
                        consumoServicioCodigos(numero);

                    } else {

                        Toast.makeText(context, "No hay ningún número valido", Toast.LENGTH_SHORT).show();

                    }

                }

            } catch (NullPointerException e) {
                Log.i("onStartCommand", "Error controlado");
            }

            }

        });

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void consumoServicioCodigos(String numero) {

        Log.i("consumoServicioCodigos", "Inicio del consumo");

        // __ Fechas __

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 1);

        String fFinal = (new SimpleDateFormat("dd-MM-yyyy")).format(c.getTime());
        String fInicial = obtenerFechaPreferencia();

        // __ Peticion __

        Datos peticion = new Datos(
                fInicial,
                fFinal,
                "521" + numero,
                FirebaseInstanceId.getInstance().getToken(),
                getTelefono()
        );

        Log.i("consumoServicioCodigos", "Peticion: " + peticion.toString());

        // __ Conusmo __

        Endpoints endpoints = (new RestApiAdapter()).establecerConexionRestAPI();
        Call<Respuesta> listaCodigosCall = endpoints.obtenerDatosTransaccion(new FirebaseRequestNotification(
            peticion,
            ConstantesRestAPI.idFirebaseFront
        ));

        listaCodigosCall.enqueue(new Callback<Respuesta>() {

            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                Log.i("consumoServicioCodigos", "Success: " + response.body().getSuccess());
                Log.i("consumoServicioCodigos", "Failure: " + response.body().getFailure());

                Toast.makeText(
                    context,
                    response.body().getSuccess() == 1 ?
                        "Espera Acertuniano!" :
                        "Ocurrio un error al mandar la peticion al servidor de Arquitectura :(",
                    Toast.LENGTH_SHORT
                ).show();

            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

                Log.e("consumoServicioCodigos", "Error en el consumo: " + t.getMessage());

                Toast.makeText(
                    context,
                    "Ocurrio un error consumo de servicio de google, verifíca tu conexión de red ;)",
                    Toast.LENGTH_SHORT
                ).show();

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
        String phonenum;

        try {
            phonenum = telephonyManager.getLine1Number();
        } catch (Exception e) {
            phonenum = "Error!!";
        }

        return phonenum;
    }

}
