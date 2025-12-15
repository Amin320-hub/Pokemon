package com.example.pokemon.ui.search;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pokemon.data.local.PokemonEntity;
import com.example.pokemon.data.remote.PokeApiService;
import com.example.pokemon.data.remote.RetrofitClient;
import com.example.pokemon.data.remote.response.PokemonResponse;
import com.example.pokemon.data.repository.PokeRepository;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {
    private PokeRepository repository;
    private MutableLiveData<PokemonResponse> wildPokemon = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();
    private List<PokemonEntity> myTeam;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new PokeRepository(application);
    }

    public LiveData<PokemonResponse> getWildPokemon() { return wildPokemon; }
    public LiveData<String> getMessage() { return message; }

    //  Cargar mi equipo actual (para saber mi poder)
    public void loadMyTeam(String username) {
        repository.getUserPokemons(username).observeForever(pokemons -> {
            this.myTeam = pokemons;
        });
    }

    //  Buscar enemigo aleatorio
    public void searchEnemy() {
        int randomId = new Random().nextInt(150) + 1; // Genera ID del 1 al 150

        //  Uso Retrofit directamente para detectar IOException (Internet)
        PokeApiService api = RetrofitClient.getClient().create(PokeApiService.class);
        api.getPokemonDetail(randomId).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    wildPokemon.postValue(response.body());
                } else {
                    message.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                //Por si no hay internet
                if (t instanceof IOException) {
                    message.postValue("No hay conexión a Internet. Revisa tu red.");
                } else {
                    message.postValue("Error: " + t.getMessage());
                }
            }
        });
    }

    // Intento capturar
    public void tryCapture(String username) {
        PokemonResponse wild = wildPokemon.getValue();
        if (wild == null || myTeam == null) return;

        // Comprobar duplicados
        for (PokemonEntity p : myTeam) {
            if (p.getPokeApiId() == wild.getId()) {
                message.setValue("¡Ya tienes este Pokémon!");
                return;
            }
        }

        int myTotalPower = 0;
        for (PokemonEntity p : myTeam) myTotalPower += p.getPower();

        //  Si no tengo pokemons , capturo siempre. Si tengo, mi poder > wild.baseStat
        if (myTeam.isEmpty() || myTotalPower > wild.getStats().get(0).getBaseStat()) {
            PokemonEntity newP = new PokemonEntity(
                    wild.getId(),
                    wild.getName(),
                    wild.getStats().get(0).getBaseStat(),
                    wild.getSprites().getFrontDefault(),
                    username
            );
            repository.capturePokemon(newP);
            message.setValue("¡Capturado exitosamente!");
        } else {
            message.setValue("Es muy fuerte (" + wild.getStats().get(0).getBaseStat() +
                    "). Tu poder es solo " + myTotalPower);
        }
    }
}