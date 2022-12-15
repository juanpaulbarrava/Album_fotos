package com.example.album_fotos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.album_fotos.databinding.ActivityCropImageViewBinding;
import com.canhub.cropper.CropImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Crop_Image_View extends AppCompatActivity {

    CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image_view);
        cropImageView = this.findViewById(R.id.cropImageView);


        //cropImageView.setGuidelines(CropImageView.Guidelines.ON);
        cropImageView.setGuidelines(CropImageView.Guidelines.ON);
        cropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        cropImageView.setAutoZoomEnabled(false);
        cropImageView.setShowProgressBar(true);

        Intent i = this.getIntent();
        Bundle obp = i.getExtras();
        if (obp != null){
            Uri imageUri = obp.getParcelable("imageUri");
            cropImageView.setImageUriAsync(imageUri);
        }

        ImageButton regresar = this.findViewById(R.id.imgBtnRegresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view) {
                onBackPressed();
                //Intent intent = new Intent(Crop_Image_View.this, ImagenesFragment.class);
                //startActivity(intent);
            }
        });

        Button listo = this.findViewById(R.id.btnListo);
        listo.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {
                Bitmap bitmap = cropImageView.getCroppedImage();
            }
        });

        FloatingActionButton rotarIzquierda = this.findViewById(R.id.rotarIzquierda);
        rotarIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {
                cropImageView.rotateImage(-1);
            }
        });

        FloatingActionButton rotarDerecha = this.findViewById(R.id.rotarDerecha);
        rotarDerecha.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {
                cropImageView.rotateImage(1);
            }
        });

        FloatingActionButton resetear = this.findViewById(R.id.btnResetear);
        resetear.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {
                cropImageView.resetCropRect();
            }
        });

        ImageButton imgBtnRotar90 = this.findViewById(R.id.imgBtnRotar90);
        imgBtnRotar90.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {
                cropImageView.rotateImage(90);
            }
        });


    }
}