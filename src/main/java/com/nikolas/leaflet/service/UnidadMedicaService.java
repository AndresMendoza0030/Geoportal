package com.nikolas.leaflet.service;

import com.nikolas.leaflet.domain.UnidadMedica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface UnidadMedicaService extends Serializable {
    Optional<UnidadMedica> unidadMedicaGetOne(Integer id);
    List<UnidadMedica> unidadMedicaGetAll();
    List<UnidadMedica> buscarPorMunicipio(String municipio);
    List<String> getDistinctMunicipios(); 
    // Nuevo método para paginación
    Page<UnidadMedica> buscarPorMunicipio(String municipio, Pageable pageable);
    Page<UnidadMedica> unidadMedicaGetAll(Pageable pageable);
}
