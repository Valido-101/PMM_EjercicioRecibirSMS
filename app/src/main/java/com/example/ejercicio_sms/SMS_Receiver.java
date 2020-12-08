package com.example.ejercicio_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import androidx.annotation.RequiresApi;

public class SMS_Receiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus != null) {
            // Check the Android version.
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                String regex = "(\\d{8}$)";

                Pattern patron = Pattern.compile(regex);

                Matcher matcher = patron.matcher(strMessage);

                try {
                    matcher.find();

                    MainActivity.devuelveInstancia().actualizarEditText(matcher.group(1));

                    MainActivity.devuelveInstancia().setMensajeCodigo(strMessage);

                }catch(IllegalStateException e)
                {
                    Toast.makeText(context,"Código no válido", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}