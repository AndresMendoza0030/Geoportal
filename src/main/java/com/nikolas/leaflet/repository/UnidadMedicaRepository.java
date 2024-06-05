package com.nikolas.leaflet.repository;

import com.nikolas.leaflet.domain.UnidadMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnidadMedicaRepository extends JpaRepository<UnidadMedica, Integer> {

    UnidadMedica findOne(Integer id);
    List<UnidadMedica> findAll();
    
    List<UnidadMedica> findByMunicipio(String municipio);

}
