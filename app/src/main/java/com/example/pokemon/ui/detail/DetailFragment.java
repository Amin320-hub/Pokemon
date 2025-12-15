package com.example.pokemon.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.pokemon.data.remote.PokeApiService;
import com.example.pokemon.data.remote.RetrofitClient;
import com.example.pokemon.data.remote.response.PokemonResponse;
import com.example.pokemon.databinding.ActivityDetailBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private ActivityDetailBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null) {
            String name = getArguments().getString("pokemonName");
            int id = getArguments().getInt("pokemonId");

            binding.tvDetailName.setText(name);
            loadPokemonDetails(id);
        }

        binding.btnBack.setOnClickListener(v -> {
            // Volver atrás en el historial de navegación
            Navigation.findNavController(view).popBackStack();
        });
    }

    private void loadPokemonDetails(int id) {
        PokeApiService api = RetrofitClient.getClient().create(PokeApiService.class);
        api.getPokemonDetail(id).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonResponse p = response.body();

                    // 1. Mostrar el PODER
                    if (!p.getStats().isEmpty()) {
                        binding.tvDetailPower.setText("Poder Base: " + p.getStats().get(0).getBaseStat());
                    }

                    // Tipo
                    // La API devuelve una lista de tipos. Valido que no esté vacía.
                    if (p.getTypes() != null && !p.getTypes().isEmpty()) {
                        // Accedemos a: Lista -> Posición 0 -> Objeto Type -> Nombre
                        String typeName = p.getTypes().get(0).getType().getName();
                        binding.tvDetailType.setText("Tipo: " + typeName.toUpperCase());
                    } else {
                        binding.tvDetailType.setText("Tipo: Desconocido");
                    }

                    // Imagen
                    if (getContext() != null) {
                        Glide.with(getContext())
                                .load(p.getSprites().getFrontDefault())
                                .into(binding.ivDetailImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                // Error
                if (getContext() != null) {
                    android.widget.Toast.makeText(getContext(), "Error de conexión", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}