package com.backend.OngConciencia.Services;

import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import com.backend.OngConciencia.Dto.UsuarioResponseDto;
import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Repository.UsuarioRepository;
import com.backend.OngConciencia.Security.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioServices {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    EmailServices mail;

    @Autowired
    CodigoServices codigoServices;

    @Autowired
    ImagemService imagemService;

    public List<UsuarioResponseDto> findAllUsuarios(){
        return repository.findAll().stream()
                .map(UsuarioResponseDto::new)
                .toList();
    }

    public Optional<Usuario> findUsuarioById(String id){
        return repository.findById(id);
    }

    @Transactional
    public ResponseEntity saveUsuario(UsuarioRequestDto data, int tentativa){
        Usuario usuario = new Usuario(data);

        if (codigoServices.verificarCodigo(data.email(),tentativa)) {
            repository.save(usuario);
            return ResponseEntity.ok("Usuario salvo com sucesso");
        }
        return ResponseEntity.ok("Código expirado ou tentativa inválida. Faça uma nova tentaiva de criar uma conta para tentar novamente");
    }

    @Transactional
    public ResponseEntity<Usuario> updateUsuario(String id,UsuarioRequestDto usuarioNovo, MultipartFile foto){
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        Optional<Usuario> usuarioComMesmoEmail = repository.findOptionalByEmail(usuarioNovo.email());
        if (usuarioComMesmoEmail.isPresent() && usuarioComMesmoEmail.get().getEmail().equals(usuarioNovo.email())){
            throw new DataIntegrityViolationException("Já existe um usuário com este email");
        }

        usuarioExistente.setEmail(usuarioNovo.nome());
        usuarioExistente.setEmail(usuarioNovo.email());
        usuarioExistente.setSenha(usuarioNovo.senha());
        usuarioExistente.setFoto(imagemService.tratarImagem(foto));

        repository.save(usuarioExistente);

        return ResponseEntity.ok(usuarioExistente);
    }

    @Transactional
    public ResponseEntity<String> updateFoto(String email, MultipartFile foto){
        Usuario usuario = repository.findOptionalByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Não existe um usuário com este email"));

        try {
            if (foto.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A imagem está vazia");
            }

            usuario.setFoto(imagemService.tratarImagem(foto));
            repository.save(usuario);

            return ResponseEntity.ok("Imagem salva com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar imagem");
        }
    }

    @Transactional
    public ResponseEntity<Usuario> deleteByID(String id){

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        repository.deleteById(id);

        return ResponseEntity.ok(usuario);
    }

    @Transactional
    public ResponseEntity deleteAll(){
        repository.deleteAll();
        return ResponseEntity.ok("Usuarios deletados com sucesso");
    }

    public void enviarEmailVerificacao(String email){
        codigoServices.salvarCodigo(email);
    }
}
