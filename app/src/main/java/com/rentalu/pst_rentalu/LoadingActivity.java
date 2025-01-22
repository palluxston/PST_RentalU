package com.rentalu.pst_rentalu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 3000;

    Animation slide, bot, fade_in;
    ImageView imgView;
    TextView logo, slogan, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        MusicManager.getMediaPlayer(this).start();
        //animations
        slide = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom_animation);
        bot = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);

        //Elements
        imgView = (ImageView) findViewById(R.id.imageView);
        logo = (TextView) findViewById(R.id.Logo);
        slogan = (TextView) findViewById(R.id.slogan);
        btnLogin = (TextView) findViewById(R.id.btnLogin);

        imgView.setAnimation(slide);
        logo.setAnimation(bot);
        slogan.setAnimation(bot);



        //adding two animation for the login button
        AnimationSet AS = new AnimationSet(false);
        AS.addAnimation(bot);
        AS.addAnimation(fade_in);
        btnLogin.setAnimation(AS);

        //job of btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LoadingActivity.this, login_page.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}