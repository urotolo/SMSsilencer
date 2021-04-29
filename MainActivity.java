package com.example.silenced;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//Asymmetric Encryption

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.KeyPair;
import java.security.KeyPairGenerator;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText textMsg, textPhoneNo;
    String messageToSend, targetPhoneNum;
    String[] rsaHashValueSplitInParts;
    Button send;
    PublicKey myPublicKeys;

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    private ListView list;

   //  KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DH");
   //  keyPairGen.(2048);
    // keyPairGen.initialize(2048);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }

        textMsg = findViewById(R.id.textMsg);
        textPhoneNo = findViewById(R.id.textPhoneNo);
        send = findViewById(R.id.send);
        list = (ListView) findViewById(R.id.myList);

        send.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View V){
                sendTextMessage();
            }
        });
    } //OnCreate

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
            {
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSION_GRANTED
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // Now broadcastreceiver will work in background
                    Toast.makeText(this, "Thank you for permitting!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Cant operate correctly unless you, yes you human, permit me.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void sendTextMessage()  {
        messageToSend = textMsg.getText().toString();
        try {
            AsymmetricCryptography encryptOrDecrypt = new AsymmetricCryptography();
            messageToSend = encryptOrDecrypt.encrypt(messageToSend);
            myPublicKeys = encryptOrDecrypt.getPublicKeys();

          //  messageToSend = encryptOrDecrypt.decrypt(); // decrypt function works!!

            ///*   rsaHashValueSplitInParts = encryptOrDecrypt.encrypt(messageToSend);
            //messageToSend = rsaHashValueSplitInParts[0];
            //messageToSend = encryptOrDecrypt.decrypt(messageToSend);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

     /*   } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }*/

        targetPhoneNum = textPhoneNo.getText().toString();

        try
        {
            //Use SmsManager API to send sms
            SmsManager smsManager = SmsManager.getDefault();






    ////////////////////////////////////////////////////////////////////////////////////////////////
         //   for(int i = 0; i < 4; ++i) {
  /*              String rsaHashPartOne = rsaHashValueSplitInParts[0];
                String rsaHashPartTwo = rsaHashValueSplitInParts[1];
                String rsaHashPartThree = rsaHashValueSplitInParts[2];
                String rsaHashPartFour = rsaHashValueSplitInParts[3]; */

                long start = System.currentTimeMillis();

         /*       smsManager.sendTextMessage(targetPhoneNum, null, rsaHashPartOne, null, null);
                Thread.sleep(2000); // Accidental DDoS prevention...Fix bounds division from static 236 rsa hash length to dynamic hash length divide by 4
                smsManager.sendTextMessage(targetPhoneNum, null, rsaHashPartTwo, null, null);
                Thread.sleep(2000); // Equals 2 seconds
                smsManager.sendTextMessage(targetPhoneNum, null, rsaHashPartThree, null, null);
                Thread.sleep(2000);
                smsManager.sendTextMessage(targetPhoneNum, null, rsaHashPartFour, null, null);*/


    //////////////////////////////////////////////////////////////////////////////////////////////// Below is Important

                int lengthInt = messageToSend.length();
                String lengthString = Integer.toString(lengthInt);
                // Odd RSA hash vakues return sms failed yet still send sms
                // even RSA hash values return sms send and function most optimally do to even divide by N
                if(lengthInt % 4 != 0){
                    System.out.println("This string cannot be divided into 4 even parts!");
                    int counter = 0;
                    for(int i = 0; i < lengthInt; i = i+(lengthInt/4))
                    {
                        if(counter == 3) {
                            String part = messageToSend.substring(i); // Takes everything from index i and after? Seems to send sms but also throws string out of bounds exception
                            Thread.sleep(2000);                 // thus simoultaneously triggering the catch clause. I think the single variate substr(i) takes the rest of
                            smsManager.sendTextMessage(targetPhoneNum, null, part, null, null); // the RSA hash string plus more then needed indexes!!
                            Thread.sleep(2000);
                            counter++;
                        }
                        else {
                            String part = messageToSend.substring(i, i + (lengthInt / 4));
                            Thread.sleep(2000);
                            smsManager.sendTextMessage(targetPhoneNum, null, part, null, null);
                            Thread.sleep(2000);
                            counter++;
                        }

                    }
                }
                else {
                    for(int i = 0; i < lengthInt; i = i+(lengthInt/4))
                    {
                        // Divide string in 4 or N equal parts with substr
                        String part = messageToSend.substring(i,i+(lengthInt/4));
                        Thread.sleep(2000);
                        smsManager.sendTextMessage(targetPhoneNum, null, part, null, null);
                        Thread.sleep(2000);
                    }

                }

        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), "Failed to send SMS!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void checkMessages(View view)
    {
        setContentView(R.layout.receive_sms_version3);
    }

    public void goHome(View view)
    {
        setContentView(R.layout.activity_main);
    }





}