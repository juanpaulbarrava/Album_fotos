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

import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.album_fotos.databinding.ActivityCropImageViewBinding;
import com.canhub.cropper.CropImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;


public class Crop_Image_View extends AppCompatActivity {

    CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image_view);
        cropImageView = this.findViewById(R.id.cropImageView);
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

        ImageButton imgBtnRegresar = this.findViewById(R.id.imgBtnRegresar);
        imgBtnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view) {
                onBackPressed();
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

        Button btnlisto = this.findViewById(R.id.btnListo);
        btnlisto.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {
                Bitmap bitmap = cropImageView.getCroppedImage();
                Intent intent = new Intent();
                intent.setData(getImageUri(getBaseContext(), bitmap));
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        });


        FloatingActionButton rotarIzquierda = this.findViewById(R.id.btnRotarIzquierda);
        rotarIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override              //Metodo main no retorna ninguna vista al hacer clic
            public void onClick(View view)
            {
                cropImageView.rotateImage(-1);
            }
        });

        FloatingActionButton rotarDerecha = this.findViewById(R.id.btnRotarDerecha);
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
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        File file = new File(inContext.getCacheDir(),"CUSTOM NAME"); //Get Access to a local file.
        file.delete(); // Delete the File, just in Case, that there was still another File
        try
        {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte[] bytearray = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(bytearray);
            fileOutputStream.flush();
            fileOutputStream.close();
            byteArrayOutputStream.close();

            URI URI = file.toURI();
            return Uri.parse(URI.toString());
        }
        catch (Exception e){

        }

        return  null;
    }
}