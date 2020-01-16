package com.example.cachorros.Classes;

public class Cachorro {
    private String nome;
    private String raca;
    private String sub_raca;
    private String img;

    public Cachorro(String nome, String raca, String sub_raca, String img) {
        this.nome = nome;
        this.raca = raca;
        this.sub_raca = sub_raca;
        this.img = img;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getSub_raca() {
        return sub_raca;
    }

    public void setSub_raca(String sub_raca) {
        this.sub_raca = sub_raca;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
