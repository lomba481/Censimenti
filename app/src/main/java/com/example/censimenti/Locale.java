package com.example.censimenti;

public class Locale {
    String nome, note, foto;
    float Kx, Ky;

    public Locale(String nome, String note, String foto, float kx, float ky) {
        this.nome = nome;
        this.note = note;
        this.foto = foto;
        Kx = kx;
        Ky = ky;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public String getNote() {
        return note;
    }

    public String getFoto() {
        return foto;
    }
}
