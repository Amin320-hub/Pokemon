package com.example.pokemon.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AppDao {

    // --- USUARIOS ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :user AND password = :pass")
    UserEntity login(String user, String pass);

    // Comprobar si esta duplicado
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity getUserByName(String username);

    //Borrar usuario
    @Query("DELETE FROM users WHERE username = :username")
    void deleteUser(String username);


    // --- POKEMONS ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPokemon(PokemonEntity pokemon);

    @Query("SELECT * FROM pokemons WHERE ownerId = :userId")
    LiveData<List<PokemonEntity>> getUserPokemons(String userId);



    // Borrar pokemon del usuaorio en cascada
    @Query("DELETE FROM pokemons WHERE ownerId = :username")
    void deleteUserPokemons(String username);
}