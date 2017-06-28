package fuzzer.com.codigosarquitectura;


import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fuzzer.com.codigosarquitectura.restAPI.adapter.RestApiAdapter;
import fuzzer.com.codigosarquitectura.restAPI.endpoints.Endpoints;
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
    Pusher pusher;

    @Override
    public void onCreate() {
        context = this;
        Log.e("-->>", "inicio servicio");


        pusher = new Pusher("3c9aa0e2bcc0ff7d6926");

        Channel channel = pusher.subscribe("enviar");
        channel.bind("enviar-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                Log.e("pusher", data);
                //recibe la respuesta y se va a la otra pantalla que muestra la lista de codigos
                Log.e("->", "manda a llamar a la otra activity");
                Intent intent = new Intent(context, ListaCodigos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        pusher.connect();

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
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
        pusher.disconnect();
        super.onDestroy();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        clipboardManager.addPrimaryClipChangedListener(listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                clipData = clipboardManager.getPrimaryClip();

                try {
                    if (clipData.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        Log.e("*** CACHADOR ***", clipData.getItemAt(0).getText().toString());

                        String textoCopiado = clipData.getItemAt(0).getText().toString().trim();

                        Pattern pattern = Pattern.compile("[0-9]{10}");
                        Matcher matcher = pattern.matcher(textoCopiado);
                        String numero = "";
                        if (matcher.find()) {
                            numero = matcher.group(0);
                          //  Toast.makeText(context, "numero encontrado: " + numero, Toast.LENGTH_SHORT).show();
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

//    private void performClipboardCheck() {
//        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//        if (cb.hasPrimaryClip()) {
//            ClipData cd = cb.getPrimaryClip();
//            if (cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//
//
//                Log.e("*** CACHADOR ***", cd.getItemAt(0).getText().toString());
//
//                String textoCopiado = cd.getItemAt(0).getText().toString().trim();
//
//                Pattern pattern = Pattern.compile("[0-9]{10}");
//                Matcher matcher = pattern.matcher(textoCopiado);
//                String numero = "";
//                if (matcher.find()) {
//                    numero = matcher.group(0);
//                    Toast.makeText(this, "numero encontrado: " + numero, Toast.LENGTH_SHORT).show();
//                    consumirServicioDeMemo(numero);
//                } else {
//                    Toast.makeText(this, "no hay un numero valido", Toast.LENGTH_SHORT).show();
//                }
//                Log.e("Numero encontrado->", numero);


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


//            }
//        }
//    }


    public void consumirServicioDeMemo(String numero) {
//        Log.e("->", "manda a llamar a la otra activity");
//        Intent intent = new Intent(context, ListaCodigos.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("numero", numero);
//        startActivity(intent);

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
        Log.e("fecha", fInicial + "    " + fFinal);


        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
        Call<Respuesta> listaCodigosCall = endpoints.obtenerDatosTransaccion(fInicial, fFinal, "521" + numero, new Date().toString());
        listaCodigosCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Log.e("respuesta servicio", response.body().getRespuesta());
                Toast.makeText(context, "Espera Acertuniano !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.e("->", "error en el consumo");
                Log.e("->", t.getMessage());
            }
        });

//        listaCodigosCall.enqueue(new Callback<ArrayList<Codigos>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Codigos>> call, Response<ArrayList<Codigos>> response) {
//                listaDeCodigos = new ArrayList<Codigos>();
//                listaDeCodigos = response.body();
//                Log.e("tanaño de lista", listaDeCodigos.size() + "");
//                Log.e("elemento", listaDeCodigos.get(0).getCodigo());
//                mostrarDatos(listaDeCodigos);
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Codigos>> call, Throwable t) {
//                Log.e("->", "error en el consumo");
//                Log.e("->", t.getMessage());
//            }
//        });
    }

}
