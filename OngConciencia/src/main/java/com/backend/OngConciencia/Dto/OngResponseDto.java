package com.backend.OngConciencia.Dto;

import com.backend.OngConciencia.Model.Ong;

import java.math.BigInteger;

public record OngResponseDto(BigInteger id, String nome, String descricao, String email, String telefone, String link_img, String link_site) {
    public OngResponseDto(Ong ong){
        this(ong.getId(),ong.getNome(), ong.getDescricao(), ong.getEmail(), ong.getTelefone(), ong.getLink_img(), ong.getLink_site());
    }
}
