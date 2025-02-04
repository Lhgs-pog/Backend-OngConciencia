package com.backend.OngConciencia.Model;


import com.backend.OngConciencia.Dto.OngRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "ong")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@EqualsAndHashCode(of = "id")
public class Ong {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "telefone", nullable = false, unique = true)
    private String telefone;
    @Column(name = "link_img", nullable = true)
    private String link_img;
    @Column(name = "link_site", nullable = false)
    private String link_site;

    public Ong(OngRequestDto dto){
        this.nome=dto.nome();
        this.email=dto.email();
        this.descricao=dto.descricao();
        this.telefone=dto.telefone();
        this.link_img=dto.link_img();
        this.link_site=dto.link_site();
    }
}
