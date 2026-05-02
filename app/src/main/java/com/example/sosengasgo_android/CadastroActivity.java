package com.example.sosengasgo_android;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
    private Button btn_cadastrar;
    private FirebaseAuth usuario = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        startElements();

        btn_cadastrar.setOnClickListener(view -> {
            String nome = edt_nome.getText().toString();
            String email = edt_email.getText().toString();
            String senha = edt_senha.getText().toString();
            String confirma_senha = etd_confirma_senha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirma_senha.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Necessário preencher todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!senha.equals(confirma_senha)){
                Toast.makeText(CadastroActivity.this, "Senhas diferentes!", Toast.LENGTH_SHORT).show();
                return;
            }


            cadastrarUsuario(nome, email, senha);


        });

    }

    private void cadastrarUsuario(String nome, String email, String senha){

        usuario.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if( task.isSuccessful() ) {
                                    FirebaseUser firebaseUser = usuario.getCurrentUser();
                                    if (firebaseUser != null) {
                                        String uid = firebaseUser.getUid();
                                        salvarDadosUsuario(uid, nome, email);
                                    } else {
                                        Log.e("DEBUG", "Tentativa de gravar dados sem usuário logado!");
                                    }
                                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar!", Toast.LENGTH_SHORT).show();
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
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FIREBASE", dataSnapshot.getValue().toString() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FIREBASE","Falha ao ler os valores.", databaseError.toException());
            }
        });
        User user = new User();
        user.setNome(nome);
        user.setEmail(email);

        users.child(uid).setValue(user);

    }
    private void startElements() {
        edt_nome = findViewById(R.id.edt_nome);
        edt_email = findViewById(R.id.edt_email);
        edt_senha = findViewById(R.id.edt_senha);
        etd_confirma_senha = findViewById(R.id.edt_confirma_senha);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
    }

}