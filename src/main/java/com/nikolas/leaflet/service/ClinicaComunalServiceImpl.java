package com.nikolas.leaflet.service;

import java.util.Optional;
import com.nikolas.leaflet.domain.ClinicaComunal;
import com.nikolas.leaflet.domain.UnidadMedica;
import com.nikolas.leaflet.repository.ClinicaComunalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ClinicaComunalServiceImpl implements ClinicaComunalService{
    @Autowired
    ClinicaComunalRepository ClinicaComunalRepository;
    @Override
    public List<ClinicaComunal> clinicaComunalGetAll() {
        return ClinicaComunalRepository.findAll();
    }
    @Override
    public  List<ClinicaComunal> buscarPorMunicipio(String municipio) {
        return ClinicaComunalRepository.findByMunicipio(municipio);
    }
    @Override
    public Optional<ClinicaComunal>  clinicaComunalGetOne(Integer id) {

        return ClinicaComunalRepository.findById(id);
    }

    
    @Override
    public List<ClinicaComunal>  findByNombreContaining(String nombre) {
        return ClinicaComunalRepository.findByNombreContaining(nombre);
    }
    public List<String> getDistinctMunicipios() {
        return ClinicaComunalRepository.findDistinctMunicipios();
    }
    @Override
    public List<ClinicaComunal> buscarPorMunicipios(List<String> municipios) {
        return ClinicaComunalRepository.findByMunicipios(municipios);
    }
  // Implementación del nuevo método de paginación
     @Override
     public Page<ClinicaComunal> ClinicaComunalGetAll(Pageable pageable) {
         return ClinicaComunalRepository.findAll(pageable);
     }
     @Override
     public Page<ClinicaComunal> buscarPorMunicipio(String municipio, Pageable pageable) {
         return ClinicaComunalRepository.findByMunicipio(municipio, pageable);
     }
}
