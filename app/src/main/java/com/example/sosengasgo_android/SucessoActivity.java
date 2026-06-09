package com.example.sosengasgo_android;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosengasgo_android.database.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class SucessoActivity extends AppCompatActivity {

    private Button btn_finalizar, btn_cancelar_acionamento;
    private TextView txt_instrucao_topo;
    private AppDatabase db;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sucesso);

        startComponents();

        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            mAuth = null;
        }

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);
        atualizarEnderecoTexto(latitude, longitude);
        long id = getIntent().getLongExtra("id", 0);

        db = AppDatabase.getDatabase(this);

        btn_finalizar.setOnClickListener(v -> {
            String status = "finalizado";
            encerraAcionamento(id, status);
        });

        btn_cancelar_acionamento.setOnClickListener( v-> {
            String status = "cancelado";
            encerraAcionamento(id, status);
        });

    }

    private void startComponents(){
        btn_finalizar = findViewById(R.id.btn_finalizar);
        btn_cancelar_acionamento = findViewById(R.id.btn_cancelar_acionamento);
        txt_instrucao_topo = findViewById(R.id.txt_instrucao_topo);
    }

    private void atualizarEnderecoTexto(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            // Pede ao Android para buscar o endereço mais próximo dessa coordenada
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Monta uma string legível (Rua, Número - Cidade)
                String rua = address.getThoroughfare() != null ? address.getThoroughfare() : "Rua não identificada";
                String numero = address.getFeatureName() != null ? ", " + address.getFeatureName() : "";
                String bairro = address.getSubLocality() != null ? " - " + address.getSubLocality() : "";
                String cidade = address.getSubAdminArea() != null ? " - " + address.getSubAdminArea() : "";

                String enderecoCompleto = rua + numero + bairro + cidade;

                // Atualiza o texto do aviso com o endereço real
                txt_instrucao_topo.setText("O socorro enviado para:\n" + enderecoCompleto + "\n\nInicie a manobra agora.");
            } else {
                Log.d("DEBUG_Coordenadas", "Endereço por extenso não encontrado. Mas enviaremos o socorro para as coordenadas: " + latitude + ", " + longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback caso o aparelho esteja sem internet ou o serviço do Google de Geocode falhe temporariamente
            txt_instrucao_topo.setText("Socorro enviado para as coordenadas: " + latitude + ", " + longitude + "\n\nInicie a manobra agora.");
        }
    }

    private void encerraAcionamento(long id, String status) {

        String userId = mAuth.getCurrentUser().getUid();

        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.buttonActivationDao().updateStatus(id, userId, status);
            Log.i("DEBUG_Status_Update", "Status atualizado com sucesso: " + status + ".");

                runOnUiThread(() -> {
                    navegaTelaAcionamento();
                    finish();
                });
            }
        });

    }

    private void navegaTelaAcionamento(){
        Intent telaAcionamento = new Intent(SucessoActivity.this, AcionamentoActivity.class);
        startActivity(telaAcionamento);
    }

}