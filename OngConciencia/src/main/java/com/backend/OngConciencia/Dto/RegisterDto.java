package com.backend.OngConciencia.Dto;

import com.backend.OngConciencia.Model.UsuarioRole;

public record RegisterDto(String email, String senha, UsuarioRole role) {
}
