package me.dio.rest.repository;

import java.util.Optional;
import me.dio.rest.entity.Cerveja;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CervejaRepository extends JpaRepository<Cerveja, Long> {
    Optional<Cerveja> findByNome(String nome);
}
