package com.nikolas.leaflet.repository;

import com.nikolas.leaflet.domain.ClinicaComunal;
import com.nikolas.leaflet.domain.UnidadMedica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClinicaComunalRepository extends JpaRepository<ClinicaComunal,Integer> {


    Optional<ClinicaComunal> findById(Integer id);
    List<ClinicaComunal> findAll();
    
    List<ClinicaComunal> findByMunicipio(String municipio);
    List<ClinicaComunal> findByNombreContaining(String nombre);
    @Query("SELECT DISTINCT c.municipio FROM ClinicaComunal c")
    List<String> findDistinctMunicipios();
    @Query("SELECT c FROM ClinicaComunal c WHERE c.municipio IN :municipios")
    List<ClinicaComunal> findByMunicipios(List<String> municipios);
    // Método con paginación
    Page<ClinicaComunal> findByMunicipio(String municipio, Pageable pageable);
    Page<ClinicaComunal> findAll(Pageable pageable);
}
