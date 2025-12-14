package iade.pt.backend.repositories;

import iade.pt.backend.models.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByCompradorId(Long compradorId);

    List<Proposta> findByProdutoUsuarioId(Long vendedorId);

    List<Proposta> findByProdutoIdProduto(Long produtoId);
}
