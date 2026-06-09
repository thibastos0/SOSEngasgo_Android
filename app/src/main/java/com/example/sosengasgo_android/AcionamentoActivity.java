package com.example.sosengasgo_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.List;
import java.util.Locale;

public class AcionamentoActivity extends AppCompatActivity {

    private Button btn_Confirmar, btn_Cancelar;
    private ImageButton btn_acionar;
    private ImageView btn_menu;
    private FrameLayout map_Container;
    private TextView txt_confirm_warning;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // API Key para OpenStreetMap
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_acionamento);

        startComponents();

        btn_menu.setOnClickListener(view -> abrirMenu());

        btn_acionar.setOnClickListener(view -> {

            verificaLocalizacao();

        });
    }

    private void startComponents(){
        btn_acionar = findViewById(R.id.btn_acionar);
        btn_menu = findViewById(R.id.btn_menu);
    }
    private void navegaTelaSucesso(double latitude, double longitude){
        Intent telaSucesso = new Intent(AcionamentoActivity.this, SucessoActivity.class);
        telaSucesso.putExtra("latitude", latitude);
        telaSucesso.putExtra("longitude", longitude);
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

    private void navegaTelaHistorio(){
        //TODO: Navegar para tela de histórico
        //Intent telaHistoria = new Intent(AcionamentoActivity.this, HistoricoActivity.class);
        //startActivity(telaHistoria);
    }

    private void abrirMenu() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_menu_bottom_sheet, null);

        // Configurar cliques
        sheetView.findViewById(R.id.menu_perfil).setOnClickListener(v -> {
            navegaTelaPerfil();
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.menu_sair).setOnClickListener(v -> {
            mAuth.signOut();
            bottomSheetDialog.dismiss();
            navegaTelaMain();
            finish();
        });

        sheetView.findViewById(R.id.menu_historico).setOnClickListener(v -> {
            navegaTelaHistorio();
            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void verificaLocalizacao() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Tente obter a localização atual
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                confirmarSocorro(latitude, longitude);
                                //Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                                Log.i("DEBUG_Coordenadas", "Latitude: " + latitude + ", Longitude: " + longitude);
                            } else {
                                Toast.makeText(this, "Não foi possível obter a localização atual.", Toast.LENGTH_SHORT).show();
                                confirmarSocorro(-23.0913, -47.2167); //Indaiatuba para casos de erro ao detectar localização
                            }
                        });

        } else {
            // 3. Se não tem permissão, peça-a
            pedirPermissao();
        }

    }

    private void confirmarSocorro(double latitude, double longitude){

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetViewAcionamento = getLayoutInflater().inflate(R.layout.layout_confirmacao_socorro, null);

        MapView mapView = new MapView(this);

        //Tipo de mapa
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        //insere no frameLayout
        map_Container = sheetViewAcionamento.findViewById(R.id.map_container);
        map_Container.addView(mapView);

        //Coordenadas e centro do mapa
        final GeoPoint[] localSelecionado = {new GeoPoint(latitude, longitude)};
        mapView.getController().setZoom(17.5);
        mapView.getController().setCenter(localSelecionado[0]);

        //cria e insere marcador no mapa
        Marker marker = new Marker(mapView);
        marker.setPosition(localSelecionado[0]);
        Drawable iconPino = ContextCompat.getDrawable(
                this, R.drawable.ic_marcador_socorro);
        marker.setIcon(iconPino);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Localização do SOS");
        mapView.getOverlays().add(marker);

        //mapView.invalidate(); //recria o mapa com o marcador

        btn_Confirmar = sheetViewAcionamento.findViewById(R.id.btn_confirmar_socorro);
        btn_Cancelar = sheetViewAcionamento.findViewById(R.id.btn_cancelar_socorro);
        txt_confirm_warning = sheetViewAcionamento.findViewById(R.id.txt_confirm_warning);

        // Busca o endereço inicial do GPS
        atualizarEnderecoTexto(localSelecionado[0].getLatitude(), localSelecionado[0].getLongitude(), txt_confirm_warning);

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                // Se preferir que mude com clique simples, coloque o código aqui.
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                // Atualiza a posição ao segurar o dedo no mapa por 1 segundo
                localSelecionado[0] = p;
                marker.setPosition(p);
                mapView.invalidate(); // Recarrega o mapa visualmente

                // Busca o novo endereço a partir do ponto clicado!
                atualizarEnderecoTexto(p.getLatitude(), p.getLongitude(), txt_confirm_warning);
                return true;
            }
        };

        // Adiciona o detector de cliques nas camadas do mapa
        MapEventsOverlay eventsOverlay = new MapEventsOverlay(mReceive);
        mapView.getOverlays().add(eventsOverlay);

        // Ações do botão confirmar dentro do pop-up
        btn_Confirmar.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            navegaTelaSucesso(localSelecionado[0].getLatitude(), localSelecionado[0].getLongitude());
            finish();
        });

        // Ações do botão cancelar dentro do pop-up
        btn_Cancelar.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(sheetViewAcionamento);
        bottomSheetDialog.show();
    }

    private void atualizarEnderecoTexto(double latitude, double longitude, TextView txt_confirm_warning) {
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
                txt_confirm_warning.setText("O socorro será enviado para:\n" + enderecoCompleto + "\n\n(Se necessário, segure pressionado no mapa para corrigir o ponto).");
            } else {
                txt_confirm_warning.setText("Endereço por extenso não encontrado. Mas enviaremos o socorro para as coordenadas geográficas deste ponto.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback caso o aparelho esteja sem internet ou o serviço do Google de Geocode falhe temporariamente
            txt_confirm_warning.setText("Socorro será enviado para as coordenadas: " + latitude + ", " + longitude + "\n\n(Segure pressionado no mapa para corrigir o ponto).");
        }
    }

    private void pedirPermissao() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
           verificaLocalizacao();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, você pode acessar a localização
                verificaLocalizacao();
            } else {
                // Permissão negada
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
                pedirPermissao();
            }
        }
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