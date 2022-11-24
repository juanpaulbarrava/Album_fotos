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

public class Carousel_Imagenes extends DialogFragment {

    //Vistas
    private CarouselView carouselView;

    //Objetos
    private PhotoViewAttacher zoom = null;
    ArrayList<Bitmap> imagenes;
    int position;


    public Carousel_Imagenes(ArrayList<Bitmap> imagenes, int position) {
        this.position = position;
        this.imagenes = imagenes;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null)
            dialog.getWindow().setLayout(width, height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_carousel_imagenes, container, false);
        Button regresar = view.findViewById(R.id.boton_salir_carousel);
        Button eliminar = view.findViewById(R.id.eliminar_imagen_actual);


        carouselView = view.findViewById(R.id.carousel);

        carouselView.setFillColor(Color.rgb(117, 245, 66));
        carouselView.setPageCount(imagenes.size());
        carouselView.setImageListener(imageListener());
        carouselView.setCurrentItem(position);
        return view;
    }

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
}

