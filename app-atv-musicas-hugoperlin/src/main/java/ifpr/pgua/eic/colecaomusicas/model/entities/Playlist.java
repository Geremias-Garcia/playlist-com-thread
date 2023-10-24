package ifpr.pgua.eic.colecaomusicas.model.entities;

import java.util.ArrayList;

public class Playlist {
    private int id;
    private String nome;
    private ArrayList<Musica> musica;
    
    //método construtor para quando puxar
    //do banco de dados
    public Playlist(int id, String nome){
        this.id = id;
        this.nome = nome;
        musica = new ArrayList<>();
    }

    //método construtor para quando for
    //cadastrar um novo gênero
    public Playlist(String nome){
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Musica> getMusica(){
        return musica;
    }

    public void setMusica(ArrayList<Musica> musica){
        this.musica = musica;
    }

    public String toString(){
        return nome+" ("+id+")";
    }
}
