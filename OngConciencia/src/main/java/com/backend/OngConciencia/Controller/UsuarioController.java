package com.backend.OngConciencia.Controller;

import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import com.backend.OngConciencia.Dto.UsuarioResponseDto;
import com.backend.OngConciencia.Model.Usuario;
import com.backend.OngConciencia.Services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Optional<Usuario> getUsuarioById(@PathVariable("id")String id){
        return services.findUsuarioById(id);
    }

    @PostMapping("/{tentativa}")
    public ResponseEntity postUsuario(@PathVariable("tentativa") int tentativa ,@RequestBody UsuarioRequestDto data){
        return services.saveUsuario(data, tentativa);
    }

    @PutMapping("/{id}")
    public ResponseEntity uptUsuario(@PathVariable("id") String id,@RequestBody UsuarioRequestDto data){
        return services.updateUsuario(id, data);
    }

    @DeleteMapping
    public ResponseEntity deleteAllUsuarios(){
        return services.deleteAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUsuarioById(@PathVariable("id") String id){
        return services.deleteByID(id);
    }
}
