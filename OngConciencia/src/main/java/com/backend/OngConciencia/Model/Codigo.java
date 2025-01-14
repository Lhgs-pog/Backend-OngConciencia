package com.backend.OngConciencia.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

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
    private int id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "codigo", nullable = false)
    private int codigo;
    @Column(name = "dia", nullable = false)
    private Date dia;

    
}
