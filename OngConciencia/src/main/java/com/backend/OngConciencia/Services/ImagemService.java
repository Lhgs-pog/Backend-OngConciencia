package com.backend.OngConciencia.Services;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;

@Service
public class ImagemService {

    public byte[] tratarImagem(MultipartFile foto){
        try {
            if (foto.isEmpty() || !foto.getContentType().startsWith("image/"))
                throw new IllegalArgumentException();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(foto.getInputStream())
                    .size(300, 300)
                    .outputFormat("jpeg")
                    .outputQuality(0.7)
                    .toOutputStream(outputStream);

            byte[] imagem = outputStream.toByteArray();
            return imagem;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
