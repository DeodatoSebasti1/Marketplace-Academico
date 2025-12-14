package iade.pt.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import iade.pt.backend.models.Produto;
import iade.pt.backend.models.Usuario;
import iade.pt.backend.repositories.ProdutoRepository;
import iade.pt.backend.repositories.UsuarioRepository;
import iade.pt.backend.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private JwtUtil jwtService;

    // MINHAS COMPRAS
    @GetMapping
    public ResponseEntity<?> getMinhasCompras(@RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);

        Usuario user = usuarioRepository.findByEmail(email)
                .orElse(null);

        if (user == null)
            return ResponseEntity.badRequest().body("Usuário não encontrado");

        List<Produto> compras = produtoRepository.findByComprador_Id(user.getId());

        return ResponseEntity.ok(compras);
    }

    // MINHAS VENDAS
    @GetMapping("/vendas")
    public ResponseEntity<?> getMinhasVendas(@RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);

        Usuario user = usuarioRepository.findByEmail(email)
                .orElse(null);

        if (user == null)
            return ResponseEntity.badRequest().body("Usuário não encontrado");

        List<Produto> vendas = produtoRepository.findByUsuario_IdAndCompradorIsNotNull(user.getId());


        return ResponseEntity.ok(vendas);
    }

    // FINALIZAR COMPRA
    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarCompra(@RequestBody Map<String, Long> data) {

        Long idProduto = data.get("idProduto");
        Long idComprador = data.get("idComprador");

        if (idProduto == null || idComprador == null)
            return ResponseEntity.badRequest().body("Dados inválidos");

        Produto produto = produtoRepository.findById(idProduto)
                .orElse(null);

        if (produto == null)
            return ResponseEntity.badRequest().body("Produto não encontrado");

        Usuario comprador = usuarioRepository.findById(idComprador)
                .orElse(null);

        if (comprador == null)
            return ResponseEntity.badRequest().body("Usuário não encontrado");

        produto.setComprador(comprador);
        produto.setStatus("vendido");
        produto.setDataCompra(LocalDateTime.now());

        produtoRepository.save(produto);

        return ResponseEntity.ok("Compra registrada com sucesso!");
    }
}
