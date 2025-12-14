package iade.pt.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "produtos")
public class Produto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario; // Vendedor

    @ManyToOne
    @JoinColumn(name = "id_comprador")
    private Usuario comprador; // Comprador
    private String status;
    private LocalDateTime dataCompra;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

  


    private String nome;
    private String titulo;
    private String descricao;
    private Double preco;

    private Integer favoritosCount = 0;

    @Column(length = 500)
    private String imagens;

    private LocalDateTime criadoEm = LocalDateTime.now();

    // GETTERS E SETTERS

    public Long getIdProduto() { return idProduto; }
    public void setIdProduto(Long idProduto) { this.idProduto = idProduto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Usuario getComprador() { return comprador; }
    public void setComprador(Usuario comprador) { this.comprador = comprador; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Integer getFavoritosCount() { return favoritosCount; }
    public void setFavoritosCount(Integer favoritosCount) { this.favoritosCount = favoritosCount; }

    public String getImagens() { return imagens; }
    public void setImagens(String imagens) { this.imagens = imagens; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public String getStatus() { 
    return status; 
    }

    public void setStatus(String status) { 
        this.status = status; 
    }

    public LocalDateTime getDataCompra() { 
        return dataCompra; 
    }

    public void setDataCompra(LocalDateTime dataCompra) { 
        this.dataCompra = dataCompra; 
    }

}
