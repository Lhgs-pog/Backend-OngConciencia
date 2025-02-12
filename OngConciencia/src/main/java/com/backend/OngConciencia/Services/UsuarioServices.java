package com.backend.OngConciencia.Services;

import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import com.backend.OngConciencia.Dto.UsuarioResponseDto;
import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Model.UsuarioRole;
import com.backend.OngConciencia.Repository.UsuarioRepository;
import com.backend.OngConciencia.Security.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioServices {

    //Denpedencias
    @Autowired
    UsuarioRepository repository;

    @Autowired
    EmailServices mail;

    @Autowired
    CodigoServices codigoServices;

    @Autowired
    ImagemService imagemService;

    /*
    Retoma todos os usuários
    */
    public List<UsuarioResponseDto> findAllUsuarios(){
        return repository.findAll().stream()
                .map(UsuarioResponseDto::new)
                .toList();
    }

    /*
    Retorna um usuário especicífico pelo id dele
    * */
    public Optional<Usuario> findUsuarioById(String id){
        return repository.findById(id);
    }

    /*
    Retorna um usuário especicífico pelo Email dele
    * */
    public Optional<Usuario> findUsuarioByEmail(String email){
        return repository.findOptionalByEmail(email);
    }

    /*
    * Salva um novo usuário no banco de dados
    * */
    @Transactional
    public ResponseEntity saveUsuario(UsuarioRequestDto data, int tentativa){
        //Verifica se o email já existe
        if (repository.findByEmail(data.email()) != null)
            return ResponseEntity.badRequest().body("Este email de usuário já existe");

        //converte o objeto UsuarioRequestDto para um objeto usuário
        Usuario usuario = new Usuario(data);

        //Defini o cargo como usuário
        usuario.setRole(UsuarioRole.USER);

        //Encriptografa a senha do usuário
        String senhaCriptografda = new BCryptPasswordEncoder().encode(data.senha());
        usuario.setSenha(senhaCriptografda);

        //Verifica se o código gerado é o mesmo informado
        if (codigoServices.verificarCodigo(data.email(),tentativa)) {
            repository.save(usuario);
            return ResponseEntity.ok("Usuario salvo com sucesso");
        }
        return ResponseEntity.badRequest().body("Código expirado ou tentativa inválida. Faça uma nova tentaiva de criar uma conta para tentar novamente");
    }

    /*
    * Atualizar dados do usuário
    * */
    @Transactional
    public ResponseEntity<Usuario> updateUsuario(String id,UsuarioRequestDto usuarioNovo, MultipartFile foto){
        //Verifica se o usuário existe
        Usuario usuarioExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        //Verifica se o email novo já existe
        Optional<Usuario> usuarioComMesmoEmail = repository.findOptionalByEmail(usuarioNovo.email());
        if (usuarioComMesmoEmail.isPresent() && usuarioComMesmoEmail.get().getEmail().equals(usuarioNovo.email())){
            throw new DataIntegrityViolationException("Já existe um usuário com este email");
        }

        //Atualiza os atributos
        usuarioExistente.setEmail(usuarioNovo.nome());
        usuarioExistente.setEmail(usuarioNovo.email());

        //Encryotografada
        String senhaCriptografda = new BCryptPasswordEncoder().encode(usuarioNovo.senha());
        usuarioExistente.setSenha(senhaCriptografda);

        //Caso não tenha foto nova ele não muda
        if (!(foto.equals(null) || foto == null))
            usuarioExistente.setFoto(imagemService.tratarImagem(foto));

        repository.save(usuarioExistente);

        return ResponseEntity.ok(usuarioExistente);
    }

    /*
    * Atualizar somente a foto
    * */
    @Transactional
    public ResponseEntity<String> updateFoto(String email, MultipartFile foto){
        //Verifica se o usuário existe
        Usuario usuario = repository.findOptionalByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Não existe um usuário com este email"));

        try {
            //Verifica se existe uma imagem
            if (foto.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A imagem está vazia");
            }

            //Retorna uma imagem de 300 x 300 com 70% da qualidade
            usuario.setFoto(imagemService.tratarImagem(foto));
            repository.save(usuario);

            return ResponseEntity.ok("Imagem salva com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar imagem");
        }
    }

    /*
    * Deleta um usuário específico
    * */
    @Transactional
    public ResponseEntity<Usuario> deleteByID(String id){

        //Verifica se o usuário existe
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        repository.deleteById(id);

        return ResponseEntity.ok(usuario);
    }

    /*
    * Deleta todos os usuários no banco de dados
    * */
    @Transactional
    public ResponseEntity deleteAll(){
        repository.deleteAll();
        return ResponseEntity.ok("Usuarios deletados com sucesso");
    }

    /*
    * Envia o código de verificação por email
    * */
    public void enviarEmailVerificacao(String email){
        codigoServices.salvarCodigo(email);
    }
}
