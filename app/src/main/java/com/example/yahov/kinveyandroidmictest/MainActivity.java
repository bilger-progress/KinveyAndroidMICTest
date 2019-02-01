package com.example.yahov.kinveyandroidmictest;

import android.net.Uri;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyMICCallback;
import com.kinvey.android.model.User;
import com.kinvey.android.store.UserStore;
import com.kinvey.java.core.KinveyClientCallback;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // TODO: Set the values below!
    private Client mKinveyClient;
    private Button kinveyLoginButton;
    private String kinveyAppKey = "xxx";
    private String kinveyAppSecret = "xxx";
    private String kinveyInstanceID = "xxx";
    private String redirectURI = "myRedirectURI://"; // The same should be set on the Kinvey MIC service configuration!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kinveyLoginButton = findViewById(R.id.KinveyLoginButton);

        // Set-up Kinvey Client.
        mKinveyClient = new Client.Builder(kinveyAppKey, kinveyAppSecret, this).setInstanceID(kinveyInstanceID).build();

        // Ping Kinvey Backend.
        mKinveyClient.ping(new KinveyPingCallback() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(getApplicationContext(), "Kinvey Ping Response: " + aBoolean.toString(), Toast.LENGTH_LONG).show();
                kinveyLoginButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Exception was thrown. Check logs.", Toast.LENGTH_LONG).show();
                System.out.println("Exception was thrown: " + throwable.getMessage());
            }
        });

        kinveyLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKinveyClient.getActiveUser() != null) {
                    // There's a User logged-in. Please log-out.
                    UserStore.logout(mKinveyClient, new KinveyClientCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "Exception was thrown. Check logs.", Toast.LENGTH_LONG).show();
                            throwable.printStackTrace();
                        }
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Proceed with Social Login.
                            defaultKinveyMICLogin();
                        }
                    });
                } else {
                    // No User present. Proceed.
                    defaultKinveyMICLogin();
                }
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        UserStore.onOAuthCallbackReceived(intent,null, mKinveyClient);
    }

    /**
     * Calls for default Kinvey MIC login flow.
     * Will open a popup window, where the User can enter their
     * username and password.
     */

    private void defaultKinveyMICLogin () {
        mKinveyClient.setMICApiVersion("3");
        UserStore.loginWithMIC(mKinveyClient,null, redirectURI,
                new KinveyMICCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(getApplicationContext(), "Logged-in successfully. Please check log messages.", Toast.LENGTH_LONG).show();
                        System.out.println(user);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        Toast.makeText(getApplicationContext(), "Exception was thrown. Check logs.", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }

                    @Override
                    public void onReadyToRender(String myURLToRender) {
                        // Time to render the login page for the user!
                        // This renders the login page with the device's default browser.
                        Uri uri = Uri.parse(myURLToRender);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    }
                });
    }
}