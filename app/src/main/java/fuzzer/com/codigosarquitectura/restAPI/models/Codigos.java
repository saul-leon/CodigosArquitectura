package fuzzer.com.codigosarquitectura.restAPI.models;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Codigos implements Comparable<Codigos> {

    private String logId;
    private String fecha;
    private String destinatario;
    private String codigo;
    private String tipo;
    private String origen;
    private Date fechaSolicitud;

    public Codigos(
        String logId,
        String fecha,
        String destinatario,
        String codigo,
        String tipo,
        String origen
    ){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
            this.fechaSolicitud = formatter.parse(fecha);
            Log.i("Codigos", "fechaSolicitud: " + fechaSolicitud.toString());
        } catch (ParseException e) {
            Log.e("Codigos", "Error: " + e.getMessage());
        }

        this.fecha = fecha;
        this.logId = logId;
        this.destinatario = destinatario;
        this.codigo = codigo;
        this.tipo = tipo;
        this.origen = origen;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getFechaSolicitud() {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");
            this.fechaSolicitud = formatter.parse(fecha);
        } catch (ParseException e) {
            Log.e("Codigos", "Error: " + e.getMessage());
        }

        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
    public int compareTo(@NonNull Codigos o) {
        if (getFechaSolicitud() == null || o.getFechaSolicitud() == null)
            return 0;
        return getFechaSolicitud().compareTo(o.getFechaSolicitud());
    }
}
