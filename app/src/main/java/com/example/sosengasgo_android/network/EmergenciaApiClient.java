package com.example.sosengasgo_android.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EmergenciaApiClient {

    //private static final String BASE_URL = "https://sosengasgo.onrender.com";
    //private static final String BASE_URL = "https://sosengasgo-sosengasgoproduction.up.railway.app";
    private static final String BASE_URL = "https://sosengasgo-api-telegram.vercel.app";

    private final OkHttpClient client = new OkHttpClient();
    private final Context context;

    public EmergenciaApiClient(Context context) {
        this.context = context;
    }

    public interface Callback {
        void onSucesso(String chave);
        void onErro(String mensagem);
    }

    public void acionarEmergencia(String firebaseToken, double lat, double lng, long acionamentoId, String uid, Callback callback) {
        String chave = acionamentoId + "_" + uid;
        String json = "{\"latitude\":" + lat
                + ",\"longitude\":" + lng
                + ",\"acionamento_id\":" + acionamentoId
                + ",\"uid\":\"" + uid + "\""
                + ",\"chave\":\"" + chave + "\"}";

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/emergencia/acionar")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + firebaseToken)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onErro("Falha de conexão: " + e.getMessage());
                Log.e("DEBUG_Emergencia", "Erro ao acionar emergência: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respostaCompleta = response.body().string();
                //para DEBUG
                /*
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, respostaCompleta, Toast.LENGTH_LONG).show()
                );*/

                if (response.isSuccessful()) {
                    callback.onSucesso(chave);
                    Log.i("DEBUG_Emergencia", "Emergência acionada com sucesso! \n" + respostaCompleta);
                } else {
                    callback.onErro("Erro " + response.code() + ": " + respostaCompleta);
                    Log.e("DEBUG_Emergencia", "Erro ao acionar emergência: " + respostaCompleta);
                }
            }
        });
    }

    public interface StatusCallback {
        void onConfirmado(String resposta);
        void onAguardando();
    }

    public void consultarStatus(String chave, String firebaseToken, StatusCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/emergencia/status/" + chave)
                .get()
                .addHeader("Authorization", "Bearer " + firebaseToken)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("DEBUG_Status", "Falha: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String body = response.body().string();
                    org.json.JSONObject json = new org.json.JSONObject(body);
                    if (json.optBoolean("confirmado", false)) {
                        callback.onConfirmado(json.optString("resposta", "Responsável a caminho!"));
                    } else {
                        callback.onAguardando();
                    }
                } catch (Exception e) {
                    Log.e("DEBUG_Status", "Erro ao parsear: " + e.getMessage());
                }
            }
        });
    }
}