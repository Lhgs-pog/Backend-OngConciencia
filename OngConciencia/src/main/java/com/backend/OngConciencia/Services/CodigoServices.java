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
        //Gera um numero aleatorio de 000000 a 999999
        return gerador.nextInt(1000000);
    }

    //Chamar isso quando a o codigo for gerado
    public void salvarCodigo(String email) {
        int codigo = gerarCodigo();
        LocalDateTime now = LocalDateTime.now();
        Codigo cod = new Codigo(email, codigo, now);
        repository.save(cod);

        emailServices.envirEmailCodigo(
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
