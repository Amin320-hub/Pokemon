package com.example.pokemon.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

// Definimos la tabla y la relacionamos con UserEntity (Si se borra el usuario, se borran sus pokemons)
@Entity(
        tableName = "pokemons",
        foreignKeys = @androidx.room.ForeignKey(
                entity = UserEntity.class,
                parentColumns = "username",
                childColumns = "ownerId",
               //en cascasda si se borra el usuario, se borran sus pokemons
                onDelete = androidx.room.ForeignKey.CASCADE
        ),
        indices = {@androidx.room.Index(value = "ownerId")}
)
public class PokemonEntity {

    @PrimaryKey(autoGenerate = true)
    private int id; // ID interno de la base de datos (1, 2, 3...)

    @ColumnInfo(name = "poke_api_id")
    private int pokeApiId; // ID real del pokemon (ej: Pikachu es 25)

    private String name;
    private int power;      // El 'base_stat' [cite: 6]
    private String imageUrl; // URL de la foto para cargarla luego

    @ColumnInfo(name = "ownerId")
    private String ownerId;

    // --- Constructor ---
    public PokemonEntity(int pokeApiId, String name, int power, String imageUrl, String ownerId) {
        this.pokeApiId = pokeApiId;
        this.name = name;
        this.power = power;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
    }

    // getter y seters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPokeApiId() { return pokeApiId; }
    public void setPokeApiId(int pokeApiId) { this.pokeApiId = pokeApiId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
}