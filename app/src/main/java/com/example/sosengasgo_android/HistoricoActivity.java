package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosengasgo_android.adapter.HistoricoAdapter;
import com.example.sosengasgo_android.database.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class HistoricoActivity extends AppCompatActivity {

    private HistoricoAdapter adapter;
    private AppDatabase db;
    private Button btn_limpar_historico, btn_voltar_historico;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        startComponents();

        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            mAuth = null;
        }

        // Inicializar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_historico);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Criar adapter
        adapter = new HistoricoAdapter();
        recyclerView.setAdapter(adapter);

        // Inicializar banco de dados
        db = AppDatabase.getDatabase(this);

        // Observar mudanças no banco de dados
        db.buttonActivationDao().getAllActivations().observe(this, buttonActivations -> {
            // Atualizar o RecyclerView quando houver mudanças
            adapter.setActivations(buttonActivations);
        });

        btn_limpar_historico.setOnClickListener(v-> limparHistorico());

        btn_voltar_historico.setOnClickListener(view -> navegaTelaAcionamento());

    }

    private void startComponents(){
        btn_limpar_historico = findViewById(R.id.btn_limpar_historico);
        btn_voltar_historico = findViewById(R.id.btn_voltar_historico);
    }

    private void navegaTelaAcionamento(){
        Intent telaAcionamento = new Intent(HistoricoActivity.this, AcionamentoActivity.class);
        startActivity(telaAcionamento);
        finish();
    }

    private void limparHistorico(){
        // Apagar todos os registros do banco de dados
        if (mAuth != null) {
            String userId = mAuth.getCurrentUser().getUid();

            AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    db.buttonActivationDao().deleteAll(userId);
                    Log.i("DEBUG_Historico", "Registros do histórico de acionamentos apagados com sucesso!");
                }
            });


        }

    }

}
