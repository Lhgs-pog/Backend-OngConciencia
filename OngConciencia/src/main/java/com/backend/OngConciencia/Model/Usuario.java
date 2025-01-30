package com.backend.OngConciencia.Model;

import com.backend.OngConciencia.Dto.UsuarioRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Table(name = "usuario")

@Entity(name = "usuario")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@EqualsAndHashCode(of = "id")

@Component
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "senha", nullable = false)
    private String senha;
    @Enumerated(EnumType.STRING)
    @Column(name = "roles" ,nullable = false)
    private UsuarioRole role;
    @Lob
    @Column(name = "foto", columnDefinition = "oid")
    private byte[] foto;

    public Usuario(String nome, String email, String senha){
        this.nome=nome;
        this.email=email;
        this.senha=senha;
    }

    public Usuario(String nome, String email, String senha, UsuarioRole role){
        this.nome=nome;
        this.email=email;
        this.senha=senha;
        this.role=role;
    }


    public Usuario(UsuarioRequestDto user){
        this.nome= user.nome();
        this.email= user.email();
        this.senha= user.senha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UsuarioRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
