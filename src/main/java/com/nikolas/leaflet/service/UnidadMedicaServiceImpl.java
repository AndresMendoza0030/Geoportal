package com.nikolas.leaflet.service;

import com.nikolas.leaflet.domain.UnidadMedica;
import com.nikolas.leaflet.repository.ClinicaComunalRepository;
import com.nikolas.leaflet.repository.UnidadMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UnidadMedicaServiceImpl implements UnidadMedicaService {
    @Autowired
    private UnidadMedicaRepository unidadMedicaRepository;

    @Override
    public List<UnidadMedica> buscarPorMunicipio(String municipio) {
        return unidadMedicaRepository.findByMunicipio(municipio);
    }

    @Override
    public Optional<UnidadMedica> unidadMedicaGetOne(Integer id) {
        return unidadMedicaRepository.findById(id);
    }

    @Override
    public List<UnidadMedica> unidadMedicaGetAll() {
        return unidadMedicaRepository.findAll();
    }
    
    // Implementación del nuevo método de paginación
    @Override
    public Page<UnidadMedica> buscarPorMunicipio(String municipio, Pageable pageable) {
        return unidadMedicaRepository.findByMunicipio(municipio, pageable);
    }
     // Implementación del nuevo método de paginación
     @Override
     public Page<UnidadMedica> unidadMedicaGetAll(Pageable pageable) {
         return unidadMedicaRepository.findAll(pageable);
     }
      public List<String> getDistinctMunicipios() {
        return  unidadMedicaRepository.findDistinctMunicipios();
    }
}
