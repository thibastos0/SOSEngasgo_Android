package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SucessoActivity extends AppCompatActivity {

    Button btn_finalizar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sucesso);

        btn_finalizar = findViewById(R.id.btn_finalizar);

        btn_finalizar.setOnClickListener(v -> navegaTelaAcionamento());
    }

    private void navegaTelaAcionamento(){
        Intent telaAcionamento = new Intent(SucessoActivity.this, AcionamentoActivity.class);
        startActivity(telaAcionamento);
    }

}