package com.example.pokemon.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.pokemon.data.local.AppDao;
import com.example.pokemon.data.local.AppDatabase;
import com.example.pokemon.data.local.PokemonEntity;
import com.example.pokemon.data.local.UserEntity;
import com.example.pokemon.data.remote.PokeApiService;
import com.example.pokemon.data.remote.RetrofitClient;
import com.example.pokemon.data.remote.response.PokemonResponse;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokeRepository {
    private AppDao mAppDao;
    private PokeApiService apiService;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public PokeRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAppDao = db.appDao();
        apiService = RetrofitClient.getClient().create(PokeApiService.class);
    }

    // Usuario
    public void insertUser(UserEntity user) {
        executor.execute(() -> mAppDao.insertUser(user));
    }

    public void login(String user, String pass, LoginCallback callback) {
        executor.execute(() -> {
            UserEntity result = mAppDao.login(user, pass);
            callback.onResult(result);
        });
    }

    // POkemon
    public LiveData<List<PokemonEntity>> getUserPokemons(String username) {
        return mAppDao.getUserPokemons(username);
    }

    //meotodo para sumar el poder
    public void getUserPokemonsSync(String username, PokemonsCallback callback) {
        executor.execute(() -> {


        });
    }

    public void deleteUserFull(String username, Runnable onComplete) {
        executor.execute(() -> {
            // Borramos primero los pokemons y luego el usuario
            mAppDao.deleteUserPokemons(username);
            mAppDao.deleteUser(username);

            // Avisamos cuando termine para cambiar de pantalla
            if (onComplete != null) {
                // Volvemos al hilo principal para tocar la UI (opcional, pero útil)
                new android.os.Handler(android.os.Looper.getMainLooper()).post(onComplete);
            }
        });
    }
    public void capturePokemon(PokemonEntity pokemon) {
        executor.execute(() -> mAppDao.insertPokemon(pokemon));
    }

    // Api
    public void searchRandomPokemon(int randomId, ApiCallback callback) {
        apiService.getPokemonDetail(randomId).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Pokemon no encontrado");
                }
            }
            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                callback.onError("Error de conexión");
            }
        });
    }

    public UserEntity getUserByNameSync(String name) {
        return mAppDao.getUserByName(name);
    }
    // Interfaces para los callbacks
    public interface LoginCallback { void onResult(UserEntity user); }
    public interface ApiCallback {
        void onSuccess(PokemonResponse pokemon);
        void onError(String error);
    }
    public interface PokemonsCallback { void onResult(List<PokemonEntity> pokemons); }
}