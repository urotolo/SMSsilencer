package com.example.silenced;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ReceiveSMS extends BroadcastReceiver {

    private static final String TAG = ReceiveSMS.class.getSimpleName();
    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Receives the SMS message
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");

        // Extract data from the received sms message
        Object[] pdus = (Object[]) bundle.get(pdu_type);

        if (pdus != null) {
            // Check the Android version
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);


            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check android version and use appropriate createFromPdu
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {   // If does not work replace with if(isVersionM)
                    // If Android version M or newer
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);

                } else {
                    // If Android version L or older
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}
