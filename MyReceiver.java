package com.example.silenced;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyReceiver extends BroadcastReceiver {


    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String Tag = "SmsBroadcastReceiver";

    ArrayList<String> listOfItems = new ArrayList<String>();
    // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, listOfItems);

    String msg, phoneNo = "";



    @Override
    public void onReceive(Context context, Intent intent) {
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfItems);
        Log.i(TAG, "Intent Received: " +intent.getAction());
        if(intent.getAction() == SMS_RECEIVED)
        {
            //retrieves a map of extended data from the intent
            Bundle dataBundle = intent.getExtras();
            if(dataBundle != null)
            {
                //Creating Protocol Data Unit(PDU) pbject which is a protocol for transferring message
                Object[] mypdu = (Object[])dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for(int i = 0; i< mypdu.length; i++)
                {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        String format = dataBundle.getString("format");
                        //From PDU we get all object and SmsMessage Object using following line of code
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i], format);
                    }
                    else
                    {
                        //API level 23
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i]);
                    }
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
                }
                Toast.makeText(context, "Message: " + msg + "\nNumber: " + phoneNo, Toast.LENGTH_LONG).show();
                //MainActivity.arrayList.add(phoneNo + " " + msg + " Encrypted");
            }
        }
    }

    public void MessageReceived(){



    }
}