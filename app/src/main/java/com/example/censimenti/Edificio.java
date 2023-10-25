package com.example.censimenti;

public class Edificio {
    String nome, indirizzo;

    public Edificio(){}
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getNome() {
        return nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public Edificio(String nome, String indirizzo) {
        this.nome = nome;
        this.indirizzo = indirizzo;
    }
}
