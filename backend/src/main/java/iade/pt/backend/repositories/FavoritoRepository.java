package iade.pt.backend.repositories;

import iade.pt.backend.models.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuario_Id(Long usuarioId);

    Optional<Favorito> findByUsuario_IdAndProduto_IdProduto(Long usuarioId, Long produtoId);
}
