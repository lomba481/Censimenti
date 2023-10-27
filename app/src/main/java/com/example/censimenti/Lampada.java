package com.example.censimenti;

public class Lampada {
    String nome, descrizione, foto, key;
    float x, y;
    public Lampada () {}

    public Lampada(String nome, String descrizione, String foto, String key, int x, int y) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.foto = foto;
        this.key = key;
        this.x = x;
        this.y = y;
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

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getFoto() {
        return foto;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
