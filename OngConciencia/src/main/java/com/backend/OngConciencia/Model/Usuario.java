package com.backend.OngConciencia.Model;

import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Table(name = "usuario")

@Entity(name = "usuario")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@EqualsAndHashCode(of = "id")

@Component
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private BigInteger id;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "senha", nullable = false)
    private String senha;

    public Usuario(String nome, String email, String senha){
        this.nome=nome;
        this.email=email;
        this.senha=senha;
    }

    public Usuario(UsuarioRequestDto user){
        this.nome= user.nome();
        this.email= user.email();
        this.senha= user.senha();
    }

}
