package com.example.saif.nustana.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.saif.nustana.HomeActivity;
import com.example.saif.nustana.MainActivity;
import com.example.saif.nustana.R;

public class WelcomeScreenActivity extends AppCompatActivity {

    private static final String TAG = "Welcome Screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welome_screen);


        Button sign_up_button = findViewById(R.id.sign_up);
        Button sign_in_button = findViewById(R.id.sign_in);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sign_up_intent = new Intent(getApplicationContext(), LoginActivity.class);
                sign_up_intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                sign_up_intent.putExtra("mode", "sign_up");
                startActivity(sign_up_intent);
                finish();
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_in_intent = new Intent(getApplicationContext(), LoginActivity.class);
                sign_in_intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                sign_in_intent.putExtra("mode", "log_in");
                startActivity(sign_in_intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED);
        finish();
    }
}