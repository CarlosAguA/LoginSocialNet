package com.example.android.loginsocialnet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {
    /*Variables primer video*/
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    /*Variables segundo video u opción B) para obtener datos de usuario */
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i("TAG: ", "OnCreate LoginActivity");
        //FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginButton);

      loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i("TAG: ", "Pasamos onSuccess");
                        Log.i("TAG ", loginResult.getAccessToken().getPermissions().toString()) ;
                        goMainScreen();

                    }

                    @Override
                    public void onCancel() {
                        Log.i("TAG: ", "Pasamos onCancel");
                        Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.i("TAG: ", "Pasamos onError");
                        Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void goMainScreen() {

        //Opción A que funciona sin datos de usuario
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

        //Opcíón B) para obtener datos de usuario pero con bugs

        if (Profile.getCurrentProfile() != null)
        {
            Profile profile = Profile.getCurrentProfile();
            Intent intent = new Intent(this, MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //Obtener datos

            // Tratar de ver como agregar una excepción o algo que me deje avanzar aunque esten vacíos los datos
            //Después ver por que razón estarían vacíos los datos

            Log.d("Nombre de Usuario", profile.getFirstName() ) ;
            Log.d("ID de Usuario", profile.getId() ) ;
            intent.putExtra("name", profile.getFirstName());
            intent.putExtra("id" , profile.getId() ) ;
            startActivity(intent);
        }
        else {
            ProfileTracker profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    stopTracking();
                    Profile.setCurrentProfile(currentProfile);
                    // Desde aquí ya se puede obtener la información como en el caso de arriba
                }
            };
            profileTracker.startTracking();
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG: ", "Pasamos onActivity result");
    }

}