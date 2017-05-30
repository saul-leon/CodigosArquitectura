package fuzzer.com.codigosarquitectura.restAPI.models;

/**
 * Created by omar on 25/05/17.
 */

public class Codigos {
    private String logId;
    private String fecha;
    private String destinatario;
    private String codigo;
    private String tipo;

    public Codigos(String logId, String fecha, String destinatario, String codigo, String tipo) {
        this.logId = logId;
        this.fecha = fecha;
        this.destinatario = destinatario;
        this.codigo = codigo;
        this.tipo = tipo;
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
}
