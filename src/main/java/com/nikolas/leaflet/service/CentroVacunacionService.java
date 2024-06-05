package com.nikolas.leaflet.service;

import com.nikolas.leaflet.domain.ClinicaComunal;

import java.io.Serializable;
import java.util.List;

public interface CentroVacunacionService extends Serializable {
    ClinicaComunal clinicaComunalGetOne(Integer id);
    List<ClinicaComunal> clinicaComunalGetAll();
    List<ClinicaComunal> buscarPorMunicipio(String municipio);
    List<ClinicaComunal> findByNombreContaining(String nombre);
    List<String> getDistinctMunicipios(); 
    List<ClinicaComunal> buscarPorMunicipios(List<String> municipio);
}
