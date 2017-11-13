package fuzzer.com.codigosarquitectura;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseBackGroundService extends FirebaseMessagingService {

    private Map<String, String> dataNotification;
    private Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        context = this;

        if ( remoteMessage.getNotification() != null )
            Log.i("onMessageReceived", "Message Notification Body: " + remoteMessage.getNotification().getBody());

        if (remoteMessage.getData().size() > 0 && isServiceRunning()) {

            try {

                Ringtone r = RingtoneManager.getRingtone(
                    getApplicationContext(),
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                );

                r.play();

            } catch (Exception e) {

                Log.e("onMessageReceived", "Error: " + e.getMessage() );

            }

            Log.i("onMessageReceived", "Data size: " + remoteMessage.getData().size());

            dataNotification = remoteMessage.getData();
            Log.i("onMessageReceived", dataNotification.toString());


            Intent intent = new Intent(context, ListaCodigos.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("dataSMS", dataNotification.get("SMS"));
            intent.putExtra("dataVOZ", dataNotification.get("VOZ"));
            startActivity(intent);

        }
    }

    public boolean isServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if ("fuzzer.com.codigosarquitectura.CBWatcherService".equals(service.service.getClassName()))
                return true;

        return false;

    }
}
