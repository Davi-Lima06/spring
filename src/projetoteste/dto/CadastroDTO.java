package projetoteste.dto;

public class CadastroDTO {
    private String nome;
    public CadastroDTO(String nome) {
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}
