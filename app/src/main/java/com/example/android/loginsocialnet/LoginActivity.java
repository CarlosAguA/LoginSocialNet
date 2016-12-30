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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    /*Variables primer video*/
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    /*Variables segundo video u opción B) para obtener datos de usuario */
    //private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i("TAG: ", "OnCreate LoginActivity");
        //FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("TAG: 1. ", " LLegamos onSuccess");
                        Log.d("TAG  2. Permisos ", loginResult.getAccessToken().getPermissions().toString()) ;

                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {
                                            String email = object.getString("email");
                                            Log.d("TAG 6.-Email = ", " " + email);
                                            String birthday = object.getString("birthday"); // 01/31/1980 format
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                        goMainScreen();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG: ", "Pasamos onCancel");
                        Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG: ", "Pasamos onError");
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

            Log.d("TAG 3.-Nomb de Usuario", profile.getFirstName() ) ;
            Log.d("TAG 4.-ID de Usuario  ", profile.getId() ) ;
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
        Log.d("TAG: 5. ", "Pasamos onActivity result");
    }

}