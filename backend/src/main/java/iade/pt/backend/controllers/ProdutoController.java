package iade.pt.backend.controllers;

import iade.pt.backend.models.Categoria;
import iade.pt.backend.models.Produto;
import iade.pt.backend.models.Usuario;
import iade.pt.backend.repositories.ProdutoRepository;
import iade.pt.backend.repositories.UsuarioRepository;
import iade.pt.backend.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;



    // CRIAR PRODUTO

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> criarProduto(@RequestBody Produto produto) {

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(produto.getUsuario().getId());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }

            produto.setUsuario(usuarioOpt.get());
            produtoRepository.save(produto);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }


    // LISTAR PRODUTOS

    @GetMapping
    public ResponseEntity<?> listarProdutos() {

        List<Produto> produtos = produtoRepository.findAll();

        // Também carrega o usuário aqui
        for (Produto p : produtos) {
            Usuario u = p.getUsuario();
            if (u != null) {
                u.getId();
                u.getNome();
                u.getEmail();
                u.getFotoPerfil(); 
            }
        }

        return ResponseEntity.ok(produtos);
    }


    // DETALHE DO PRODUTO 

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduto(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario u = produto.getUsuario();
        if (u != null) {
            u.getId();
            u.getNome();
            u.getEmail();
            u.getFotoPerfil();
        }

        return ResponseEntity.ok(produto);
    }


    @GetMapping("/categoria/{id}")
    public List<Produto> produtosPorCategoria(@PathVariable Long id) {
        List<Produto> produtos = produtoRepository.findByCategoriaId(id);

        // Também carrega o usuário aqui
        for (Produto p : produtos) {
            Usuario u = p.getUsuario();
            if (u != null) {
                u.getId();
                u.getNome();
                u.getEmail();
                u.getFotoPerfil();
            }
        }

        return produtos;
    }

    //editar produto
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable Long id,
            @RequestBody Produto dados
    ) {
        try {
            Produto produto = produtoRepository.findById(id).orElse(null);
            if (produto == null) {
                return ResponseEntity.notFound().build();
            }

            //NÃO PERMITE substituir usuário
            dados.setUsuario(produto.getUsuario());

            // Atualiza os campos editáveis
            produto.setTitulo(dados.getTitulo());
            produto.setDescricao(dados.getDescricao());
            produto.setPreco(dados.getPreco());

            if (dados.getNome() != null) {
                produto.setNome(dados.getNome());
            }

            
             // ATUALIZAR CATEGORIA
            if (dados.getCategoria() != null && dados.getCategoria().getIdCategoria() != null) {
                Categoria categoria = categoriaRepository.findById(
                        dados.getCategoria().getIdCategoria()
                ).orElse(produto.getCategoria());

                produto.setCategoria(categoria);
            }


            if (dados.getImagens() != null) {
                produto.setImagens(dados.getImagens());
            }

            produtoRepository.save(produto);

            return ResponseEntity.ok(produto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Erro ao atualizar produto: " + e.getMessage());
        }
    }


    // eliminar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProduto(@PathVariable Long id) {
        try {
            if (!produtoRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            produtoRepository.deleteById(id);
            return ResponseEntity.ok("Produto eliminado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Erro ao eliminar produto: " + e.getMessage());
        }
    }

}
