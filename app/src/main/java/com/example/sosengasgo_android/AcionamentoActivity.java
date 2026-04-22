package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AcionamentoActivity extends AppCompatActivity {

    ImageButton btn_acionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acionamento);

        btn_acionar = findViewById(R.id.btn_acionar);

        btn_acionar.setOnClickListener(v -> navegaTelaSucesso());

    }

    private void navegaTelaSucesso(){
       // Intent telaSucesso = new Intent(AcionamentoActivity.this, SucessoActivity.class);
        //startActivity(telaSucesso);
    }
}