package com.example.censimenti;

public class Planimetria {
    String nome;
    String imageUrl;

    String key;

    public Planimetria(String nome, String imageUrl, String key) {
        this.nome = nome;
        this.imageUrl = imageUrl;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNome() {
        return nome;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public  Planimetria (){}




}
