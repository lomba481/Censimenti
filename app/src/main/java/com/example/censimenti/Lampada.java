package com.example.censimenti;

public class Lampada {
    String tipo, sorgente, potenza, attacco, foto;
    float x, y, Kx, Ky;

    public Lampada() {
    }

    public Lampada(String tipo, String sorgente, String potenza, String attacco, String foto, float x, float y, float Kx, float Ky) {
        this.tipo = tipo;
        this.sorgente = sorgente;
        this.potenza = potenza;
        this.attacco = attacco;
        this.foto = foto;
        this.x = x;
        this.y = y;
        this.Kx = Kx;
        this.Ky = Ky;
    }

    public void setKx(float kx) {
        Kx = kx;
    }

    public void setKy(float ky) {
        Ky = ky;
    }

    public float getKx() {
        return Kx;
    }

    public float getKy() {
        return Ky;
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
