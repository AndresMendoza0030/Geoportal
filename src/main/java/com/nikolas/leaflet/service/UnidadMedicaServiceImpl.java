package com.nikolas.leaflet.service;

import com.nikolas.leaflet.domain.UnidadMedica;
import com.nikolas.leaflet.repository.UnidadMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.List;


@Component
public class UnidadMedicaServiceImpl implements UnidadMedicaService{
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
}