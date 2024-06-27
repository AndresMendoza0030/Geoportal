package com.nikolas.leaflet.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.nikolas.leaflet.domain.ClinicaComunal;
import com.nikolas.leaflet.domain.UnidadMedica;
import com.nikolas.leaflet.service.CentroVacunacionService;
import com.nikolas.leaflet.service.UnidadMedicaService;
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
import org.springframework.web.servlet.ModelAndView;

import com.nikolas.leaflet.domain.LeafletMap;
import com.nikolas.leaflet.service.LeafletMapService;
import com.nikolas.leaflet.util.GenericResponse;
import com.nikolas.leaflet.dto.BusquedaDTO;


@Controller
@RequestMapping("/map")
public class LeafletMapController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	LeafletMapService leafletMapService;


	@Autowired
	CentroVacunacionService centroVacunacionService;

	@Autowired
	UnidadMedicaService UnidadMedicaService;

	
	@RequestMapping(value = "/index")
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		final LeafletMap  leafletMap = this.leafletMapService.leafletMap(2);
		myModel.put("map", leafletMap);
		ModelAndView mav = new ModelAndView();
		List<ClinicaComunal> cvList = centroVacunacionService.clinicaComunalGetAll();
		List<String> municipios = centroVacunacionService.getDistinctMunicipios();
		mav.addObject("centros",cvList);
		mav.addObject("model",myModel);
		mav.addObject("municipios", municipios);
		return mav;

	}
	
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse registerUserAccount(@Valid final LeafletMap leafletMap, final HttpServletRequest request) {
    	logger.debug("Registering user account with information: {}",leafletMap);
    	leafletMapService.updateLeafletMap(leafletMap);
        return new GenericResponse("success");
    }


	@RequestMapping(value = "/personas")
	public ModelAndView ingresarPersona() {

		Map<String, Object> myModel = new HashMap<String, Object>();

		final LeafletMap  leafletMap = this.leafletMapService.leafletMap(2);
		myModel.put("map", leafletMap);
		ModelAndView mav = new ModelAndView();
		List<UnidadMedica> centros2 = UnidadMedicaService.unidadMedicaGetAll();
		mav.addObject("centros",centros2);
		mav.addObject("model",myModel);
		mav.setViewName("/map/personas");
		return mav;

	}

	@RequestMapping("/ipersonas")
	public ModelAndView personaVacunada(){
		ModelAndView mav = new ModelAndView();
		UnidadMedica personaVacunada = new UnidadMedica();
		mav.addObject("personaVacunada",personaVacunada);
		mav.setViewName("/map/ingresarPersonaVacunada");
		return mav;
	}
	@RequestMapping("/inpersonas")
	public ModelAndView inPersonaVacunada(@Valid @ModelAttribute UnidadMedica personaVacunada, BindingResult result){
		
		Map<String, Object> myModel = new HashMap<String, Object>();

		final LeafletMap  leafletMap = this.leafletMapService.leafletMap(2);
		myModel.put("map", leafletMap);
		ModelAndView mav = new ModelAndView();
		List<ClinicaComunal> unidades = centroVacunacionService.clinicaComunalGetAll();
		mav.addObject("centros",unidades);
		mav.addObject("model",myModel);
		return mav;

	
	} 
	
	@GetMapping("/informacion")
public ModelAndView mostrarInformacion(@RequestParam("id") int id) {
    List<String> debugMessages = new ArrayList<>();
    debugMessages.add("Handling request to /informacion with id: " + id);
    
    ModelAndView mav = new ModelAndView();
    
    try {
        Optional<ClinicaComunal> clinicaOpt = centroVacunacionService.clinicaComunalGetOne(id);
        if (!clinicaOpt.isPresent()) {
            debugMessages.add("Clinica no encontrada con id: " + id);
            mav.addObject("error", "Clinica no encontrada");
            mav.addObject("debugMessages", debugMessages);
            mav.setViewName("error");
            return mav;
        }

        ClinicaComunal clinica = clinicaOpt.get();
        mav.addObject("clinica", clinica);
        debugMessages.add("Returning ModelAndView for /informacion with clinica: " + clinica);
        mav.setViewName("map/informacion");
    } catch (Exception e) {
        debugMessages.add("Error al manejar la solicitud a /informacion");
        debugMessages.add(e.getMessage());
        mav.addObject("error", e.getMessage());
        mav.setViewName("error");
    }
    
    mav.addObject("debugMessages", debugMessages);
    return mav;
}


}
