package com.backend.OngConciencia.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "codigo")
@Entity(name = "codigo")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@EqualsAndHashCode(of = "id")

@Component
public class Codigo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private BigInteger id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "codigo", nullable = false)
    private int codigo;
    @Column(name = "dia", nullable = false)
    private LocalDateTime dia;

    public Codigo(String email, int codigo, LocalDateTime dia){
        this.email=email;
        this.codigo=codigo;
        this.dia=dia;
    }
}
