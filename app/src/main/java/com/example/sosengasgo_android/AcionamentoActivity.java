package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AcionamentoActivity extends AppCompatActivity {

    private Button btn_Confirmar, btn_Cancelar;
    private ImageButton btn_acionar;
    private ImageView btn_menu;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acionamento);

        startComponents();

        btn_menu.setOnClickListener(view -> abrirMenu());

        btn_acionar.setOnClickListener(view -> confirmarSocorro());

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
        Intent telaPerfil = new Intent(AcionamentoActivity.this, CadastroActivity.class);
        startActivity(telaPerfil);
    }

    private  void navegaTelaMain(){
        Intent telaMain = new Intent(AcionamentoActivity.this, MainActivity.class);
        telaMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(telaMain);
    }

    private void confirmarSocorro(){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AcionamentoActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_confirmacao_socorro, null);
        bottomSheetDialog.setContentView(sheetView);

        btn_Confirmar = sheetView.findViewById(R.id.btn_confirmar_socorro);
        btn_Cancelar = sheetView.findViewById(R.id.btn_cancelar_socorro);

        // Ações do botão confirmar dentro do pop-up
        btn_Confirmar.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            navegaTelaSucesso();
        });

        // Ações do botão cancelar dentro do pop-up
        btn_Cancelar.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    private void abrirMenu() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_menu_bottom_sheet, null);

        // Configurar cliques
        view.findViewById(R.id.menu_perfil).setOnClickListener(v -> {
            navegaTelaPerfil();
            bottomSheetDialog.dismiss();
        });

        view.findViewById(R.id.menu_sair).setOnClickListener(v -> {
            mAuth.signOut();
            bottomSheetDialog.dismiss();
            navegaTelaMain();
            finish();
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void reload() { }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

}