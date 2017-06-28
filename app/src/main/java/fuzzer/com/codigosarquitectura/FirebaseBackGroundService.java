package fuzzer.com.codigosarquitectura;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by omar on 27/06/17.
 */

public class FirebaseBackGroundService extends FirebaseMessagingService {
    private Map<String, String> dataNotification;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Log.e("=====>>", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.e("data notification >>",remoteMessage.getData().size()+"");
            dataNotification = remoteMessage.getData();
        }
    }
}
