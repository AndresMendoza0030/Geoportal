package com.nikolas.leaflet.service;

import com.nikolas.leaflet.domain.ClinicaComunal;
import com.nikolas.leaflet.repository.ClinicaComunalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CentroVacunacionServiceImpl implements CentroVacunacionService{
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
    public ClinicaComunal clinicaComunalGetOne(Integer id) {

        return ClinicaComunalRepository.findOne(id);
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

}
