package com.example.censimenti;

public class Edificio {
    String nome, indirizzo, key;

    public Edificio(){}

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

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

    public Edificio(String nome, String indirizzo, String key) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.key = key;
    }
}
