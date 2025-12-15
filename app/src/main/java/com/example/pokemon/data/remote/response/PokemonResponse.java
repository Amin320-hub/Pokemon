package com.example.pokemon.data.remote.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PokemonResponse {
    private int id;
    private String name;
    private List<StatSlot> stats;
    private Sprites sprites;
    private List<TypeSlot> types;

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public List<StatSlot> getStats() { return stats; }
    public Sprites getSprites() { return sprites; }
    public List<TypeSlot> getTypes() { return types; }



    public static class StatSlot {
        @SerializedName("base_stat")
        private int baseStat;
        public int getBaseStat() { return baseStat; }
    }

    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault;
        public String getFrontDefault() { return frontDefault; }
    }


    public static class TypeSlot {
        private TypeObj type;
        public TypeObj getType() { return type; }
    }

    public static class TypeObj {
        private String name;
        public String getName() { return name; }
    }
}