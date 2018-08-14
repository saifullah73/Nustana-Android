package com.example.saif.nustana;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class HomeActivity extends AppCompatActivity {
    // brandind screen
    private ImageView gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        gif = findViewById(R.id.gifHere);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gif);
        Glide.with(getApplicationContext()).load(R.drawable.anim_logo).into(imageViewTarget);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        }, 4000);


    }
}
