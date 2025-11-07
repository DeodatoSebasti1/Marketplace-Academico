package iade.pt.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    private String nome;
    private String email;
    @Column(name = "senha_hash")
    private String senhaHash;

    // Getter para id
    public Long getId() {
        return id;
    }

    // Setter para id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter para nome
    public String getNome() {
        return nome;
    }

    // Setter para nome
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter para email
    public String getEmail() {
        return email;
    }

    // Setter para email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter para senhaHash
    public String getSenhaHash() {
        return senhaHash;
    }

    // Setter para senhaHash
    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }
}