package com.example.album_fotos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.ViewHolder> {

    private ArrayList<Bitmap> Lista;
    private Context context;

    public FotoAdapter(ArrayList<Bitmap> lista, Context context) {
        Lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bit=Lista.get(position);
        holder.image.setImageBitmap(bit);

        holder.image.setOnClickListener(view -> {
            AbrirCarousel_Imagenes(position);

        }
        );



    }

    private void AbrirCarousel_Imagenes(int position)
    {

        Carousel_Imagenes carousel_imagenes = new Carousel_Imagenes(Lista, position);
        carousel_imagenes.show(((FragmentActivity) context).getSupportFragmentManager(),"hgy");
    }


    @Override
    public int getItemCount() {
        return Lista.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.foto);
        }
    }
}
