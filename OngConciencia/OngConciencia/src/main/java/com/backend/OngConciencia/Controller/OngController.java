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

@RestController
@RequestMapping("/ong")
public class OngController {

    @Autowired
    OngServices services;

    @GetMapping
    public List<OngResponseDto> getAllOngs(){
        return services.getAllOngs();
    }

    @GetMapping("/{id}")
    public Optional<Ong> getOngById(@PathVariable("id")BigInteger id){
        return services.getOngBYId(id);
    }

    @PostMapping
    public ResponseEntity saveOng(@RequestBody OngRequestDto data){
        return services.saveUsuario(data);
    }

    @PutMapping
    public ResponseEntity uptOng(@RequestBody OngRequestDto data){
        return services.updateUsuario(data);
    }

    @DeleteMapping
    public ResponseEntity deleteAllOngs(){
        return services.deleteAllOngs();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity DeleteOngById(@PathVariable("id") BigInteger id){
        return services.deleteOngById(id);
    }
}
