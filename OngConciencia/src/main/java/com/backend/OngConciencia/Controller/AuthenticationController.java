package com.backend.OngConciencia.Controller;

import com.backend.OngConciencia.Dto.AuthenticationDto;
import com.backend.OngConciencia.Dto.LoginResponseDto;
import com.backend.OngConciencia.Dto.RegisterDto;
import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Model.UsuarioRole;
import com.backend.OngConciencia.Repository.UsuarioRepository;
import com.backend.OngConciencia.Services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    //Dependencias
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    /*
    * Função para fazer o login do usuário
    * */
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());//Verifica os dados do usuário
        var auth = this.authenticationManager.authenticate(usernamePassword);//Verifica a authenticação

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());//Gerá um token com os dados do usuário
        return ResponseEntity.ok(new LoginResponseDto(token));//Status da resposta
    }

    /*
    * Não usaremos essa função
    * */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDto data) {
        if (this.repository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        // Define o papel do usuário sempre como UsuarioRole.USER
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), encryptedPassword, UsuarioRole.USER);

        this.repository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }
}
