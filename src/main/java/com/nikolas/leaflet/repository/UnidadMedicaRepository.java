package com.nikolas.leaflet.repository;

import com.nikolas.leaflet.domain.UnidadMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UnidadMedicaRepository extends JpaRepository<UnidadMedica, Integer> {

    Optional<UnidadMedica> findById(Integer id);
    List<UnidadMedica> findAll();
    
    List<UnidadMedica> findByMunicipio(String municipio);

}
