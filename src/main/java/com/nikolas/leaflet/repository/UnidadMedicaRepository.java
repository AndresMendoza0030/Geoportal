package com.nikolas.leaflet.repository;

import com.nikolas.leaflet.domain.UnidadMedica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface UnidadMedicaRepository extends JpaRepository<UnidadMedica, Integer> {
    Optional<UnidadMedica> findById(Integer id);
    List<UnidadMedica> findAll();
    List<UnidadMedica> findByMunicipio(String municipio);
    @Query("SELECT DISTINCT u.municipio FROM UnidadMedica u")
    List<String> findDistinctMunicipios();
    // Método con paginación
    Page<UnidadMedica> findByMunicipio(String municipio, Pageable pageable);
    Page<UnidadMedica> findAll(Pageable pageable);
}
