package iade.pt.backend.controllers;

import iade.pt.backend.models.*;
import iade.pt.backend.repositories.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propostas")
@CrossOrigin(origins = "*")
public class PropostaController {

    private final PropostaRepository propostaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public PropostaController(
            PropostaRepository propostaRepository,
            ProdutoRepository produtoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.propostaRepository = propostaRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ===================== ENVIAR PROPOSTA =====================
    @PostMapping("/enviar")
    public Proposta enviarProposta(
            @RequestParam Long produtoId,
            @RequestParam Long compradorId,
            @RequestParam Double valor
    ) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Usuario comprador = usuarioRepository.findById(compradorId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Proposta p = new Proposta();
        p.setProduto(produto);
        p.setComprador(comprador);
        p.setValor(valor);
        p.setStatus(StatusProposta.PENDENTE);

        return propostaRepository.save(p);
    }

    // ===================== LISTAR COMO COMPRADOR =====================
    @GetMapping("/comprador/{id}")
    public List<Proposta> listarComoComprador(@PathVariable Long id) {
        return propostaRepository.findByCompradorId(id);
    }

    // ===================== LISTAR COMO VENDEDOR =====================
    @GetMapping("/vendedor/{id}")
    public List<Proposta> listarComoVendedor(@PathVariable Long id) {
        return propostaRepository.findByProdutoUsuarioId(id);
    }

    // ===================== ACEITAR PROPOSTA =====================
    @PutMapping("/{id}/aceitar")
    public Proposta aceitar(@PathVariable Long id) {

        Proposta p = propostaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada"));

        if (p.getStatus() != StatusProposta.PENDENTE &&
            p.getStatus() != StatusProposta.CONTRAOFERTA) {
            throw new RuntimeException("Proposta já tratada");
        }

        Produto produto = p.getProduto();

        if ("VENDIDO".equals(produto.getStatus())) {
            throw new RuntimeException("Produto já vendido");
        }

        // ✅ Aceita esta proposta
        p.setStatus(StatusProposta.ACEITA);
        propostaRepository.save(p);

        // ❌ Recusa todas as outras propostas do mesmo produto
        List<Proposta> outras =
                propostaRepository.findByProdutoIdProduto(produto.getIdProduto());

        for (Proposta outra : outras) {
            if (!outra.getIdProposta().equals(p.getIdProposta())) {
                outra.setStatus(StatusProposta.RECUSADA);
            }
        }
        propostaRepository.saveAll(outras);

        // 🔒 Marca produto como vendido
        produto.setStatus("VENDIDO");
        produto.setComprador(p.getComprador());
        produto.setDataCompra(java.time.LocalDateTime.now());
        produtoRepository.save(produto);

        return p;
    }

    // ===================== RECUSAR PROPOSTA =====================
    @PutMapping("/{id}/recusar")
    public Proposta recusar(@PathVariable Long id) {
        Proposta p = propostaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada"));

        p.setStatus(StatusProposta.RECUSADA);
        return propostaRepository.save(p);
    }

    // ===================== CONTRAPROPOSTA =====================
    @PutMapping("/{id}/contrapropor")
    public Proposta contrapropor(
            @PathVariable Long id,
            @RequestParam Double valor
    ) {
        Proposta p = propostaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposta não encontrada"));

        p.setStatus(StatusProposta.CONTRAOFERTA);
        p.setValor(valor);
        return propostaRepository.save(p);
    }
}
