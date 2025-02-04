package com.backend.OngConciencia.Controller;

import com.backend.OngConciencia.Dto.OngRequestDto;
import com.backend.OngConciencia.Dto.OngResponseDto;
import com.backend.OngConciencia.Model.Ong;
import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Services.OngServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/ong")
public class
OngController {

    //Injeto a depedencia do service
    @Autowired
    OngServices services;

    //endpoint para pegar todas as ongs
    @GetMapping
    public List<OngResponseDto> getAllOngs(){
        return services.getAllOngs();
    }

    //endpooint para pegar uma ong com um id passado na url
    @GetMapping("/{id}")
    public Optional<Ong> getOngById(@PathVariable("id") UUID id){
        return services.getOngBYId(id);
    }

    //endpoint para pegar somente 30 ongs. Fututaamente será as de melhor avaliação
    @GetMapping("/exibicao")
    public List<OngResponseDto> getOngsExibicao(){return services.get30Ongs();}

    @GetMapping("/search")
    public List<Ong> pesquisarOngs(@RequestParam String keyword){return services.pesquisarOngs(keyword);}

    //endpoint para cadastrar uma ong
    @PostMapping
    public ResponseEntity saveOng(@RequestBody OngRequestDto data){
        return services.saveOng(data);
    }

    //endpoint para atualizar uma ong já registrada
    @PutMapping
    public ResponseEntity uptOng(@RequestBody OngRequestDto data){
        return services.updateOng(data);
    }

    //endpoint para deletar todas as ongs registradas
    @DeleteMapping
    public ResponseEntity deleteAllOngs(){
        return services.deleteAllOngs();
    }

    //endpoint para deletar uma ong por um id
    @DeleteMapping("/{id}")
    public ResponseEntity DeleteOngById(@PathVariable("id") UUID id){
        return services.deleteOngById(id);
    }
}
