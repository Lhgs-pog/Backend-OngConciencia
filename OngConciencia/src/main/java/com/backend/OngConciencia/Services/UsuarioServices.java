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

    @Autowired
    CodigoServices codigoServices;

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
    public ResponseEntity<Usuario> updateUsuario(String id,UsuarioRequestDto usuarioNovo){
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

    //Gerador de código aleatório
    public int codigoVerificacao(){
        Random gerador = new Random();
        //Gera um código de até 6 digitos entre 000000 a 999999
        return gerador.nextInt(1000000);
    }

    public void enviarEmailVerificacao(String email){

        int codigo = codigoVerificacao();

        mail.envirEmailCodigo(
                email,
                "Verificação de email",
                String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                body {
                                    font-family: Arial, sans-serif;
                                    background-color: white;
                                    background-image: linear-gradient( 180deg, #90aeff, #60efb8, #cefc86);
                                    color: black;
                                    padding: 0px;
                                }
                                .email-container {
                                    background-color: #f9f7f7;
                                    border-radius: 10px;
                                    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                                    padding: 20px;
                                    text-align: center;
                                    width: 60vw;
                                    margin: auto;
                                    color: #000;
                                    margin-top: 30px;
                                    margin-bottom: 50px;
                                }
                                #navbar{
                                    background-color: rgba(0, 0, 0, 0);
                                    display: block;
                                    width: 100vw;
                                    text-align: center;
                                    padding-top: 10px;
                                    padding-bottom: 10px;
                                }
                        
                                .email-container h1{
                                    color: red;
                                }
                        
                            </style>
                        </head>
                        <body>
                            <nav id="navbar">
                                <h1>Ong Conciência</h1>
                            </nav>
                            <div class="email-container">
                                <h4>Verificação de email</h4>
                                <p>
                                    Prezado usuário,<br>
                        
                                    O e-mail %s está sendo cadastrado em nosso site <b>Ong Consciência</b>. <br>
                                    Aqui está o seu código de verificação:
                                    <br>
                                    <h1>%d.</h1>
                                    <br>
                        
                                    Se você não realizou essa solicitação, por favor, ignore esta mensagem e não compartilhe este código.
                        
                                    Agradecemos sua atenção.
                                    Atenciosamente,<br>
                                    <b>Equipe Ong Consciência</b>
                                </p>
                            </div>
                        </body>
                        </html>""", email, codigo)

        );


    }

    public boolean verificarCodigo(int codigo , int tentativa){
        if (codigo == tentativa)
            return true;
        return false;
    }
}
