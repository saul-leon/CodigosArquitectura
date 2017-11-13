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

    private ListView listaC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ListaCodigos","Temino de hablar");

        setContentView(R.layout.activity_lista_codigos);

        Intent intent = getIntent();
        String dataSMS = intent.getStringExtra("dataSMS");
        String dataVOZ = intent.getStringExtra("dataVOZ");

        listaC = (ListView) findViewById(R.id.listaC);
        listaC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Codigos codigo = (Codigos) parent.getItemAtPosition(position);
            abrirWhats(codigo.getCodigo());
            }
        });

        crearListaDesdeStringData(dataSMS, dataVOZ);
    }

    private void crearListaDesdeStringData(String dataSMS, String dataVOZ) {

        ArrayList<Codigos> codigosSMS = new Gson().fromJson(
            dataSMS,
            new TypeToken<List<Codigos>>() { }.getType()
        );

        ArrayList<Codigos> codigosVOZ = new Gson().fromJson(
            dataVOZ,
            new TypeToken<List<Codigos>>() { }.getType()
        );

        ArrayList<Codigos> codigos = new ArrayList<>();

        for (int i = 0; i < codigosSMS.size(); i++) {
            codigosSMS.get(i).setOrigen("SMS");
            codigos.add(codigosSMS.get(i));
        }

        for (int i = 0; i < codigosVOZ.size(); i++) {
            codigosVOZ.get(i).setOrigen("VOZ");
            codigos.add(codigosVOZ.get(i));
        }

        if ( codigosSMS.isEmpty() ) {

            String mensaje = "No se encontrarón peticiones para este número, favor de validarlo";

            Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_SHORT
            ).show();

            abrirWhats(mensaje);

        } else {

            mostrarDatos(codigos);

        }
    }

    private void abrirWhats(String text) {

        try {

            // Trigger NameNotFoundException
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);

            startActivity(Intent.createChooser(waIntent, "Share with"));

            this.finish();

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }

    }

    private void mostrarDatos(ArrayList<Codigos> listaDeCodigos) {

        // Sort List
        Collections.sort(listaDeCodigos);
        Collections.reverse(listaDeCodigos);

        // Show
        listaC.setAdapter(
            new CodigosAdapter(this, listaDeCodigos)
        );

        Log.i("mostrarDatos", "Mostrando lista");

    }

    @Override
    public void onBackPressed() {
        this.finish();
        Toast.makeText(this, ":)", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, ";)", Toast.LENGTH_SHORT).show();
        this.finish();
        super.onPause();
    }

    @Override
    protected void onStop() {
        this.finish();
        super.onStop();
    }

}
