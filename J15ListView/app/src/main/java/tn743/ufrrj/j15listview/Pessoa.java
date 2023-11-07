package tn743.ufrrj.j15listview;

public class Pessoa {
    private String nome;
    private String idade;
    private int fotoId;

    Pessoa(String nome, String idade, int fotoId) {
        this.setNome(nome);
        this.setIdade(idade);
        this.setFotoId(fotoId);
    }

    public String getNome()                 {return nome;}
    public void setNome(String nome)        {this.nome = nome;}
    public String getIdade()                {return idade;}
    public void setIdade(String idade)      {this.idade = idade;}
    public int getFotoId()                  {return fotoId;}
    public void setFotoId(int fotoId)       {this.fotoId = fotoId;}
    // Implemente os Getters e Setters
}
