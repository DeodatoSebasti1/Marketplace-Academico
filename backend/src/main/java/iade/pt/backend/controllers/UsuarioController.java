package iade.pt.backend.controllers;

import iade.pt.backend.models.Usuario;
import iade.pt.backend.repositories.UsuarioRepository;
import iade.pt.backend.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.Random;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;   

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtService; 

    // CADASTRO
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {

        // validacao telefone
        if (usuario.getTelefone() == null || usuario.getTelefone().length() != 9) {
            return ResponseEntity.badRequest().body("Telefone inválido! Deve ter 9 dígitos.");
        }

        // validacao email repetido
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já registado!");
        }

        usuario.setSenhaHash(passwordEncoder.encode(usuario.getSenhaHash()));
        Usuario novo = usuarioRepository.save(usuario);

        return ResponseEntity.ok(novo);
    }

    // RECUPERAÇÃO DE SENHA
    @PostMapping("/recuperar")
    public ResponseEntity<?> solicitarRecuperacao(@RequestParam String email) {
        return usuarioRepository.findByEmail(email)
                .map(user -> {
                    String token = String.format("%06d", new java.util.Random().nextInt(999999));
                    user.setTokenRecuperacao(token);
                    usuarioRepository.save(user);

                    // Enviar email via Brevo SMTP
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("mercadoacademico@outlook.pt");  // AQUI!!!
                    message.setTo(email);
                    message.setSubject("Recuperação de Senha - Marketplace Académico");
                    message.setText("🔐 Seu código para redefinir a senha é: " + token +
                            "\n\nUse este código na app para redefinir sua senha."  +
                            "\n\\n" + //
                            "Equipe Mercado Acadêmico");
                    mailSender.send(message);

                    return ResponseEntity.ok("Codigo enviado para e-mail!");
                })
                .orElse(ResponseEntity.badRequest().body("Email não encontrado!"));
    }

    // NOVA SENHA
    @PostMapping("/reset")
    public ResponseEntity<?> resetarSenha(
            @RequestParam String token,
            @RequestParam String novaSenha
    ) {
        return usuarioRepository.findByTokenRecuperacao(token)
                .map(user -> {

                    // VERIFICA SE A SENHA É IGUAL À ANTIGA
                    if (passwordEncoder.matches(novaSenha, user.getSenhaHash())) {
                        return ResponseEntity.badRequest().body("A nova senha deve ser diferente da atual!");
                    }

                    user.setSenhaHash(passwordEncoder.encode(novaSenha));
                    user.setTokenRecuperacao(null);
                    usuarioRepository.save(user);

                    return ResponseEntity.ok("Senha atualizada com sucesso!");
                })
                .orElse(ResponseEntity.badRequest().body("Token inválido!"));
    }


    //PERFIL
    @GetMapping("/perfil")
    public ResponseEntity<?> getMeuPerfil(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String emailUsuario = jwtService.extractUsername(jwt);

        Usuario user = usuarioRepository.findByEmail(emailUsuario).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        return ResponseEntity.ok(user);
    }
    
    
    // EDITAR PERFIL
    @PutMapping("/editar")
    public ResponseEntity<?> editarPerfil(
            HttpServletRequest request,
            @RequestBody Usuario dados
    ) {
        try {
            Long userId = Long.valueOf(request.getAttribute("userId").toString());
            Usuario usuario = usuarioRepository.findById(userId).orElseThrow();

            usuario.setNome(dados.getNome());
            usuario.setTelefone(dados.getTelefone());

            if (dados.getFotoPerfil() != null && !dados.getFotoPerfil().isBlank()) {
                usuario.setFotoPerfil(dados.getFotoPerfil());
            }

            usuarioRepository.save(usuario);

            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar perfil");
        }
    }



    //UPLOAD FOTO
    @PostMapping("/uploadFoto")
    public ResponseEntity<?> uploadFoto(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Long userId = Long.valueOf(request.getAttribute("userId").toString());

            File directory = new File("/Users/deodatoluzayadio/academicplace/backend/uploads/");
            if (!directory.exists()) directory.mkdirs();

            String fileName = "perfil_" + userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = directory.getAbsolutePath() + "/" + fileName;

            file.transferTo(new File(filePath));

            String imageUrl = "http://localhost:8080/uploads/" + fileName;

            Usuario usuario = usuarioRepository.findById(userId).orElseThrow();
            usuario.setFotoPerfil(imageUrl);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao enviar foto: " + e.getMessage());
        }
    }


  @PostMapping("/verificar-email")
    public ResponseEntity<?> verificarEmail(@RequestParam String email) {

        Usuario usuarioTemp = usuarioRepository.findByEmail(email).orElse(null);

        if (usuarioTemp != null && usuarioTemp.isVerificado()) {
            return ResponseEntity.badRequest().body("Email já está registado e verificado.");
        }

        String token = String.format("%06d", new Random().nextInt(999999));

        if (usuarioTemp == null) {
            usuarioTemp = new Usuario();
            usuarioTemp.setEmail(email);
        }

        usuarioTemp.setTokenVerificacao(token);
        usuarioTemp.setVerificado(false);
        usuarioRepository.save(usuarioTemp);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("mercadoacademico@outlook.pt");
        msg.setTo(email);
        msg.setSubject("🔐 Código de Verificação - Marketplace Académico");
        msg.setText(
            "Olá,\n\n" +
            "Use o código abaixo para confirmar o seu e-mail e continuar com o cadastro:\n\n" +
            "CÓDIGO: " + token + "\n\n" +
            "Se não pediu este código, basta ignorar esta mensagem.\n\n" +
            "Atenciosamente,\n" +
            "Equipe Mercado Académico"
        );
        mailSender.send(msg);

        return ResponseEntity.ok("Código enviado ao email");
    }

    @PostMapping("/validar-email")
    public ResponseEntity<?> validarEmail(@RequestParam String token) {

        return usuarioRepository.findByTokenVerificacao(token)
                .map(user -> {
                    user.setVerificado(true);
                    usuarioRepository.save(user);
                    return ResponseEntity.ok("Email verificado!");
                })
                .orElse(ResponseEntity.badRequest().body("Token inválido!"));
    }


    @PostMapping("/criar")
    public ResponseEntity<?> criarConta(
            @RequestParam String nome,
            @RequestParam String telefone,
            @RequestParam String senha,
            @RequestParam String email
    ) {
        return usuarioRepository.findByEmail(email)
                .map(user -> {
                    if (!user.isVerificado()) {
                        return ResponseEntity.badRequest().body("Email não verificado!");
                    }

                    user.setNome(nome);
                    user.setTelefone(telefone);
                    user.setSenhaHash(passwordEncoder.encode(senha));
                    user.setTokenVerificacao(null);

                    usuarioRepository.save(user);

                    return ResponseEntity.ok("Conta criada com sucesso!");
                })
                .orElse(ResponseEntity.badRequest().body("Erro ao criar conta"));
    }

    


}
