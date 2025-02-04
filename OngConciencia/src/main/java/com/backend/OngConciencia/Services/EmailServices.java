package com.backend.OngConciencia.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServices {

    //Denpendencias
    @Autowired
    JavaMailSender javaMailSender;

    //Recupera o email no applications properties
    @Value("${spring.mail.username}")
    private String remetente;

    /*
    * Envia emails em formato de texto
    * */
    public String enviarEmailTexto(String destinatario, String assunto, String mensagem){
        try{
            //Configuração
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(remetente);
            simpleMailMessage.setTo(destinatario);
            simpleMailMessage.setSubject(assunto);
            simpleMailMessage.setText(mensagem);
            javaMailSender.send(simpleMailMessage);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao enviar email" + e.getLocalizedMessage();
        }
    }

    /*
    * Envia emails em formato html
    * */
    public String envirEmailCodigo(String destinatario, String assunto, String conteudo){
        try {
            //Criação do objeto
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            //Configuração
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);//Indica que ele é html

            //Envia
            javaMailSender.send(mimeMessage);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao enviar email" + e.getMessage();
        }
    }
}
