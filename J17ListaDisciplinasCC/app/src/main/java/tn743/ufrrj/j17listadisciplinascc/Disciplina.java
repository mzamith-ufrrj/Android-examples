package tn743.ufrrj.j17listadisciplinascc;

public class Disciplina {
    private String mCodigo;
    private String mNome;
    private String mArea;
    private int mPeriodo;

    public Disciplina(String codigo,
                      String nome,
                      String area,
                      int periodo){
        this.setCodigo(new String(codigo));
        this.setNome(new String(nome));
        this.setArea(new String(area));
        this.setPeriodo(periodo);
    }

    public String getCodigo() {
        return mCodigo;
    }

    public void setCodigo(String mCodigo) {
        this.mCodigo = mCodigo;
    }

    public String getNome() {
        return mNome;
    }

    public void setNome(String mNome) {
        this.mNome = mNome;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String mArea) {
        this.mArea = mArea;
    }

    public int getPeriodo() {
        return mPeriodo;
    }

    public void setPeriodo(int mPeriodo) {
        this.mPeriodo = mPeriodo;
    }
}
