package iade.pt.backend.controllers;

import iade.pt.backend.models.Usuario;
import iade.pt.backend.repositories.UsuarioRepository;
import iade.pt.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Usuario loginData) {

        return usuarioRepository.findByEmail(loginData.getEmail())
                .map(usuario -> {
                    if (passwordEncoder.matches(loginData.getSenhaHash(), usuario.getSenhaHash())) {

                        String token = jwtUtil.generateToken(usuario);

                        Map<String, Object> response = new HashMap<>();
                        response.put("id", usuario.getId());
                        response.put("nome", usuario.getNome());
                        response.put("email", usuario.getEmail());
                        response.put("token", token);

                        return ResponseEntity.ok(response);

                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Senha incorreta!");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado!"));
    }
}
