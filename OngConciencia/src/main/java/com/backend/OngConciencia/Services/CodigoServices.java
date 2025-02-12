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

    //Dependencias
    @Autowired
    CodigoRepository repository;

    @Autowired
    EmailServices emailServices;

    /*
    * Gera um código numerico de 6 digitos
    * */
    public int gerarCodigo(){
        Random gerador = new Random();
        //Gera um numero aleatorio de 000000 a 999999
        return gerador.nextInt(1000000);
    }

    //Chamar isso quando a o codigo for gerado
    /*
    * Função respoosnssável por gerar o código de verificação, ssalvando o no banco de dados para verificação futura
    * e envio do código por um email html para o usuário
    * */
    public void salvarCodigo(String email) {
        //Deleta um código anterior
        Codigo codigoa = repository.findByEmail(email);
        repository.deleteById(codigoa.getId());

        //Informações para salvamento do código
        int codigo = gerarCodigo();
        LocalDateTime now = LocalDateTime.now();
        Codigo cod = new Codigo(email, codigo, now);
        repository.save(cod);

        //Envio do email em formato html
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
                        </html>""", email, codigo)//Atributos inseridos na string de multiplas linhas

        );
    }

    /*
    * Verifica se o código salvo no banco de dados e o informado pelo o usuário são os mesmos
    * */
    public boolean verificarCodigo(String email, int tentativa){
        //Busca o código salvo no banco de dados
        Codigo codigo = repository.findByEmail(email);

        //Compara a diferença de tempo de quando o códico foi criado e agora
        Duration duration = Duration.between(codigo.getDia(), LocalDateTime.now());

        //Apaga o código do banco estando certo ou não
        deleteCodigo(codigo);

        //Valida se a tentativa e o código são os mesmos e se ele foi criado a mais de 24h
        if (codigo.getCodigo() == tentativa && duration.toHours() <= 24) {
            return true;
        }
        return false;
    }

    /*
    * Função responsável por deletar o código no banco de dados
    * */
    public void deleteCodigo(Codigo codigo){
        repository.deleteById(codigo.getId());
    }
}
