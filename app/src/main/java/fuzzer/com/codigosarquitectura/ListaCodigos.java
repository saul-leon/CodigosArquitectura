package fuzzer.com.codigosarquitectura;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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

        Log.i("ListaCodigos", "Temino de hablar");

        setContentView(R.layout.activity_lista_codigos);

        Intent intent = getIntent();
        String dataSMS = intent.getStringExtra("dataSMS");
        String dataVOZ = intent.getStringExtra("dataVOZ");

        listaC = (ListView) findViewById(R.id.listaC);
        listaC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                elegirMedioDeEnvio(
                    (Codigos) parent.getItemAtPosition(position)
                );
            }
        });

        crearListaDesdeStringData(dataSMS, dataVOZ);
    }

    public void elegirMedioDeEnvio(final Codigos codigo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Envialo por");
        builder.setItems(new String[]{"WhatsApp", "SMS", "Cancelar"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //WhatsApp
                        abrirWhats(codigo.getCodigo());
                        break;
                    case 1: //SMS
                        mandarSMS(
                            crearPlantillaSMS(
                                codigo.getCodigo(),
                                codigo.getTipo()
                            ),
                            codigo.getDestinatario().substring(3)
                        );
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mandarSMS(String texto, String destinatario) {

        try {
            Intent smsIntent = new Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("smsto:" + destinatario)
            );
            smsIntent.putExtra("sms_body", texto);
            startActivity(smsIntent);

            Log.i("mandarSMS", "SMS sended");
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("mandarSMS", "Error: " + ex.getMessage());

            Toast.makeText(
                this,
                "No se pudo mandar el SMS :(",
            Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private String crearPlantillaSMS(String codigo, String tipoCodigo) {

        String salida = "";

        switch (tipoCodigo) {
            case "Primer acceso": //plantilla 4012
                salida = "Utiliza esta clave de confirmacion para continuar con tu registro: " + codigo;
                break;
            case "Verificación": //plantilla 4193
                salida = "Bienvenido a banco azteca movil. Tu clave de Confirmacion es: " + codigo;
        }

        return salida;
    }


    private void crearListaDesdeStringData(String dataSMS, String dataVOZ) {

        ArrayList<Codigos> codigosSMS = new Gson().fromJson(
                dataSMS,
                new TypeToken<List<Codigos>>() {
                }.getType()
        );

        ArrayList<Codigos> codigosVOZ = new Gson().fromJson(
                dataVOZ,
                new TypeToken<List<Codigos>>() {
                }.getType()
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

        if (codigosSMS.isEmpty()) {

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
