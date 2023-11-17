package com.example.censimenti;

public class Lampada {
    String locale, tipo, nome, potenza, sorgente, attacco, installazione, foto;
    float x, y, Kx, Ky;

    public Lampada() {
    }

    public Lampada(String locale, String tipo, String nome, String potenza, String sorgente, String attacco, String installazione, String foto, float x, float y, float kx, float ky) {
        this.locale = locale;
        this.tipo = tipo;
        this.nome = nome;
        this.potenza = potenza;
        this.sorgente = sorgente;
        this.attacco = attacco;
        this.installazione = installazione;
        this.foto = foto;
        this.x = x;
        this.y = y;
        Kx = kx;
        Ky = ky;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setInstallazione(String installazione) {
        this.installazione = installazione;
    }

    public String getNome() {
        return nome;
    }

    public String getInstallazione() {
        return installazione;
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
