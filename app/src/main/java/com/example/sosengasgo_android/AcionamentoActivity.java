package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

public class AcionamentoActivity extends AppCompatActivity {

    ImageButton btn_acionar;
    ImageView btn_menu;
    FirebaseAuth usuario = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acionamento);

        startComponents();

        btn_menu.setOnClickListener(view -> abrirMenu(view));
        btn_acionar.setOnClickListener(v -> navegaTelaSucesso());

    }

    private void startComponents(){
        btn_acionar = findViewById(R.id.btn_acionar);
        btn_menu = findViewById(R.id.btn_menu);
    }
    private void navegaTelaSucesso(){
        Intent telaSucesso = new Intent(AcionamentoActivity.this, SucessoActivity.class);
        startActivity(telaSucesso);
    }

    private void navegaTelaPerfil(){
        //Intent telaPerfil = new Intent(AcionamentoActivity.this, PerfilActivity.class);
        //startActivity(telaPerfil);
    }

    private  void navegaTelaMain(){
        Intent telaMain = new Intent(AcionamentoActivity.this, MainActivity.class);
        telaMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(telaMain);
    }

    private void abrirMenu(View v) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_menu_bottom_sheet, null);

        // Configurar cliques
        view.findViewById(R.id.menu_perfil).setOnClickListener(view1 -> {
            navegaTelaPerfil();
            Toast.makeText(this, "Perfil clicado", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        view.findViewById(R.id.menu_sair).setOnClickListener(view1 -> {
            usuario.signOut();
            bottomSheetDialog.dismiss();
            navegaTelaMain();
            finish();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }


}