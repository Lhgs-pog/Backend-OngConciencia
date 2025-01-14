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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public List<UsuarioResponseDto> findAllUsuarios(){
        return repository.findAll().stream()
                .map(UsuarioResponseDto::new)
                .toList();
    }

    public Optional<Usuario> findUsuarioById(BigInteger id){
        return repository.findById(id);
    }

    @Transactional
    public ResponseEntity<Usuario> saveUsuario(UsuarioRequestDto data){
        Usuario usuario = new Usuario(data);

        repository.save(usuario);

        return ResponseEntity.ok(usuario);
    }

    @Transactional
    public ResponseEntity<Usuario> updateUsuario(BigInteger id,UsuarioRequestDto usuarioNovo){
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        Optional<Usuario> usuarioComMesmoEmail = repository.findOptionalByEmail(usuarioNovo.email());
        if (usuarioComMesmoEmail.isPresent() && usuarioComMesmoEmail.get().getEmail().equals(usuarioNovo.email())){
            throw new DataIntegrityViolationException("Já existe um usuário com este email");
        }

        usuarioExistente.setEmail(usuarioNovo.nome());
        usuarioExistente.setEmail(usuarioNovo.email());
        usuarioExistente.setSenha(usuarioNovo.senha());

        repository.save(usuarioExistente);

        return ResponseEntity.ok(usuarioExistente);
    }

    @Transactional
    public ResponseEntity<Usuario> deleteByID(BigInteger id){

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

    //Gerador de código aleatório
    public int codigoVerificacao(){
        Random gerador = new Random();
        //Gera um código de até 6 digitos entre 000000 a 999999
        return gerador.nextInt(1000000);
    }

    public void enviarEmailVerificacao(String email){

        int codigo = codigoVerificacao();

        mail.enviarEmailTexto(
                email,
                "Verificação de email",
                "O email: "+email+"está sendo cadastrado no noso site Ong Conciência." +
                        "Aqui está o seu código de configuração: "+codigo +". Caso está acha não tenha sido feita por você," +
                        "apenas ignore está menssagem.\n Um agradecimento da equipe Ong Conciência."

        );


    }

    public boolean verificarCodigo(int codigo , int tentativa){
        if (codigo == tentativa)
            return true;
        return false;
    }
}
