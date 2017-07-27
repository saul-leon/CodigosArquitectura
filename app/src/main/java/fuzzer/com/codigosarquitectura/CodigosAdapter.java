package fuzzer.com.codigosarquitectura;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import fuzzer.com.codigosarquitectura.restAPI.models.Codigos;

/**
 * Created by omar on 26/05/17.
 */

public class CodigosAdapter extends ArrayAdapter<Codigos> {
    ArrayList<Codigos> listaC;
    public CodigosAdapter(Context context, ArrayList<Codigos> codigos) {
        super(context, 0, codigos);
        listaC=codigos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Codigos codigos = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.elemento_lista, parent, false);
        }
        // Lookup view for data population
        TextView logId = (TextView) convertView.findViewById(R.id.logId);
        TextView fecha = (TextView) convertView.findViewById(R.id.fecha);
        TextView destinatario = (TextView) convertView.findViewById(R.id.destinatario);
        TextView codigo = (TextView) convertView.findViewById(R.id.codigo);
        TextView tipo = (TextView) convertView.findViewById(R.id.tipo);
        TextView origen = (TextView) convertView.findViewById(R.id.origen);


        // Populate the data into the template view using the data object
        logId.setText(codigos.getLogId());
        fecha.setText(codigos.getFecha());
        destinatario.setText(codigos.getDestinatario());
        codigo.setText(codigos.getCodigo());
        tipo.setText(codigos.getTipo());
        origen.setText(codigos.getOrigen());


        //se saca la diferencia de tiempo
        Date date = new Date();
        long diferenciaTiempo =date.getTime() - codigos.getFechaSolicitud().getTime();


        if( !codigos.getTipo().equals("Primer acceso") )
            convertView.setBackgroundColor(
                diferenciaTiempo<90000 ?
                    Color.RED :
                    Color.GREEN
            );

        // Return the completed view to render on screen
        return convertView;
    }



    public Codigos getItem(int position){
        return listaC.get(position);
    }
}