package com.backend.OngConciencia.Dto;

public record UsuarioRequestDto(String nome, String email, String senha, byte[] foto) {
}
