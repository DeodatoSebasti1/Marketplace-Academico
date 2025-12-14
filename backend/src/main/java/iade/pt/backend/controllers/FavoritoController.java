package iade.pt.backend.controllers;

import iade.pt.backend.models.Favorito;
import iade.pt.backend.models.Produto;
import iade.pt.backend.models.Usuario;
import iade.pt.backend.repositories.FavoritoRepository;
import iade.pt.backend.repositories.ProdutoRepository;
import iade.pt.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping("/{userId}/{produtoId}")
    @Transactional
    public ResponseEntity<?> adicionarFavorito(@PathVariable Long userId, @PathVariable Long produtoId) {

        Usuario user = usuarioRepository.findById(userId).orElse(null);
        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if (user == null || produto == null) {
            return ResponseEntity.badRequest().body("Usuário ou produto inválido");
        }

        if (favoritoRepository.findByUsuario_IdAndProduto_IdProduto(userId, produtoId).isPresent()) {
            return ResponseEntity.ok("Já é favorito");
        }

        Favorito fav = new Favorito(user, produto);
        favoritoRepository.save(fav);

        // Incrementa favoritos_count
        produtoRepository.incrementFavoritosCount(produtoId);

        return ResponseEntity.ok("Favorito adicionado");
    }

    @DeleteMapping("/{userId}/{produtoId}")
    @Transactional
    public ResponseEntity<?> removerFavorito(@PathVariable Long userId, @PathVariable Long produtoId) {

        Favorito favorito = favoritoRepository
                .findByUsuario_IdAndProduto_IdProduto(userId, produtoId)
                .orElse(null);

        if (favorito == null) {
            return ResponseEntity.badRequest().body("Não existe favorito");
        }

        favoritoRepository.delete(favorito);

        // Decrementa favoritos_count
        produtoRepository.decrementFavoritosCount(produtoId);

        return ResponseEntity.ok("Favorito removido");
    }
}
