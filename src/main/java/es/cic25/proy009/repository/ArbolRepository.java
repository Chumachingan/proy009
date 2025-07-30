package es.cic25.proy009.repository;

import es.cic25.proy009.model.Arbol;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio JPA para la entidad Arbol
public interface ArbolRepository extends JpaRepository<Arbol, Long> {
    // Métodos CRUD heredados de JpaRepository
}