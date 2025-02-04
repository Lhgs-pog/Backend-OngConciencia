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

    //Denpendencias
    @Autowired
    OngRespository respository;

    @Autowired
    EmailServices mail;

    /*
    * Retorna todas as ongs
    * */
    public List<OngResponseDto> getAllOngs(){
        return respository.findAll().stream()
                .map(OngResponseDto::new)
                .toList();
    }

    /*
    * Retorna uma lista de 30 ongs do banco de dados
    * */
    public List<OngResponseDto> get30Ongs(){
        //Retorna todas as ongs
        List<OngResponseDto> todas = respository.findAll().stream()
                .map(OngResponseDto::new)
                .toList();

        //Adicionas as ongs a um array com limite de 30
        ArrayList<OngResponseDto> limitada = new ArrayList<OngResponseDto>(30);
        for (int i=0; i < 30; i++){
            limitada.add(todas.get(i));
        }
        return limitada;
    }

    /*
    * Retorna uma ong com id epecífico
    * */
    public Optional<Ong> getOngBYId(String id){
        return respository.findById(id);
    }

    /*
    * Salva uma nova ong
    * */
    @Transactional
    public ResponseEntity saveOng(OngRequestDto data){

        //Verifica se o nome já existe
        if(respository.findOptionalByNome(data.nome()).isPresent()){
            throw new DataIntegrityViolationException("Já existe uma ong com esse nome");
        }

        //Converte o dto em um objeto ong
        Ong ong = new Ong(data);
        respository.save(ong);//Salva a ong no banco de dados

        //Retorna um status da resposta
        return ResponseEntity.ok("Ong cadastrada com sucesso");
    }

    /*
    * Atualiza os dados de uma ong já existente
    * */
    @Transactional
    public ResponseEntity updateOng(OngRequestDto data){

        //Verifica se ela existe
        Ong ongexistente = respository.findOptionalByNome(data.nome())
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma ong encontrada com esse nome"));


        //Muda os atributos
        ongexistente.setNome(data.nome());
        ongexistente.setDescricao(data.descricao());
        ongexistente.setEmail(data.email());
        ongexistente.setTelefone(data.telefone());
        ongexistente.setLink_img(data.link_img());
        ongexistente.setLink_site(data.link_site());

        respository.save(ongexistente);//Salva a ong

        //status da reposta
        return ResponseEntity.ok("Ong cadastrada com sucesso");
    }

    /*
    * Deleta um ong pelo id passado
    * */
    @Transactional
    public ResponseEntity deleteOngById(String id){
        respository.deleteById(id);

        //Status da resposta
        return ResponseEntity.ok("Ong deletada com sucesso");
    }

    /*
    * Deleta todas as ongs do banco de dados
    * */
    @Transactional
    public ResponseEntity deleteAllOngs(){
        respository.deleteAll();

        //sttaus da resposta
        return ResponseEntity.ok("Ongs deletadas com sucesso");
    }

}
