package com.example.simplefirebasenotification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload (beauty messages).
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            createAndSendNotificationB(remoteMessage);
        }

        // Check if message contains a notification payload (from console).
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            createAndSendNotificationC(remoteMessage.getNotification().getBody());
        }
    }

    private void createAndSendNotificationB(RemoteMessage remoteMessage){
        //Code here
    }

    private void createAndSendNotificationC(String messageBody){
        //Code here
    }

}
