package com.example.censimenti;

import com.google.firebase.database.annotations.NotNull;

public class Lampada {
    String tipo, sorgente, potenza, attacco, foto, key;
    float x, y;

    public Lampada() {
    }

    public Lampada(String tipo, String sorgente, String potenza, String attacco, @NotNull String foto, String key, float x, float y) {
        this.tipo = tipo;
        this.sorgente = sorgente;
        this.potenza = potenza;
        this.attacco = attacco;
        this.foto = foto;
        this.key = key;
        this.x = x;
        this.y = y;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSorgente() {
        return sorgente;
    }

    public void setSorgente(String sorgente) {
        this.sorgente = sorgente;
    }

    public String getPotenza() {
        return potenza;
    }

    public void setPotenza(String potenza) {
        this.potenza = potenza;
    }

    public String getAttacco() {
        return attacco;
    }

    public void setAttacco(String attacco) {
        this.attacco = attacco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
