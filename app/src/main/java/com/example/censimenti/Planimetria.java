package com.example.censimenti;

public class Planimetria {
    String nome;
    String imageUrl;



    public Planimetria(String nome, String imageUrl) {
        this.nome = nome;
        this.imageUrl = imageUrl;
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
