package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_login,btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        */

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(v -> navegaTelaLogin());
        btn_signup.setOnClickListener(v ->  navegaTelaCadastro());








    }

    private void navegaTelaLogin(){
        Intent telaLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(telaLogin);
    }

    private void navegaTelaCadastro(){
        Intent telaCadastro = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(telaCadastro);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*
        ImageView imgLogo = findViewById(R.id.img_logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.heart_beat);
        animation.setRepeatCount(1);
        imgLogo.startAnimation(animation);*/

        if (hasFocus) {
            final ImageView imgLogo = findViewById(R.id.img_logo);
            final Animation anim = AnimationUtils.loadAnimation(this, R.anim.heart_beat);

            imgLogo.startAnimation(anim);

            new android.os.Handler().postDelayed(() -> imgLogo.startAnimation(anim), 650);
        }
    }

}