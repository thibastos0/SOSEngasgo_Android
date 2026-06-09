package com.example.sosengasgo_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosengasgo_android.database.AppDatabase;

public class HistoricoActivity extends AppCompatActivity {

    private HistoricoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        // Inicializar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_historico);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Criar adapter
        adapter = new HistoricoAdapter();
        recyclerView.setAdapter(adapter);

        // Inicializar banco de dados
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

        // Observar mudanças no banco de dados
        db.buttonActivationDao().getAllActivations().observe(this, buttonActivations -> {
            // Atualizar o RecyclerView quando houver mudanças
            adapter.setActivations(buttonActivations);
        });
    }
}
