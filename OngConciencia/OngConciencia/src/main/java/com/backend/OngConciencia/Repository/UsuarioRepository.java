package com.backend.OngConciencia.Repository;

import com.backend.OngConciencia.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, BigInteger> {
    Optional<Usuario> findOptionalByEmail(String email);
}
