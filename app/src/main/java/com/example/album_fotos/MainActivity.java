package com.example.album_fotos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment= new ImagenesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.panelFragmento,fragment,fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }
}