package com.backend.OngConciencia.Controller;

import com.backend.OngConciencia.Dto.PasswordResponseDto;
import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import com.backend.OngConciencia.Dto.UsuarioResponseDto;
import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    UsuarioServices services;

    /*
    * Retorna todos os usuários
    * */
    @GetMapping
    public List<UsuarioResponseDto> getAllUsuarios(){
        return services.findAllUsuarios();
    }

    /*
    * Retorna um usuário específico
    * */
    @GetMapping("/{id}")
    public Optional<Usuario> getUsuarioById(@PathVariable("id")String id){
        return services.findUsuarioById(id);
    }

    /*
     * Retorna um usuário específico pelo EMAIL
     * */
    @GetMapping("/email/{email}")
    public Optional<Usuario> getUsuarioByEmail(@PathVariable("email")String email){
        return services.findUsuarioByEmail(email);
    }
    /*
    * Cadastrar novo usuário
    * */
    @PostMapping("/{tentativa}")
    public ResponseEntity postUsuario(@PathVariable("tentativa") int tentativa ,@RequestBody UsuarioRequestDto data){
        return services.saveUsuario(data, tentativa);
    }

    /*
    * Gerar um código de verificação e envia-lo pelo email
    * */
    @PostMapping("/codigo")
    public void gerarCodigo(@RequestBody String email){
        services.enviarEmailVerificacao(email);
    }

    /*
    * Atualizar foto do usuário
    * */
    @PutMapping("/foto")
    public ResponseEntity<String> postFoto(@RequestParam("email") String email,@RequestParam("foto")MultipartFile foto){
        return services.updateFoto(email,foto);
    }

    /*
     * Atualizar senha do usuário
     * */
    @PutMapping("/senha")
    public ResponseEntity uptSenha(@RequestBody PasswordResponseDto passwordResponseDto){
        return services.updateSenha(passwordResponseDto.email(), passwordResponseDto.cod(), passwordResponseDto.senha());
    }

    /*
    * Atualizar dados do usuário
    * */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uptUsuario(
            @PathVariable("id") String id,
            @RequestPart("data") UsuarioRequestDto data,
            @RequestPart(value = "foto", required = false) MultipartFile foto) {
        return services.updateUsuario(id, data, foto);
    }

    /*
    * Deletar todos os usuários do banco de dados
    * */
    @DeleteMapping
    public ResponseEntity deleteAllUsuarios(){
        return services.deleteAll();
    }

    /*
    * Deleta um usuário específico
    * */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUsuarioById(@PathVariable("id") String id){
        return services.deleteByID(id);
    }
}
