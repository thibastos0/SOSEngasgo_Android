package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btn_entrar;
    private TextView txt_cadastro_spannable;
    private EditText login_user, login_pass;
    private ProgressBar progressBar_login;
    private FirebaseAuth usuario = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        startComponents();

        btn_entrar.setOnClickListener(v -> login(v));
        txt_cadastro_spannable.setOnClickListener(view -> navegaTelaCadastro());

    }

    private void startComponents(){
        login_user = findViewById(R.id.login_user);
        login_pass = findViewById(R.id.login_pass);
        btn_entrar = findViewById(R.id.btn_entrar);
        txt_cadastro_spannable = findViewById(R.id.txt_cadastro_spannable);
        progressBar_login = findViewById(R.id.progressBar_login);
    }

    private void login(View v){
        String email_user = login_user.getText().toString();
        String pass_user = login_pass.getText().toString();

        if (email_user.isEmpty() || pass_user.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        usuario.signInWithEmailAndPassword(email_user, pass_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()) {
                    String erro;
                    try {
                        throw  task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erro = "Email não existe ou a conta foi desativada.";
                    } catch (FirebaseNetworkException e) {
                        erro = "Dispositivo offline.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Senha incorreta ou formato do e-mail inválido";
                    } catch (Exception e) {
                        erro = "Erro ao tentar fazer o login do usuário!";
                    }
                    Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar_login.setVisibility(v.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navegaTelaAcionamento();
                    }
                }, 3000);
                finish();
            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser logged_user = usuario.getCurrentUser();

        if(logged_user != null) {
            progressBar_login.setVisibility(View.VISIBLE);
            navegaTelaAcionamento();
        }
    }

    private void navegaTelaAcionamento(){
        Intent telaAcionamento = new Intent(LoginActivity.this, AcionamentoActivity.class);
        startActivity(telaAcionamento);
    }

    private void navegaTelaCadastro(){
        Intent telaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(telaCadastro);
    }

}