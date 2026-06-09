package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btn_login,btn_signup;
    private MaterialButton btn_GoogleLogin;
    private FirebaseAuth usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try {
            usuario = FirebaseAuth.getInstance();
        } catch (Exception e) {
            // Se o Firebase não estiver inicializado (ex: sem google-services.json), 
            // evitamos o crash aqui. Em produção, isso deve ser resolvido com a configuração correta.
            usuario = null;
        }

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        btn_GoogleLogin = findViewById(R.id.btn_GoogleLogin);


        btn_login.setOnClickListener(v -> navegaTelaLogin());
        btn_signup.setOnClickListener(v ->  navegaTelaCadastro());
        btn_GoogleLogin.setOnClickListener(view -> {
            Intent telaLogin = new Intent(MainActivity.this, LoginActivity.class);
            telaLogin.putExtra("googleChoosed", true);
            startActivity(telaLogin);
        });

    }

    private void navegaTelaLogin(){
        Intent telaLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(telaLogin);
    }

    private void navegaTelaCadastro(){
        Intent telaCadastro = new Intent(MainActivity.this, CadastroActivity.class);
        telaCadastro.putExtra("isNewUser", true);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (usuario != null) {
            FirebaseUser logged_user = usuario.getCurrentUser();

            if (logged_user != null) {
                navegaTelaAcionamento();
            }
        }
    }

    private void navegaTelaAcionamento(){
        Intent telaAcionamento = new Intent(MainActivity.this, AcionamentoActivity.class);
        startActivity(telaAcionamento);
        finish();
    }

}