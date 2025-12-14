package iade.pt.backend.repositories;

import iade.pt.backend.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Produto p SET p.favoritosCount = p.favoritosCount + 1 WHERE p.idProduto = :produtoId")
    void incrementFavoritosCount(Long produtoId);

    @Modifying
    @Transactional
    @Query("UPDATE Produto p SET p.favoritosCount = p.favoritosCount - 1 WHERE p.idProduto = :produtoId AND p.favoritosCount > 0")
    void decrementFavoritosCount(Long produtoId);


    @Query("SELECT p FROM Produto p WHERE p.categoria.idCategoria = :categoriaId")
    List<Produto> findByCategoriaId(Long categoriaId);

    List<Produto> findByComprador_Id(Long compradorId);
    List<Produto> findByUsuario_Id(Long vendedorId);

    List<Produto> findByUsuario_IdAndStatus(Long idUsuario, String status);
    List<Produto> findByUsuario_IdAndCompradorIsNotNull(Long idUsuario);

}
