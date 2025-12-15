package com.example.pokemon.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pokemon.data.local.UserEntity;
import com.example.pokemon.data.repository.PokeRepository;

public class LoginViewModel extends AndroidViewModel {
    private PokeRepository repository;
    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private MutableLiveData<String> registerMessage = new MutableLiveData<>(); // Para avisar si hay error o éxito

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new PokeRepository(application);
    }

    public LiveData<Boolean> getLoginResult() { return loginResult; }
    public LiveData<String> getRegisterMessage() { return registerMessage; }

    // Lógica de LOGIN
    public void login(String username, String password) {
        repository.login(username, password, user -> {
            if (user != null) {
                loginResult.postValue(true);
            } else {
                loginResult.postValue(false);
            }
        });
    }

    // Lógica de REGISTRO
    public void register(String username, String password) {
        // Ejecutamos en segundo plano
        new Thread(() -> {
            // 1. Validar duplicados
            UserEntity existing = repository.getUserByNameSync(username); // Necesitas añadir este método al repo

            if (existing == null) {
                // 2. Si no existe, se crea
                UserEntity newUser = new UserEntity();
                newUser.setUsername(username);
                newUser.setPassword(password); // En una app real, esto iría cifrado
                repository.insertUser(newUser);
                registerMessage.postValue("¡Registro exitoso! Ahora inicia sesión.");
            } else {
                registerMessage.postValue("Error: El usuario '" + username + "' ya existe.");
            }
        }).start();
    }
}