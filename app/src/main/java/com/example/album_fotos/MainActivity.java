package com.example.album_fotos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout lnResultado;
    Button btnProcesarFotos;
    TextView tvEtiqueta;
    EditText etVerExtracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recognizeTextBtn = this.findViewById(R.id.recognizeTextBtn);
        LinearLayout lnResultado = this.findViewById(R.id.LnResultado);

        /*//init UI Views
        recognizeTextBtn = findViewById(R.id.recognizeTextBtn);
        imageIv = findViewById(R.id.imageIv);
        recognizedTextEt = findViewById(R.id.recognizedTextEt);

        // iniciar arreglo de permisos requeridos para cámara, galería
        cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        //init TextRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        //manejar clic, mostrar cuadro de diálogo de imagen de entrada
        inputImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputImageDialog();
            }
        });

        //haga clic en el controlador, comience a reconocer el texto de la imagen que tomamos de la cámara / galería
        recognizeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //compruebe si se selecciona la imagen si imageUri no es nulo
                if (imageUri == null){
                    //imageUri es nulo, lo que significa que aún no hemos elegido la imagen, no podemos reconocer el texto
                    Toast.makeText(MainActivity.this, "Elige primero la imagen", Toast.LENGTH_SHORT).show();
                }
                else{
                    //image Uri no es nulo, lo que significa que hemos elegido la imagen, podemos reconocer el texto
                    recognizeTextFromImage();
                }
            }
        });*/



        Fragment fragment= new ImagenesFragment(lnResultado);
        getSupportFragmentManager().beginTransaction().replace(R.id.panelFragmento,fragment,fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }


}