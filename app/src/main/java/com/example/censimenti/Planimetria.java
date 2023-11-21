package com.example.censimenti;

public class Planimetria {
    String nome;
    String imageUrl;
    Long conteggio;


    public Planimetria(String nome, String imageUrl, Long conteggio) {
        this.nome = nome;
        this.imageUrl = imageUrl;
        this.conteggio = conteggio;
    }

    public void setConteggio(Long conteggio) {
        this.conteggio = conteggio;
    }

    public Long getConteggio() {
        return conteggio;
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
