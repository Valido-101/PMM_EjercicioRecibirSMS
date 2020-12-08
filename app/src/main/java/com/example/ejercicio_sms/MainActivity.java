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

    ActivityResultLauncher<String> requestPermissionLauncher;
    private static MainActivity mainActivity_instance;
    Button btn_comprobar;
    EditText text_codigo;
    private static String mensaje_codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_comprobar = (Button)findViewById(R.id.btn_comprobar);

        text_codigo = (EditText)findViewById(R.id.text_codigo);

        btn_comprobar.setOnClickListener(this);

        mainActivity_instance = this;

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

        //Si ya se ha dado permiso procedemos con la descarga
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
// You can use the API that requires the permission.
            //doSomeTaskAsync();
        }
        //Si no se pide permiso y se hace la descarga
        else {
// You can directly ask for the permission.
// The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS);
        }
    }

    public static MainActivity  devuelveInstancia(){
        return mainActivity_instance;
    }

    public void actualizarEditText(final String t) {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = (EditText) findViewById(R.id.text_codigo);
                editText.setText(t);
            }
        });
    }

    public void setMensajeCodigo(String s)
    {
        mensaje_codigo=s;
    }

    @Override
    public void onClick(View v) {
        String regex = "(\\d{8}$)";

        Pattern patron = Pattern.compile(regex);

        Matcher matcher = patron.matcher(mensaje_codigo);

        try {
            matcher.find();

            if((text_codigo.getText().toString().equals(matcher.group(1))))
            {
                Toast.makeText(this,"Código correcto",Toast.LENGTH_LONG).show();
            }
            else
                {
                    Toast.makeText(this,"Código incorrecto", Toast.LENGTH_LONG).show();
                }

        }catch(IllegalStateException e)
        {
            Toast.makeText(this,"Formato de código inválido", Toast.LENGTH_LONG).show();
        }
    }
}