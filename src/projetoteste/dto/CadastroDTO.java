package projetoteste.dto;

public class CadastroDTO {
    private Long id;
    private String nome;

    public CadastroDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}