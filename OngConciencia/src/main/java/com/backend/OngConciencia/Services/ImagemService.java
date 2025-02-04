package com.backend.OngConciencia.Services;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@Service
public class ImagemService {

    /*
    * Função resposnsável por tratar as imagens para que ocupem menos espaço no banco de dados
    * */
    public byte[] tratarImagem(MultipartFile foto){
        try {
            //Verifica se o arquivo está vazio e se ele é do tipo imagem
            if (foto.isEmpty() || !foto.getContentType().startsWith("image/"))
                throw new IllegalArgumentException();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(foto.getInputStream())
                    .size(300, 300)//Recortar a imagem para 300 x 300
                    .outputFormat("jpeg")//Transforma em formato jpeg
                    .outputQuality(0.7)//Mantem só 70% da qualidade
                    .toOutputStream(outputStream);

            byte[] imagem = outputStream.toByteArray();
            return imagem;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
