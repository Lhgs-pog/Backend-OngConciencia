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

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    private String conteudo;

    //Enviar emails somente de texto
    public String enviarEmailTexto(String destinatario, String assunto, String mensagem){
        try{
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

    //Enviar emails com estrutura html e estilização do css
    public String envirEmailCodigo(String destinatario, String assunto, String conteudo){
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            javaMailSender.send(mimeMessage);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao enviar email";
        }
    }
}
