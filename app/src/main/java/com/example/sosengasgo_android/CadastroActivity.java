package com.example.sosengasgo_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sosengasgo_android.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CadastroActivity extends AppCompatActivity {

    private EditText edt_nome, edt_email, edt_senha, etd_confirma_senha;
    private TextView txt_reg_title;
    private Button btn_cadastrar, btn_cancelar;
    private boolean loginComGoogle = false;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private String uid;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        startComponents();

        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
            loginComGoogle = firebaseUser.getProviderData().stream()
                    .anyMatch(info -> info.getProviderId().equals("google.com"));
        }

        boolean isNewUser = getIntent().getBooleanExtra("isNewUser", false);
        //getIntent().removeExtra("isNewUser");

        if (isNewUser && loginComGoogle) {
            btn_cancelar.setVisibility(View.GONE);
            edt_senha.setVisibility(View.GONE);
            etd_confirma_senha.setVisibility(View.GONE);
            btn_cadastrar.setText("Cadastrar com Google");
            String nome = getIntent().getStringExtra("nome");
            String email = getIntent().getStringExtra("email");
            edt_nome.setText(nome);
            edt_email.setText(email);
        }

        if (!isNewUser) {
            LoadData(firebaseUser);
            edt_senha.setVisibility(View.GONE);
            etd_confirma_senha.setVisibility(View.GONE);
            txt_reg_title.setText("Dados do Usuário");
            btn_cadastrar.setText("Atualizar");
        }

        btn_cancelar.setOnClickListener(view -> voltaTelaMain());

        btn_cadastrar.setOnClickListener(view -> {
            String nome = edt_nome.getText().toString();
            String email = edt_email.getText().toString();

            if (isNewUser && !loginComGoogle) {

                String senha = edt_senha.getText().toString();
                String confirma_senha = etd_confirma_senha.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirma_senha.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Necessário preencher todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!senha.equals(confirma_senha)) {
                    Toast.makeText(CadastroActivity.this, "Senhas diferentes!", Toast.LENGTH_SHORT).show();
                    return;
                }
                cadastrarUsuario(nome, email, senha);
                return;
            }

            if (nome.isEmpty() || email.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Necessário preencher todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseUser = mAuth.getCurrentUser();
            if (firebaseUser == null) { return; }
            salvarDadosUsuario(firebaseUser.getUid(), nome, email);
        });

    }

    private void startComponents() {
        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        txt_reg_title = findViewById(R.id.txt_reg_title);
        etd_confirma_senha = findViewById(R.id.edt_confirma_senha);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        btn_cancelar = findViewById(R.id.btn_cancelar);
    }

    private void cadastrarUsuario(String nome, String email, String senha){

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if( task.isSuccessful() ) {
                                    firebaseUser = mAuth.getCurrentUser();
                                    if (firebaseUser != null) {
                                        salvarDadosUsuario(firebaseUser.getUid(), nome, email);
                                    } else {
                                        Log.e("DEBUG", "Tentativa de gravar dados sem usuário logado!");
                                    }
                                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar!", Toast.LENGTH_SHORT).show();
                                    navegaTelaMain();
                                } else {
                                    String erro;
                                    try {
                                        throw  task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        erro = "Digite uma senha com no mínimo 6 caracteres";
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        erro = "E-mail já cadastrado";
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        erro = "E-mail inválido";
                                    } catch (Exception e) {
                                        erro = "Erro ao cadastra usuário!";
                                    }
                                    Toast.makeText(CadastroActivity.this, erro, Toast.LENGTH_SHORT).show();
                                }
                            }
                });
    }

    private void salvarDadosUsuario(String uid, String nome, String email){
        DatabaseReference users = reference.child("users");

        User userData = new User();
        userData.setNome(nome);
        userData.setEmail(email);

        users.child(uid).setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Sucesso ao gravar/atualizar dados!", Toast.LENGTH_SHORT).show();
                voltaTelaMain();
            } else {
                Exception e = task.getException();
                Toast.makeText(this, "Erro ao gravar/atualizar dados! " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadData(FirebaseUser firebaseUser) {

        DatabaseReference user = reference.child("users");
        user.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    User userData = task.getResult().getValue(User.class);

                    if (userData != null) {
                        edt_nome.setText(userData.getNome());
                        edt_email.setText(userData.getEmail());
                    }

                    else {
                        Log.e("firebase", "Erro ao ler dados!", task.getException());
                        return;
                    }

                }
            }
        });
    }

    private void navegaTelaMain(){
        Intent telaMain = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(telaMain);
        mAuth.signOut();
    }

    private void voltaTelaMain(){
        Intent telaMain = new Intent(this, MainActivity.class);
        // Limpa o empilhamento de telas para não poluir o fluxo do app
        telaMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(telaMain);
        finish();
    }

    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {   }

}