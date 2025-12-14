package iade.pt.backend.repositories;

import iade.pt.backend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByTokenRecuperacao(String token);
    Optional<Usuario> findByTokenVerificacao(String token); 
}
