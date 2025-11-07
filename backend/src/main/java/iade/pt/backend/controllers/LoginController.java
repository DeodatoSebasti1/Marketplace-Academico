package iade.pt.backend.controllers;

import iade.pt.backend.models.Usuario;
import iade.pt.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public Usuario login(@RequestBody Usuario loginData) {
        return usuarioRepository.findByEmailAndSenhaHash(
            loginData.getEmail(), loginData.getSenhaHash()
        ).orElse(null);
    }
}