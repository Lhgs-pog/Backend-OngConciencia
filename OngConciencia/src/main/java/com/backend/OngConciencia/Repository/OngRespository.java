package com.backend.OngConciencia.Repository;

import com.backend.OngConciencia.Model.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface OngRespository extends JpaRepository<Ong, String> {
    Optional<Ong> findOptionalByNome(String nome);
}
