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
// Variables
    private ArrayList<Bitmap> Lista; //ArrayList clase permite almacenar datos en memoria de forma similar a los Arrays, con ventaja de el numero de elementos que almacena de forma dinamica
    private Context context;
    ImagenesFragment ventana;
// Metodo hacer visible la ventana en cualquier clase
    public FotoAdapter(ImagenesFragment ventana,ArrayList<Bitmap> lista, Context context) {
        this.ventana = ventana; //this hace referencia al objeto actual de la clase
        Lista = lista;
        this.context = context;
    }

    @NonNull
    @Override //Metodo de clase principal que sobreescribira en ese momento
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false); //Tiene una referencia/instanciar un layout xml para poder utilizar las vistas
        return new ViewHolder(v); //retornar una nueva vista
    }

    @Override //Metodo main no retorna ningun valor
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bit=Lista.get(position);
        holder.image.setImageBitmap(bit);

        holder.image.setOnClickListener(view -> {
            AbrirCarousel_Imagenes(position);
        });
    }
 //Metodo que solo puede usarse dentro de la misma clase
    private void AbrirCarousel_Imagenes(int position)
    {
        Carousel_Imagenes carousel_imagenes = new Carousel_Imagenes(ventana,Lista, position);
        carousel_imagenes.show(((FragmentActivity) context).getSupportFragmentManager(),"hgy");
    }

    @Override //Metodo que puede ser usada por cualquier clase en cualquier paquete
    public int getItemCount() {
        return Lista.size();
    }

 //Metodo indica que en lugar de pertenecer a una instancia del tipo que se acaba de declarar pertence a un tipo en si mismo
    public static class ViewHolder extends RecyclerView.ViewHolder {
     //Metodo solo es visible dentro de la clase donde se define
        private ImageView image;
      //Metodo visible en cualquier clase
        public ViewHolder(@NonNull View itemView) {
            super(itemView); //Invocar el metodo constructor de una clase superior (clase padre)
            image=itemView.findViewById(R.id.foto); //articulo vista de la imagen
        }
    }
}
