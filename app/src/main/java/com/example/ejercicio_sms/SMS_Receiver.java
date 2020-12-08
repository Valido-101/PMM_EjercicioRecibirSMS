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
        // Elementos necesarios para obtener el mensaje sms
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Obtenemos el sms recibido y lo almacenamos en un array de objetos que obtendremos del extra "pdus" del intent
        Object[] pdus = (Object[]) bundle.get("pdus");
        //Si el array no es null (se ha recibido un mensaje)
        if (pdus != null) {
            // Rellenamos el array de sms
            msgs = new SmsMessage[pdus.length];
            //Recorremos el array de sms y creamos un mensaje a raíz de la pdu en la posición especificada
            for (int i = 0; i < msgs.length; i++) {

                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);

                // Obtenemos el texto del mensaje
                strMessage += msgs[i].getMessageBody() + "\n";

                // Obtenemos el código de entre todos los caracteres del texto mediante una expresión regular
                String regex = "(\\d{8}$)";

                //Creamos el patrón para recuperar el trozo de texto deseado
                Pattern patron = Pattern.compile(regex);

                //Creamos el matcher que nos permitirá encontrar y extraer dicho trozo del texto
                Matcher matcher = patron.matcher(strMessage);

                try {
                    //Encontramos el código
                    matcher.find();

                    //Obtenemos una instancia del MainActivity e invocamos al método actualizarEditText(), pasándole
                    //como parámetro el resultado del matcher.group()
                    MainActivity.devuelveInstancia().actualizarEditText(matcher.group(1));

                    //Pasamos al MainActivity el mensaje recibido para que pueda realizar la comprobación
                    MainActivity.devuelveInstancia().setMensajeCodigo(strMessage);

                }catch(IllegalStateException e)
                {
                    //Si el formato del código no sirve se lanza una excepción para que no se cierre la app
                    Toast.makeText(context,"Código no válido", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}