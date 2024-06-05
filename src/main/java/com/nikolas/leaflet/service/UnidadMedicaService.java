package com.nikolas.leaflet.service;

import com.nikolas.leaflet.domain.UnidadMedica;

import java.io.Serializable;
import java.util.List;

public interface UnidadMedicaService extends Serializable {
    UnidadMedica unidadMedicaGetOne(Integer id);
    List<UnidadMedica> unidadMedicaGetAll();
    List<UnidadMedica> buscarPorMunicipio(String municipio);
}
