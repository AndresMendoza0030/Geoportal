package com.nikolas.leaflet.service;

import com.nikolas.leaflet.domain.UnidadMedica;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
public interface UnidadMedicaService extends Serializable {
    Optional<UnidadMedica> unidadMedicaGetOne(Integer id);
    List<UnidadMedica> unidadMedicaGetAll();
    List<UnidadMedica> buscarPorMunicipio(String municipio);
}
