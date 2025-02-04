package com.backend.OngConciencia.Repository;

import com.backend.OngConciencia.Model.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface OngRespository extends JpaRepository<Ong, String> {
    Optional<Ong> findOptionalByNome(String nome);

    /*
    * Query para barra de pesquisa onde eu posso achar ongs pelo nome mesmo que eu não tenha colocado o nome completo, por exemplo:
    * pesquisa "médico", o resultado médicos sem fronteiras ainda apareceria
    * */
    @Query("SELECT o FROM ONG o WHERE SIMILARITY(LOWER(o.nome), LOWER(:keyword)) > 0.3 ORDER BY SIMILARITY(LOWER(o.nome), LOWER(:keyword)) DESC")
    List<Ong> findByName(@Param("keyword") String keyword);
}
/*Query de pesquisa

*SELECT o	Seleciona os dados da entidade ONG.
* FROM ONG o	Obtém os dados da tabela ONG, com alias o.
* SIMILARITY(LOWER(o.nome), LOWER(:keyword)) > 0.3	Usa a função SIMILARITY() para comparar nomes de ONGs com a palavra digitada pelo usuário. Retorna valores entre 0 e 1 (quanto maior, mais parecido).
* ORDER BY SIMILARITY(...) DESC	Ordena os resultados pela maior similaridade primeiro.
* */


/*
* Querys adicionadas ao banco de dados para que melhorem a permormace e permita buscas aproximadas
*
* CREATE EXTENSION IF NOT EXISTS pg_trgm; Ativa a extensão pg_trgm que permite as buscas aproximadas
* CREATE INDEX idx_ong_nome_trgm ON ong USING gin (LOWER(nome) gin_trgm_ops);
* CREATE INDEX idx_ong_nome_trgm	Cria um índice chamado idx_ong_nome_trgm para otimizar buscas.
* ON ong	Define que o índice será aplicado na tabela ong.
* USING gin	Usa a estrutura GIN (Generalized Inverted Index), ideal para buscas rápidas em textos grandes.
* LOWER(nome) gin_trgm_ops	Aplica o índice em LOWER(nome), tornando buscas case-insensitive, e usa gin_trgm_ops para otimizar consultas com LIKE e buscas aproximadas.
* */

/*Resumo
*
* LIKE '%keyword%'	Busca parcial (exato ou parte da palavra).
* ILIKE '%keyword%'	Igual ao LIKE, mas insensível a maiúsculas/minúsculas.
* pg_trgm + GIN INDEX	Melhora performance para buscas com LIKE e buscas aproximadas.
* SIMILARITY()	Retorna resultados mesmo com erros de digitação.
* */
