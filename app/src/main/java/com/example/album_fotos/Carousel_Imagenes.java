package com.example.album_fotos;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import com.synnapps.carouselview.CarouselView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;
//clase publica accesible desde cualquier otra clase
public class Carousel_Imagenes extends DialogFragment {

    //Vistas
    private CarouselView carouselView;

    //Objetos
    private PhotoViewAttacher zoom = null;
    ArrayList<Bitmap> imagenes;
    int position;
    ImagenesFragment ventana;


    public Carousel_Imagenes(ImagenesFragment ventana,ArrayList<Bitmap> imagenes, int position) {
        this.ventana = ventana; //hace referencia al objeto actual de la clase en este caso ventana
        this.position = position; //hace referencia al objeto actual de la clase
        this.imagenes = imagenes; //hace referencia al objeto actual de la clase
        int width = ViewGroup.LayoutParams.MATCH_PARENT; //MATCH_PARENT hace que la vista se expanda lo mas posible dentro de la vista principal
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null)
            dialog.getWindow().setLayout(width, height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) { //Metodo visible en cualquier clase onCreate fragmento debe diseñar la interfaz de usuario por primera vez
        View view = inflater.inflate(R.layout.fragment_carousel_imagenes, container, false); //inflater se utiliza para instnciar el contenido de los archivos XML de diseño
        Button regresar = view.findViewById(R.id.boton_salir_carousel);
        Button eliminar = view.findViewById(R.id.eliminar_imagen_actual);
        regresar.setOnClickListener(view1 -> dismiss()); // muestra lista tradicional desaparece la vista y se regresa cierra ventana
        eliminar.setOnClickListener(view2 -> EliminarImagen()); //se elimina la imagen que ya no queremos


        carouselView = view.findViewById(R.id.carousel);

        carouselView.setFillColor(Color.rgb(117, 245, 66));
        carouselView.setPageCount(imagenes.size());
        carouselView.setImageListener(imageListener());
        carouselView.setCurrentItem(position);
        return view;
    }
    //metodo visible dentro de la clase donde manda llamar la imagen y retornara una position
    private ImageListener imageListener() {
        return (position, imageView) ->
        {
            imageView.setBackgroundResource(R.drawable.borde_con_fondo_blanco);
            imageView.setPadding(4,4,4,4);
            imageView.setImageBitmap(imagenes.get(position));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            zoom = new PhotoViewAttacher(imageView);
        };
    }
  //Metodo solo puede usarse dentro de la misma clase del button void no regresa nada
    private  void EliminarImagen() {
        //condicion para que no haga nada el botton despues de eliminar imagenes y cierre view
        if (imagenes.size() == 0) { //al dar clic en eliminar se iran eliminando miestras halla imagenes sea mayor a 0
            Toast.makeText(getContext(), "No tiene imagenes", Toast.LENGTH_LONG).show(); //si hay fotos una vez se borre y si ya no hay muestre el mensaje
         //si aun hay imagenes las muestra hasta que ya se eliminen todas se cierre ventana
        } else {
            imagenes.remove(carouselView.getCurrentItem()); //retira una imagen de carousel y la obtienen al articulo actual
            carouselView.setPageCount(imagenes.size()); //pone numero de imagenes  y el tamaño
            carouselView.setImageListener(imageListener()); //manda llamar la imagen que seleccionamos
            ventana.ActualizarImagenes(); //se actualiza las imagenes
            //carouselView.removeView(carouselView.getChildAt(carouselView.getCurrentItem()));

            if (imagenes.size() == 0)
                dismiss(); //dismiss cerrar ventanas


        }
    }
}

