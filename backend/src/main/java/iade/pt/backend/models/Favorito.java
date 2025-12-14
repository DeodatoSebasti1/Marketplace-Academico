package iade.pt.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "favoritos")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    public Favorito() {}

    public Favorito(Usuario usuario, Produto produto) {
        this.usuario = usuario;
        this.produto = produto;
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Produto getProduto() { return produto; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setProduto(Produto produto) { this.produto = produto; }
}
