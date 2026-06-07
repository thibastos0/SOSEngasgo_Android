package com.example.sosengasgo_android;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private Button btn_entrar;
    private MaterialButton btn_GoogleLogin;
    private TextView txt_cadastro_spannable;
    private EditText login_user, login_pass;
    private ProgressBar progressBar_login;
    private Boolean googleChoosed = false;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        startComponents();

        btn_entrar.setOnClickListener(v -> login(v));
        btn_GoogleLogin.setOnClickListener(v->signInGoogle());
        txt_cadastro_spannable.setOnClickListener(view -> navegaTelaCadastro());

    }

    private void startComponents(){
        login_user = findViewById(R.id.login_user);
        login_pass = findViewById(R.id.login_pass);
        btn_entrar = findViewById(R.id.btn_entrar);
        btn_GoogleLogin = findViewById(R.id.btn_GoogleLogin);
        txt_cadastro_spannable = findViewById(R.id.txt_cadastro_spannable);
        progressBar_login = findViewById(R.id.progressBar_login);
    }

    private void signInGoogle() {
        // Instantiate a Google sign-in request
        // defaul_web_client_id só aparece se der build já com o novo google-services.json
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        // Create the Credential Manager request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CredentialManager credentialManager = CredentialManager.create(this);
        //referênica: https://github.com/firebase/snippets-android
        // caminho: auth/app/src/main/java/com/google/firebase/quickstart/auth/GoogleSignInActivity.java
        credentialManager.getCredentialAsync(
                this,
                request,
                null,
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        //Pega a credencial e chama signin
                        handleSignIn(result.getCredential());
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        Log.e("GoogleAuth", "Erro ao obter credencial. e: " + e.getLocalizedMessage());
                    }
                }
        );
    }

    private void handleSignIn(Credential credential) {
        // Check if credential is of type Google ID
        if (credential instanceof CustomCredential customCredential
                && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = customCredential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.w("GoogleAuth", "Credencial não é do tipo Google ID!");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("GoogleAuth", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        boolean isNewUser = task.getResult()
                                .getAdditionalUserInfo()
                                .isNewUser();

                        if (isNewUser) {
                            Intent navegaTelaCadastro = new Intent(this, CadastroActivity.class);
                            navegaTelaCadastro.putExtra("nome", user.getDisplayName());
                            navegaTelaCadastro.putExtra("email", user.getEmail());
                            navegaTelaCadastro.putExtra("isNewUser", isNewUser);
                            startActivity(navegaTelaCadastro);
                        } else {
                            navegaTelaAcionamento();
                        }
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w("GoogleAuth", "signInWithCredential:failure ", task.getException());
                    }
                });
    }

    private void login(View v){
        String email_user = login_user.getText().toString();
        String pass_user = login_pass.getText().toString();

        if (email_user.isEmpty() || pass_user.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email_user, pass_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
        FirebaseUser logged_user = mAuth.getCurrentUser();

        if(logged_user != null) {
            progressBar_login.setVisibility(View.VISIBLE);
            navegaTelaAcionamento();
        }

        if (getIntent().getBooleanExtra("googleChoosed", false)) {
            signInGoogle();
        }

    }

    private void navegaTelaAcionamento(){
        Intent telaAcionamento = new Intent(LoginActivity.this, AcionamentoActivity.class);
        startActivity(telaAcionamento);
    }

    private void navegaTelaCadastro(){
        Intent telaCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        telaCadastro.putExtra("isNewUser", true);
        startActivity(telaCadastro);
    }

}