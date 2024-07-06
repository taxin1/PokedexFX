package com.example.Lab08_1B_210041106;


public class Pokedex_Database {
    Integer ID;
    String Name, Type, Favourite;

    public Pokedex_Database(Integer ID, String name, String type, String queryFavourite) {
        this.ID = ID;
        Name = name;
        Type = type;
        Favourite = queryFavourite;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getFavourite() {
        return Favourite;
    }

    public void setFavourite(String queryFavourite) {
        Name = queryFavourite;
    }
}

