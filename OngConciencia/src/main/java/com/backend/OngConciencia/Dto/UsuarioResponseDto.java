package com.backend.OngConciencia.Dto;

import com.backend.OngConciencia.Model.Usuario;

import java.math.BigInteger;

public record UsuarioResponseDto(String id, String nome, String email, String senha) {
    public UsuarioResponseDto(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }
}
