package fuzzer.com.codigosarquitectura.restAPI.models;

/**
 * Created by omar on 30/06/17.
 */

public class Datos {
    private String pa_FchInicio;
    private String pa_FchFin;
    private String destinatario;
    private String to;
    private String telefono;

    public Datos(String pa_FchInicio, String pa_FchFin, String destinatario, String to, String telefono) {
        this.pa_FchInicio = pa_FchInicio;
        this.pa_FchFin = pa_FchFin;
        this.destinatario = destinatario;
        this.to = to;
        this.telefono = telefono;
    }

    public String getPa_FchInicio() {
        return pa_FchInicio;
    }

    public void setPa_FchInicio(String pa_FchInicio) {
        this.pa_FchInicio = pa_FchInicio;
    }

    public String getPa_FchFin() {
        return pa_FchFin;
    }

    public void setPa_FchFin(String pa_FchFin) {
        this.pa_FchFin = pa_FchFin;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
