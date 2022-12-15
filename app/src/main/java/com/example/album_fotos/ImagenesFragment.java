package com.example.album_fotos;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.CrossProfileApps;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.RotateDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
//Metodo accesible desde cualquier otra clase
public class ImagenesFragment extends Fragment {
    //Metodo visible dentro de la clase donde se define
    private ImagenesFragment binding;
    private RecyclerView Lista_fotos;
    private ItemTouchHelper itemTouchHelper;
    private FotoAdapter adapter;

    private ArrayList<Bitmap> fotos = new ArrayList<>();

    private File archivo;
    LinearLayout lnResultado;


 //Metodo visible en cualquier clase
    public ImagenesFragment(LinearLayout lnResultado){

        this.lnResultado = lnResultado;
    }


    @Override //Metodo que esta en la clase principal y se sobreescribira en ese momento
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {    //Metodo que se utiliza para construir y añadir las views a los diseños de (Fragment, Adapter, Dialogs, Menu o ActionBar)
        View view = inflater.inflate(R.layout.fragment_imagenes, container, false);
        //View root = binding.getView().getRootView();

        // fotos hace referencia al objeto actual de la clase

        Lista_fotos = view.findViewById(R.id.lista_fotos);
        adapter = new FotoAdapter(this,fotos, getContext());
        Lista_fotos.setLayoutManager(new GridLayoutManager(getContext(), 4));
        Lista_fotos.setAdapter(adapter);
        //activar clase  desplazar fotos para organizarlas
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(Lista_fotos);

       // destacar la accion principal de una pantalla siempre y cuando contituya una operacion habitual natural
        FloatingActionButton btn=view.findViewById(R.id.fotografiar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {hacerFoto();}
        });

        //retornar vista
        return view;
    }

  // metodo main no retorna ningun valor
    public void hacerFoto() {
        try {  //bloque que contiene una o mas sentencias
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            archivo = crearFichero();
            Uri foto = FileProvider.getUriForFile(getContext(), "com.example.album_fotos.fileprovider", archivo); //identificador uniforme de recursos
            i.putExtra(MediaStore.EXTRA_OUTPUT, foto);
            startActivityForResult(i, 1);
        }catch (IOException e){
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show(); // un objeto de vista que se despliega como un elemento emergente en la interfaz de usuario
        }

    }
    @Override //Metodo que esta en la clase principal y se sobreescribira en ese momento
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            //condicion
            /*CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); // Identificador uniforme de recursos

                Bitmap image = BitmapFactory.decodeFile(resultUri.getPath()); //mapa de bits
                fotos.add(image);
                adapter.notifyDataSetChanged();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }*/
        } else {
            if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    //Habilitar LinearLayout para hacer visible boton procesar y Extraer Texto, TextView para mostrar texto de extraccion
                    lnResultado.setVisibility(View.VISIBLE);
                    //convertir imagen Uri y posterior a recortar imagen
                    croprequest(Uri.parse( archivo.toURI().toString()));

                }else{
                    archivo.delete();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    //~~~~~~~~~~~~~~Metodo para recortar imagen~~~~~~~~~~~~~~~~~~~~~~~~~//
    private void croprequest(Uri imageUri) {
        /*CropImage.ActivityResult(imageUri)
                .setActivityMenuIconColor(Color.BLACK)
                .setActivityTitle("Foto Capturada")
                .setGuidelines(CropImageView.Guidelines.ON)
                .setBorderLineColor(Color.WHITE)
                .setGuidelinesColor(Color.WHITE)
                .setMultiTouchEnabled(true)
                .start(getContext(),this);*/

        Intent intent = new Intent(getView().getContext(), Crop_Image_View.class);
        intent.putExtra("imageUri", imageUri);
        startActivityForResult(intent, 1446);


    }

    public  void ActualizarImagenes(){
        adapter.notifyDataSetChanged();
    }

    public File crearFichero() throws IOException{
        String pre="foto";
        File directorio=getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img=File.createTempFile( pre + UUID.randomUUID().toString(), "jpg", directorio);
        return img;
    }

    //declarar metodo publico para pasar arreglo a otro activity
    public ArrayList<Bitmap> ObtenerImagenes(){
        return fotos;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
    //***************clase mover fotos para su acomodo************
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int posicionAnterior = viewHolder.getAdapterPosition();
            int posicionNueva = target.getAdapterPosition();
            Collections.swap(fotos, posicionAnterior, posicionNueva);
            recyclerView.getAdapter().notifyItemMoved(posicionAnterior, posicionNueva);
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };

}

