package com.backend.OngConciencia.Services;

import com.backend.OngConciencia.Dto.OngRequestDto;
import com.backend.OngConciencia.Dto.OngResponseDto;
import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import com.backend.OngConciencia.Model.Ong;
import com.backend.OngConciencia.Repository.OngRespository;
import com.backend.OngConciencia.Security.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OngServices {

    @Autowired
    OngRespository respository;

    @Autowired
    EmailServices mail;

    public List<OngResponseDto> getAllOngs(){
        return respository.findAll().stream()
                .map(OngResponseDto::new)
                .toList();
    }

    //pega somente 30 ongs do banco
    public List<OngResponseDto> get30Ongs(){
        List<OngResponseDto> todas = respository.findAll().stream()
                .map(OngResponseDto::new)
                .toList();

        ArrayList<OngResponseDto> limitada = new ArrayList<OngResponseDto>(30);
        for (int i=0; i < 30; i++){
            limitada.add(todas.get(i));
        }
        return limitada;
    }

    public Optional<Ong> getOngBYId(BigInteger id){
        return respository.findById(id);
    }

    @Transactional
    public ResponseEntity saveUsuario(OngRequestDto data){

        if (!(respository.findOptionalByNome(data.nome()) == null)){
            throw new DataIntegrityViolationException("Já existe uma ong com esse nome");
        }

        Ong ong = new Ong(data);

        return ResponseEntity.ok("Ong cadastrada com sucesso");
    }

    @Transactional
    public ResponseEntity updateUsuario(OngRequestDto data){

        Ong ongexistente = respository.findOptionalByNome(data.nome())
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma ong encontrada com esse nome"));

        ongexistente.setNome(data.nome());
        ongexistente.setDescricao(data.descricao());
        ongexistente.setEmail(data.email());
        ongexistente.setTelefone(data.telefone());
        ongexistente.setLink_img(data.link_img());
        ongexistente.setLink_site(data.link_site());

        respository.save(ongexistente);

        return ResponseEntity.ok("Ong cadastrada com sucesso");
    }

    @Transactional
    public ResponseEntity deleteOngById(BigInteger id){
        respository.deleteById(id);

        return ResponseEntity.ok("Ong deletada com sucesso");
    }

    @Transactional
    public ResponseEntity deleteAllOngs(){
        respository.deleteAll();

        return ResponseEntity.ok("Ongs deletadas com sucesso");
    }

}
