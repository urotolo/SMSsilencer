package com.example.silenced;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AsymmetricCryptography {

    KeyPairGenerator keyPairGen;
    KeyPair keyPair;
    Cipher cipher;
    byte[] receivedMessage;
    byte[] encryptedText;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {


            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            keyPair = keyPairGen.generateKeyPair();
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

    }

    public PublicKey getPublicKeys(){

        return keyPair.getPublic();

    }

    public PrivateKey getPrivateKeys(){

        return keyPair.getPrivate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String encrypt(String message) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        receivedMessage = message.getBytes(StandardCharsets.UTF_8); //Might need to be a standard char sets .utf_8
        cipher.update(receivedMessage);
        encryptedText = cipher.doFinal();
        String cipherAsString = new String(encryptedText, "UTF8");



    ////////////////////////////////////////////////////////////////////////////////////////////////

        // RSA hashvalue length may not always equal 237 so calculate each time for precision
      //  int len = cipherAsString.length();
     /*   while(len % 2 != 0){
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            keyPair = keyPairGen.generateKeyPair();
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            cipher.update(receivedMessage);
            encryptedText = cipher.doFinal();
            cipherAsString = new String(encryptedText, "UTF8");
            len = cipherAsString.length();
        } */

        //cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        //byte[] decipheredText = cipher.doFinal(encryptedText);


      /*  int oneFourthRSAhashValueLength = len / 4;
        //String lenStr = Integer.toString(len);

        // RSA hashValue to large to be sent in one text so divide into 4 chunks of roughly 60 characters each that can be sent over SMS
        // Must be exact or there will be off by one index errors!
        String splitCipherOne = cipherAsString.substring(0,oneFourthRSAhashValueLength);
        String splitCipherTwo = cipherAsString.substring(oneFourthRSAhashValueLength, ((oneFourthRSAhashValueLength*2)));  // RSA str length 237 and text box can only hold 140 P.S may not always be 237 only roughly so
        String splitCipherThree = cipherAsString.substring((oneFourthRSAhashValueLength*2), ((oneFourthRSAhashValueLength*3)));
        String splitCipherFour =  cipherAsString.substring(oneFourthRSAhashValueLength*3, ((oneFourthRSAhashValueLength*4)+1));
        String[] rsaHashValueInParts = new String[4];
        rsaHashValueInParts[0] = splitCipherOne;
        rsaHashValueInParts[1] = splitCipherTwo;
        rsaHashValueInParts[2] = splitCipherThree;
        rsaHashValueInParts[3] = splitCipherFour; */


       // return rsaHashValueInParts;


     ///////////////////////////////////////////////////////////////////////////////////////////////


        return cipherAsString;
        //return new String(decipheredText, "UTF-8");
        //return new String(receivedMessage, "UTF8");
        //System.out.println(new String(encryptedText, "UTF8"));
    }










    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String decrypt() throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] decipheredText = cipher.doFinal(encryptedText);
        return new String(decipheredText, "UTF-8");


    ////////////////////////////////////////////////////////////////////////////////////////////////

        //byte[] plainText = cipher.doFinal(convertedToBytesCipherText);
        //String plainTextStr = new String(plainText, "UTF8");
       // int len = plainTextStr.length();
      //  String length = Integer.toString(len);
        //String length = "ihi";
      //  return length;
      //  return new String(decipheredText, "UTF-8");
     //   return cipherText.substring(0,100);

    ////////////////////////////////////////////////////////////////////////////////////////////////

    }

}
