package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btn_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        btn_entrar = findViewById(R.id.btn_entrar);

        btn_entrar.setOnClickListener(v -> navegaTelaAcionamento());

    }

    private void navegaTelaAcionamento(){
        Intent telaAcionamento = new Intent(LoginActivity.this, AcionamentoActivity.class);
        startActivity(telaAcionamento);
    }

}