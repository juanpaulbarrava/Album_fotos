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

                //obtenemos de imagenesFramegment la lista de imagenes
                imagenes = fragment.ObtenerImagenes();

                //compruebe si se selecciona la imagen si la lista no es nulo
                if (imagenes == null){
                    //imagenes es nulo, lo que significa que aún no hemos elegido la imagen, no podemos reconocer el texto
                    Toast.makeText(MainActivity.this, "Elige primero la imagen", Toast.LENGTH_SHORT).show();
                }
                else{
                    //imagenes no es nulo, lo que significa que hemos elegido la imagen, podemos reconocer el texto
                     recognizeTextFromImage();
                }
            }
        });
    }

    //~~~~~~~~~~~~Metodo reconocer texto de photo~~~~~~~~~~~~~~~~~//
    private void recognizeTextFromImage() {

        //Mandamos llamar la clase AsyncTaskRunner
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }
    //clase abstracta para tareas pesadas en segundo
    // plano y se ejecuta en un solo hilo cuando se inicia
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        //creamos lista para guardar el texto reconocido
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
            //init arreglo
            textoReconocido = new ArrayList<String>();

            //la lista "imagenes" pasa a "imagen" una por una
            for (Bitmap imagen: imagenes)
            {
                try {
                    //Prepara InputImage from imagen
                    InputImage inputImage = InputImage.fromBitmap(imagen,0);
                    //imagen preparada, estamos a punto de iniciar el proceso de reconocimiento de texto, cambiar el mensaje de progreso

                    //iniciar el proceso de reconocimiento de texto desde la imagen
                    Task<Text> textTaskResult = textRecognizer.process(inputImage)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text text) {
                                    //proceso completado
                                    //obtener el texto reconocido
                                    String recognizedText = text.getText();
                                    Log.d(TAG, "onSuccess: recognizedText: "+recognizedText);
                                    //establecer el texto reconocido para editar texto
                                     //pasar a un vector los valores y hacer salto de linea
                                    String[] lineas = recognizedText.split("\n");
                                    //pasa "Lineas" a un vector tipo String "linea" para recorrer y ver
                                    // si hay espacios y remplazarlos por "" nada para quitarlos
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
            //cerrar dialogo de cargando proceso
            progressDialog.dismiss();
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // El resultado del metodo "doInBackground" se pasa a este método
            EliminarRepetidos();
            tvMostrartext.setText(ObtenerTexto());
            progressDialog.dismiss();

        }

        //método sólo es visible dentro de la clase donde se define
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
        //metodo
        private String ObtenerTexto(){
            //declaro array vacio
            String resultado = "";
            //obtener de "textoReconocido" cada elemento y darle salto de linea para mostrarlo
            for (String Element:textoReconocido)
                resultado+=(Element+"\n");
            return resultado;
        }



        @Override
        protected void onProgressUpdate(String... text) {
        }
    }
}