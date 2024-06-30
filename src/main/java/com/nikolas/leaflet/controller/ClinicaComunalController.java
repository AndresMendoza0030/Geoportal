package com.nikolas.leaflet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.nikolas.leaflet.domain.ClinicaComunal;
import com.nikolas.leaflet.domain.PersonaMunicipio;
import com.nikolas.leaflet.domain.UnidadMedica;
import com.nikolas.leaflet.service.ClinicaComunalService;
import com.nikolas.leaflet.service.ClinicaComunalServiceImpl;
import com.nikolas.leaflet.service.UnidadMedicaService;
import com.nikolas.leaflet.service.UnidadMedicaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.nikolas.leaflet.dto.BusquedaDTO;

@RestController
@RequestMapping("/clinicaComunal") 
public class ClinicaComunalController {
  
    @Autowired
    private ClinicaComunalService ClinicaComunalService;

    @PostMapping("/buscar")
    public ResponseEntity<List<ClinicaComunal>> getClinicasByMunicipio(@RequestBody BusquedaDTO busqueda) {
        List<ClinicaComunal> clinicas = ClinicaComunalService.buscarPorMunicipio(busqueda.getMunicipio());
        if (clinicas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clinicas);
    }
    @PostMapping("/municipios")
    public ResponseEntity<List<String>> getDistinctMunicipios() {
        List<String> municipios =ClinicaComunalService.getDistinctMunicipios();
        if (municipios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(municipios);
    }
    @PostMapping("/buscarm")
    public ResponseEntity<List<ClinicaComunal>> getClinicasByMunicipios(@RequestBody List<String> municipio) {
    List<ClinicaComunal> clinicas = new ArrayList<>();
    for (String municipios : municipio) {
        clinicas.addAll(ClinicaComunalService.buscarPorMunicipio(municipios));
    }
    if (clinicas.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(clinicas);
}

}