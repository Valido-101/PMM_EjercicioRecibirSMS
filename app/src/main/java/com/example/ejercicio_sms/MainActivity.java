package com.example.ejercicio_sms;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Elementos necesarios para realizar el ejercicio
    ActivityResultLauncher<String> requestPermissionLauncher;
    private static MainActivity mainActivity_instance;
    Button btn_comprobar;
    EditText text_codigo;
    private static String mensaje_codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Aplicamos el layout correspondiente a la actividad
        setContentView(R.layout.activity_main);

        //Obtenemos las vistas que necesitamos
        btn_comprobar = (Button)findViewById(R.id.btn_comprobar);

        text_codigo = (EditText)findViewById(R.id.text_codigo);

        //Proporcionamos un OnClickListener al botón de comprobación
        btn_comprobar.setOnClickListener(this);

        //Guardamos una instancia de esta actividad para poder referirnos a ella desde el BroadcastReceiver
        mainActivity_instance = this;

        //Método necesario para pedir permisos al usuario en tiempo de ejecución
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>(){
            @Override
            public void onActivityResult(Boolean isGranted){
                if (isGranted) {
// Permission is granted. Continue the action or workflow in your
// app.
                    //doSomeTaskAsync();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //Si ya se ha dado permiso procedemos el desarrollo normal de la aplicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
// You can use the API that requires the permission.
            //doSomeTaskAsync();
        }
        //Si no se pide permiso y se sigue el desarrollo normal de la aplicación
        else {
// You can directly ask for the permission.
// The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS);
        }
    }

    //Método que nos permite referenciar a esta actividad desde el BroadcastReceiver
    public static MainActivity  devuelveInstancia(){
        return mainActivity_instance;
    }

    //Método que sirve para actualizar el contenido del EditText
    public void actualizarEditText(final String t) {
        //Ejecutamos este método en un hilo
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                //Obtenemos el EditText
                EditText editText = (EditText) findViewById(R.id.text_codigo);
                //Actualizamos el texto que contiene
                editText.setText(t);
            }
        });
    }

    //Método que nos permite almacenar el mensaje recibido para poder comprobar si es correcto
    public void setMensajeCodigo(String s)
    {
        mensaje_codigo=s;
    }

    @Override
    public void onClick(View v) {
        //Proceso para obtener el código del mensaje recibido

        //Expresión regular necesaria para obtener el código
        String regex = "(\\d{8}$)";

        //Patrón que buscará y encontrará el código en el texto del mensaje
        Pattern patron = Pattern.compile(regex);

        //Matcher que buscará el patrón en el texto del mensaje, que guardamos previamente
        Matcher matcher = patron.matcher(mensaje_codigo);

        try {
            //Encontramos el trozo de texto que coincide con el patrón
            matcher.find();

            //Lo extraemos y comprobamos si es correcto
            if((text_codigo.getText().toString().equals(matcher.group(1))))
            {
                //Si lo es informamos al usuario
                Toast.makeText(this,"Código correcto",Toast.LENGTH_LONG).show();
            }
            else
                {
                    //Si no lo es, informamos al usuario
                    Toast.makeText(this,"Código incorrecto", Toast.LENGTH_LONG).show();
                }

        }catch(IllegalStateException e)
        {
            //Si no se ha encontrado el patrón, se informa al usuario
            Toast.makeText(this,"Formato de código inválido", Toast.LENGTH_LONG).show();
        }
    }
}