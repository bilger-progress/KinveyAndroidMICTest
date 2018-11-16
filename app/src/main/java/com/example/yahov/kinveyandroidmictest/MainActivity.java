package com.example.yahov.kinveyandroidmictest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Client mKinveyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set-up Kinvey Client.
        mKinveyClient = new Client.Builder("kid_Sk18GADum", "2e4f66f377964d96a67bd09e2286c6ee", this).build();

        // Ping Kinvey Backend.
        mKinveyClient.ping(new KinveyPingCallback() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(getApplicationContext(), "Kinvey Ping Response: " + aBoolean.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Exception was thrown. Check logs.", Toast.LENGTH_LONG).show();
                System.out.println("Exception was thrown: " + throwable.getMessage());
            }
        });
    }
}
