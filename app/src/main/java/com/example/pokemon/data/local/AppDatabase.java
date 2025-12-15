package com.example.pokemon.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Aquí se declara las tablas (entities) y la versión de la BD
@Database(entities = {UserEntity.class, PokemonEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract AppDao appDao();

    //  Para que haya solo una instancia de la BD
    private static volatile AppDatabase INSTANCE;

    // Executor para realizar operaciones de base de datos en segundo plano (hilos)
    // Número de hilos fijo (4) para manejar operaciones concurrentes si fuera necesario
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "pokedam_database") // Nombre del archivo de la BD
                            .fallbackToDestructiveMigration() // Si cambias la BD, borra la anterior para no fallar
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}