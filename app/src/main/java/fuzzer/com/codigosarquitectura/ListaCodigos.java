package fuzzer.com.codigosarquitectura;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fuzzer.com.codigosarquitectura.restAPI.models.Codigos;

public class ListaCodigos extends AppCompatActivity {
    ArrayList<Codigos> listaDeCodigos;
    ListView listaC;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_codigos);
        Intent intent = getIntent();
        String dataSMS = intent.getStringExtra("dataSMS");
        String dataVOZ = intent.getStringExtra("dataVOZ");


        Log.e("dataVOZ", dataVOZ);


        //consumoServicio(numero);

        listaC = (ListView) findViewById(R.id.listaC);


        listaC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Codigos codigo = (Codigos) parent.getItemAtPosition(position);


//                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                ClipData clip = ClipData.newPlainText("codigo", codigo.getCodigo());
//                clipboard.setPrimaryClip(clip);

                //Toast.makeText(ListaCodigos.this, "-> " + codigo.getCodigo() + "copiado al portapapeles", Toast.LENGTH_SHORT).show();
                abrirWhats(codigo.getCodigo());

            }
        });

        crearListaDesdeStringData(dataSMS, dataVOZ);
    }

    private void crearListaDesdeStringData(String dataSMS, String dataVOZ) {
        ArrayList<Codigos> codigosSMS = new Gson().fromJson(dataSMS, new TypeToken<List<Codigos>>() {
        }.getType());

        ArrayList<Codigos> codigosVOZ = new Gson().fromJson(dataVOZ, new TypeToken<List<Codigos>>() {
        }.getType());


        for (int i = 0; i < codigosSMS.size(); i++) {
            codigosSMS.get(i).setOrigen("SMS");
        }

        for (int i = 0; i < codigosVOZ.size(); i++) {
            codigosVOZ.get(i).setOrigen("VOZ");
        }

        ArrayList<Codigos> codigos = new ArrayList<>();

        for (Codigos codigo : codigosVOZ) {
            codigos.add(codigo);
        }
        for (Codigos codigo : codigosSMS) {
            codigos.add(codigo);
        }

        if (!codigosSMS.isEmpty()) {
            mostrarDatos(codigos);
        } else {
            String mensaje = "No se encontraron peticiones para este numero, favor de validarlo";
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            abrirWhats(mensaje);
        }
    }

    private void abrirWhats(String cod) {
        Intent waIntent = new Intent(Intent.ACTION_SEND);
        waIntent.setType("text/plain");
        String text = cod;
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");


            //verificar configuraion si se pone en portapapeles o en texto directo

            waIntent.putExtra(Intent.EXTRA_TEXT, text);


            startActivity(Intent.createChooser(waIntent, "Share with"));
            this.finish();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public void consumoServicio(String numero) {
//
//        Log.e("->", "consume servicio");
//
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        String fInicial = sdf.format(new Date());
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        c.add(Calendar.DATE, 1);
//        String fFinal = sdf.format(c.getTime());
//        Log.e("fecha", fInicial + "    " + fFinal);
//
//
//        RestApiAdapter restApiAdapter = new RestApiAdapter();
//        Endpoints endpoints = restApiAdapter.establecerConexionRestAPI();
//        Call<String> listaCodigosCall = endpoints.obtenerDatosTransaccion(fInicial, fFinal, numero);
//        listaCodigosCall.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.e("respuesta solicitud", response.body());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.e("->", "error en el consumo");
//                Log.e("->", t.getMessage());
//            }
//        });
//
////        listaCodigosCall.enqueue(new Callback<ArrayList<Codigos>>() {
////            @Override
////            public void onResponse(Call<ArrayList<Codigos>> call, Response<ArrayList<Codigos>> response) {
////                listaDeCodigos = new ArrayList<Codigos>();
////                listaDeCodigos = response.body();
////                Log.e("tanaño de lista", listaDeCodigos.size() + "");
////                Log.e("elemento", listaDeCodigos.get(0).getCodigo());
////                mostrarDatos(listaDeCodigos);
////            }
////
////            @Override
////            public void onFailure(Call<ArrayList<Codigos>> call, Throwable t) {
////                Log.e("->", "error en el consumo");
////                Log.e("->", t.getMessage());
////            }
////        });
//    }

    private void mostrarDatos(ArrayList<Codigos> listaDeCodigos) {

        //ordenar Lista por hora de solicitud, de mayor a menor

        CodigosAdapter codigosAdapter = new CodigosAdapter(this, ordenarLista(listaDeCodigos));

        listaC.setAdapter(codigosAdapter);
        Log.e("->", "mostrando en pantalla");
    }

    private ArrayList<Codigos> ordenarLista(ArrayList<Codigos> listaDeCodigos) {

        //TODO ordena lista por hora de solicitud de la mas reciente a mas antigua
        Collections.sort(listaDeCodigos);
        Collections.reverse(listaDeCodigos);
        return listaDeCodigos;
        //*************************************************************************
    }


}
