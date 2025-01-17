package com.backend.OngConciencia.Services;

import com.backend.OngConciencia.Model.Codigo;
import com.backend.OngConciencia.Repository.CodigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class CodigoServices {

    @Autowired
    CodigoRepository repository;

    @Autowired
    EmailServices emailServices;

    public int gerarCodigo(){
        Random gerador = new Random();
        return gerador.nextInt(1000000);
    }

    //Chamar isso quando a o codigo for gerado
    public void salvarCodigo(String email){
        int codigo = gerarCodigo();
        LocalDateTime now = LocalDateTime.now();
        Codigo cod = new Codigo(email,codigo,now);
        repository.save(cod);

        emailServices.enviarEmailTexto(email,
                "Código de verificação",

                "O seu código de verificação para criação de usuário é "+codigo
        + " , caso não seja você que esteja criando uma conta na nossa plataforma Ong Conciência, apenas ignore este email e não compartilhe esse código.\n" +
                        "Atenciosamente, equipe Ong Conciência.");
    }

    public boolean verificarCodigo(String email, int tentativa){
        Codigo codigo = repository.findByEmail(email);
        Duration duration = Duration.between(codigo.getDia(), LocalDateTime.now());

        deleteCodigo(codigo);
        if (codigo.getCodigo() == tentativa && duration.toHours() >= 24)
            return true;
        return false;
    }

    public void deleteCodigo(Codigo codigo){
        repository.deleteById(codigo.getId());
    }
}
