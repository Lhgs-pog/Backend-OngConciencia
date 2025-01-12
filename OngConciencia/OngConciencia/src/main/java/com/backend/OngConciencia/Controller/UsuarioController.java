package com.backend.OngConciencia.Controller;

import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import com.backend.OngConciencia.Dto.UsuarioResponseDto;
import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    UsuarioServices services;

    @GetMapping
    public List<UsuarioResponseDto> getAllUsuarios(){
        return services.findAllUsuarios();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> getUsuarioById(@PathVariable("id")BigInteger id){
        return services.findUsuarioById(id);
    }

    @PostMapping
    public ResponseEntity postUsuario(@RequestBody UsuarioRequestDto data){
        return services.saveUsuario(data);
    }

    @PutMapping("/{id}")
    public ResponseEntity uptUsuario(@PathVariable("id") BigInteger id,@RequestBody UsuarioRequestDto data){
        return services.updateUsuario(id, data);
    }

    @DeleteMapping
    public ResponseEntity deleteAllUsuarios(){
        return services.deleteAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUsuarioById(@PathVariable("id") BigInteger id){
        return services.deleteByID(id);
    }
}
