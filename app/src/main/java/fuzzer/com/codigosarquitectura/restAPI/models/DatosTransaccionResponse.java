package fuzzer.com.codigosarquitectura.restAPI.models;

import java.util.ArrayList;

/**
 * Created by omar on 31/01/17.
 */

public class DatosTransaccionResponse {
    private int codigo;
    private String mensaje;
    private boolean ok;
    ArrayList<Logs> Log;

    public DatosTransaccionResponse(int codigo, String mensaje, boolean ok, ArrayList<Logs> log) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.ok = ok;
        Log = log;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public ArrayList<Logs> getLog() {
        return Log;
    }

    public void setLog(ArrayList<Logs> log) {
        Log = log;
    }
}
