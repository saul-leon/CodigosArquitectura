package fuzzer.com.codigosarquitectura.restAPI.models;

/**
 * Created by omar on 30/05/17.
 */

public class Respuesta {

    private int success;
    private int failure;

    public Respuesta(int success, int failure) {
        this.success = success;
        this.failure = failure;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    //    private String respuesta;
//
//    public Respuesta(String respuesta) {
//        this.respuesta = respuesta;
//    }
//
//    public String getRespuesta() {
//        return respuesta;
//    }
//
//    public void setRespuesta(String respuesta) {
//        this.respuesta = respuesta;
//    }
}
