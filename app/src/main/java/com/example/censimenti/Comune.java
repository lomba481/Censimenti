package com.example.censimenti;

public class Comune {
    String nome, nCommessa;

    //    Costruttore vuoto per Firebase
    public Comune() {}

    public Comune(String nome, String nCommessa) {
        this.nome = nome;
        this.nCommessa = nCommessa;
    }

    public String getNome() {
        return nome;
    }

    public String getnCommessa() {
        return nCommessa;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setnCommessa(String nCommessa) {
        this.nCommessa = nCommessa;
    }
}
