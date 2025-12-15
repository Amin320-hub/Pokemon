package com.example.pokemon.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation; // Importante para la navegación
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokemon.R;
import com.example.pokemon.data.local.PokemonEntity;

import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<PokemonEntity> pokemonList = new ArrayList<>();

    public void setPokemons(List<PokemonEntity> pokemons) {
        this.pokemonList = pokemons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        PokemonEntity pokemon = pokemonList.get(position);

        //  Datos visuales
        holder.tvName.setText(pokemon.getName());
        holder.tvPower.setText("Poder: " + pokemon.getPower());

        // Carga de imagen con Glide
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.ivImage);

        //  Logica de Navegación
        holder.itemView.setOnClickListener(v -> {
            // Creamos el paquete de datos
            Bundle bundle = new Bundle();
            bundle.putInt("pokemonId", pokemon.getPokeApiId());
            bundle.putString("pokemonName", pokemon.getName());

            // Navegamos usando la flecha definida en tu nav_graph.xml

            Navigation.findNavController(v)
                    .navigate(R.id.action_list_to_detail, bundle);
        });
    }

    @Override
    public int getItemCount() { return pokemonList.size(); }

    // Clase interna ViewHolder
    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPower;
        ImageView ivImage;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPower = itemView.findViewById(R.id.tvPower);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}