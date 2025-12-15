package com.example.pokemon.ui.list;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pokemon.data.local.PokemonEntity;
import com.example.pokemon.data.repository.PokeRepository;

import java.util.List;

public class ListViewModel extends AndroidViewModel {

    private final PokeRepository repository;

    public ListViewModel(@NonNull Application application) {
        super(application);
        // Inicializo  el repositorio (que a su vez inicia la BD)
        repository = new PokeRepository(application);
    }


    // Si la tabla 'pokemons' cambia (ej: capturas uno), esta lista se actualiza sola.
    public LiveData<List<PokemonEntity>> getUserPokemons(String username) {
        return repository.getUserPokemons(username);
    }



    //Metodo para borrar la cuenta
    public void deleteAccount(String username, Runnable onDeleted) {
        repository.deleteUserFull(username, onDeleted);
    }
}
