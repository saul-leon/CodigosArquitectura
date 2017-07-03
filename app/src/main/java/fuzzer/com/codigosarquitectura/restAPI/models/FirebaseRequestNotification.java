package fuzzer.com.codigosarquitectura.restAPI.models;

/**
 * Created by omar on 30/06/17.
 */

public class FirebaseRequestNotification {
    private Datos data;
    private String to;

    public FirebaseRequestNotification(Datos data, String to) {
        this.data = data;
        this.to = to;
    }

    public Datos getData() {
        return data;
    }

    public void setData(Datos data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
