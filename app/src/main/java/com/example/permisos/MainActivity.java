package com.example.permisos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables globales
    private ActivityResultLauncher<String> requestCallPermissionLauncher;
    private ActivityResultLauncher<String> requestCameraPermissionLauncher;

    Button btn_youtube, btn_llamar, btn_sms, btn_mapa, btn_camara, btn_WhatsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarLanzadoresPermisos();

        btn_youtube = findViewById(R.id.btn_youtube);
        btn_llamar = findViewById(R.id.btn_llamar);
        btn_sms = findViewById(R.id.btn_sms);
        btn_mapa = findViewById(R.id.btn_mapa);
        btn_camara = findViewById(R.id.btn_camara);
        btn_WhatsApp = findViewById(R.id.btn_WhatsApp);

        btn_youtube.setOnClickListener(this);
        btn_llamar.setOnClickListener(this);
        btn_sms.setOnClickListener(this);
        btn_mapa.setOnClickListener(this);
        btn_camara.setOnClickListener(this);
        btn_WhatsApp.setOnClickListener(this);
    }

    private void configurarLanzadoresPermisos() {
        requestCallPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        ejecutarLlamadaDirecta();
                    } else {
                        Toast.makeText(this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        ejecutarIntentCamara();
                    } else {
                        Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId(); // obtenemos el id del botón activado

        // Corregido: Llave de cierre agregada y usamos los IDs reales de los botones
        if (id == R.id.btn_youtube) {
            PW(v);
        } else if (id == R.id.btn_llamar) {
            CP(v);
        } else if (id == R.id.btn_sms) {
            enviarSMS(v);
        } else if (id == R.id.btn_mapa) {
            abrirMapa(v);
        } else if (id == R.id.btn_camara) {
            abrirCamara(v);
        }
        else if (id == R.id.btn_WhatsApp) {
            MW(v);
        }
    }

    public void PW(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
        startActivity(intent);
    }

    public void CP(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            ejecutarLlamadaDirecta();
        } else {
            requestCallPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }

    private void ejecutarLlamadaDirecta() {
        // Corregido: Se quitó 'uriString:'
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:2471002255"));
        startActivity(intent);
    }

    public void enviarSMS(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:2471002255"));
        intent.putExtra("sms_body", "Hola, te escribo desde mi aplicación de Android Studio.");
        startActivity(intent);
    }

    public void abrirMapa(View v) {
        String geoUri = "geo:19.0413,-98.2062?q=19.0413,-98.2062(Puebla)";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        startActivity(intent);
    }

    public void abrirCamara(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            ejecutarIntentCamara();
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void ejecutarIntentCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No se encontro ninguna aplicación de camara", Toast.LENGTH_SHORT).show();
        }
    }
    public void MW(View V) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hola, mensaje enviado desde Android Studio");
        sendIntent.setType("text/plain");

        // Nombre del paquete de WhatsApp en una línea separada
        sendIntent.setPackage("com.whatsapp");

        try {
            // Ejecuta la acción de abrir WhatsApp
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            // Muestra un mensaje en pantalla si la app no está instalada
            android.widget.Toast.makeText(V.getContext(), "WhatsApp no está instalado", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}