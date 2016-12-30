package com.example.android.loginsocialnet;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        }

      /*
       Quitar comentarios para probar opción B) y recibir datos del intent que se crea
       en el método goMainscreen()de la Actividad LoginActivity
      */

        //Bundle inBundle = getIntent().getExtras();
        //String name = inBundle.get("name").toString();
        /*Prueba para ver log*/
        //String nombreUsuario = getIntent().getStringExtra("name");
        //String idUsuario = getIntent().getStringExtra("id");
        //Log.d("Nombre de Usuario", nombreUsuario);
       // Log.d("ID de Usuario", idUsuario);
      /*
       TextView nameView = (TextView)findViewById(R.id.text_view_full_name);
       nameView.setText( nombreUsuario) ;
       */

    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        //intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        Log.d("Cerro sesión Usuario", "Sesión finalizada" ) ;
        startActivity(intent) ;
    }

    public void logout (View view) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }


}
