package fuzzer.com.codigosarquitectura.restAPI.models;

/**
 * Created by omar on 25/05/17.
 */

public class Logs {
    private int logId;
    private String fecha;
    private String destinatarios;
    private String idFuncionalidad;

    private String parametros;
//    4012 bienvenida
//    4193 Verificacion

    public Logs(int logId, String fecha, String destinatarios, String idFuncionalidad, String parametros) {
        this.logId = logId;
        this.fecha = fecha;
        this.destinatarios = destinatarios;
        this.idFuncionalidad = idFuncionalidad;
        this.parametros = parametros;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getIdFuncionalidad() {
        return idFuncionalidad;
    }

    public void setIdFuncionalidad(String idFuncionalidad) {
        this.idFuncionalidad = idFuncionalidad;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }
}
