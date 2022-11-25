package com.example.album_fotos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout lnResultado;
    Button btnProcesarFotos;
    TextView tvEtiqueta;
    EditText tvMostrartext;

    ArrayList<Bitmap> imagenes;

    //TextRecognizer
    private TextRecognizer textRecognizer;
    //TAG
    private static final String TAG = "MAIN_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lnResultado = this.findViewById(R.id.LnResultado);

        btnProcesarFotos= this.findViewById(R.id.recognizeTextBtn);
        tvMostrartext = this.findViewById(R.id.recognizedTextEt);
        //init TextRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        ImagenesFragment fragment= new ImagenesFragment(lnResultado);
        getSupportFragmentManager().beginTransaction().replace(R.id.panelFragmento,fragment,fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        //haga clic en el controlador, comience a reconocer el texto de la imagen que tomamos de la cámara
        btnProcesarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //obtenemos de
                imagenes = fragment.ObtenerImagenes();

                //compruebe si se selecciona la imagen si imageUri no es nulo
                if (imagenes == null){
                    //imageUri es nulo, lo que significa que aún no hemos elegido la imagen, no podemos reconocer el texto
                    Toast.makeText(MainActivity.this, "Elige primero la imagen", Toast.LENGTH_SHORT).show();
                }
                else{
                    //image Uri no es nulo, lo que significa que hemos elegido la imagen, podemos reconocer el texto
                     recognizeTextFromImage();
                }
            }
        });
    }

    //~~~~~~~~~~~~Metodo reconocer texto de photo~~~~~~~~~~~~~~~~~//
    private void recognizeTextFromImage() {

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        ArrayList<String> textoReconocido;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Procesando imagenes",
                    "Espere por favor");
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "recognizeTextFromImage: ");

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMostrartext.setText("");
                }
            });

            textoReconocido = new ArrayList<String>();

            for (Bitmap imagen: imagenes)
            {
                try {
                    //Prepare InputImage from image uri
                    InputImage inputImage = InputImage.fromBitmap(imagen,0);
                    //imagen preparada, estamos a punto de iniciar el proceso de reconocimiento de texto, cambiar el mensaje de progreso

                    //iniciar el proceso de reconocimiento de texto desde la imagen
                    Task<Text> textTaskResult = textRecognizer.process(inputImage)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text text) {
                                    //proceso completado, cerrar cuadro de diálogo
                                    //obtener el texto reconocido
                                    String recognizedText = text.getText();
                                    Log.d(TAG, "onSuccess: recognizedText: "+recognizedText);
                                    //establecer el texto reconocido para editar texto
                                     //pasar a un vector los valores
                                    String[] lineas = recognizedText.split("\n");

                                    for(String linea: lineas)
                                        textoReconocido.add(linea.replace(" ",""));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //no se pudo reconocer el texto de la imagen, cerrar el cuadro de diálogo, mostrar el motivo en Toast
                                    Log.d(TAG, "onFailure: ", e);
                                    Toast.makeText(MainActivity.this, "No se pudo reconocer el texto debido a: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    Tasks.await(textTaskResult);
                }
                catch (Exception e)
                {
                }
            }
            progressDialog.dismiss();
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            EliminarRepetidos();
            tvMostrartext.setText(ObtenerTexto());
            progressDialog.dismiss();

        }

        private void EliminarRepetidos(){
            // Crear lista de duplicados nueva
            ArrayList<String> sinDuplicados = new ArrayList<String>();
            if (textoReconocido != null){
                if (textoReconocido.size() > 1){
                    //si esta lista no esta presente en la nueva lista
                    for (String Element:textoReconocido) {
                        //contains retorna true si el elemento pasado se encuentra en la lista, añadirlo
                        if(!sinDuplicados.contains(Element)){
                            sinDuplicados.add(Element);
                        }
                    }
                    textoReconocido = sinDuplicados;
                }
            }
        }

        private String ObtenerTexto(){
            String resultado = "";
            for (String Element:textoReconocido)
                resultado+=(Element+"\n");
            return resultado;
        }


        @Override
        protected void onProgressUpdate(String... text) {
        }
    }
}