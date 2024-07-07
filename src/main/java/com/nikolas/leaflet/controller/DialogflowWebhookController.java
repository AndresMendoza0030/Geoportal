package com.nikolas.leaflet.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.nikolas.leaflet.domain.ClinicaComunal;
import com.nikolas.leaflet.service.ClinicaComunalService;
import com.nikolas.leaflet.service.UnidadMedicaService;
import com.nikolas.leaflet.domain.UnidadMedica;
import com.nikolas.leaflet.dto.DialogFlowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dialogflow")
public class DialogflowWebhookController {

    @Autowired
    private ClinicaComunalService ClinicaComunalService;
    @Autowired
    private UnidadMedicaService UnidadMedicaService;

    @PostMapping("/webhook")
    public ResponseEntity<JsonObject> handleDialogflowRequest(@RequestBody DialogFlowRequest request) {
        try {
            String intentName = request.getQueryResult().getIntent().getDisplayName();
            if ("buscarClinicasPorMunicipio".equals(intentName)) {
                Map<String, Object> params = request.getQueryResult().getParameters();
                Map<String, String> location = (Map<String, String>) params.get("location");
                String municipio = extractLocation(location);
                List<ClinicaComunal> clinicas = ClinicaComunalService.buscarPorMunicipio(municipio);
                JsonObject responseJson = createFulfillmentMessageJson(municipio,clinicas);
                return ResponseEntity.ok().body(responseJson);
            }
            if ("buscarHorarioClinica".equals(intentName)) {
                Map<String, Object> params = request.getQueryResult().getParameters();
                Map<String, String> location = (Map<String, String>) params.get("location");
                String municipio = extractLocation(location);
                List<ClinicaComunal> clinicas = ClinicaComunalService.findByNombreContaining(municipio);
                JsonObject responseJson = createFulfillmentMessageJson2(municipio,clinicas);
                return ResponseEntity.ok().body(responseJson);
            }
            if ("buscarUnidadesMedicasPorMunicipio".equals(intentName)) {
                Map<String, Object> params = request.getQueryResult().getParameters();
                Map<String, String> location = (Map<String, String>) params.get("location");
                String municipio = extractLocation(location);
                List<UnidadMedica> unidadesMedicas = UnidadMedicaService.buscarPorMunicipio(municipio);
                JsonObject responseJson = createFulfillmentMessageJson3(municipio, unidadesMedicas);
                return ResponseEntity.ok().body(responseJson);
            }
            
            if ("buscarHorarioUnidadMedica".equals(intentName)) {
                Map<String, Object> params = request.getQueryResult().getParameters();
                Map<String, String> location = (Map<String, String>) params.get("location");
                String municipio = extractLocation(location);
                
               
                
                List<UnidadMedica> unidadesMedicas = UnidadMedicaService.findByNombreContaining(municipio);
                JsonObject responseJson = createFulfillmentMessageJson4(municipio, unidadesMedicas);
                return ResponseEntity.ok().body(responseJson);
            }
            
            if ("ServiciosOfrecidos".equals(intentName)) {
                JsonObject responseJson = createFulfillmentMessageServiciosOfrecidos();
                return ResponseEntity.ok().body(responseJson);
            } 
            JsonObject defaultMessage = new JsonObject();
            defaultMessage.addProperty("fulfillmentText", "No valid intent matched.");
            return ResponseEntity.ok(defaultMessage);
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson);
        }
    }

    private JsonObject createFulfillmentMessageJson(String municipio, List<ClinicaComunal> clinicas) {
        JsonObject fulfillmentMessage = new JsonObject();
        JsonArray fulfillmentMessages = new JsonArray();
    
        // Message for introductory text or no clinics found
        JsonObject textMessage1 = new JsonObject();
        JsonObject textObject = new JsonObject();
        textObject.add("text", new JsonArray());
        if (clinicas.isEmpty()) {
            textObject.getAsJsonArray("text").add("No se encontraron clínicas en el municipio especificado.");
        } else {
            textObject.getAsJsonArray("text").add("Aquí tienes la información de las clínicas:");
        }
        textMessage1.add("text", textObject);
        fulfillmentMessages.add(textMessage1);
        if (!clinicas.isEmpty()) {
            JsonArray richContentOuterArray = new JsonArray();
            JsonArray richContentInnerArray = new JsonArray();
        
            for (ClinicaComunal clinica : clinicas) {
                // Agrega la imagen como un objeto separado
                JsonObject imageObject = new JsonObject();
                imageObject.addProperty("type", "image");
                imageObject.addProperty("rawUrl", "https://pbs.twimg.com/media/FnakFq3WQAcWDUk?format=jpg&name=4096x4096");  // Asegúrate de que la URL sea accesible públicamente
                imageObject.addProperty("accessibilityText", "Imagen de la clínica " + clinica.getNombre());
                richContentInnerArray.add(imageObject);
        
                // Agrega la información de la clínica como otro objeto
                JsonObject infoObject = new JsonObject();
                infoObject.addProperty("type", "info");
                infoObject.addProperty("title", clinica.getNombre());
                infoObject.addProperty("subtitle", "Dirección: " + clinica.getDireccion());
                infoObject.addProperty("actionLink", "https://www.google.com/maps/dir//JVV5%2BJW5,+C.+Alberto+Masferrer,+San+Salvador/@13.6439933,-89.2225477,12z/data=!4m8!4m7!1m0!1m5!1m1!1s0x8f63368504511375:0x746ee51e341edef8!2m2!1d-89.1401494!2d13.6439782?entry=ttu" + clinica.getId());
                richContentInnerArray.add(infoObject);
        
                // Añadir un objeto "divider" después de cada clinica
                JsonObject dividerObject = new JsonObject();
                dividerObject.addProperty("type", "divider");
                richContentInnerArray.add(dividerObject);
            }
        
            // Eliminar el último "divider" que se añadió después de la última clínica
            if (clinicas.size() > 0) {
                richContentInnerArray.remove(richContentInnerArray.size() - 1);
            }
        
            richContentOuterArray.add(richContentInnerArray);
        
            JsonObject payloadObject = new JsonObject();
            payloadObject.add("richContent", richContentOuterArray);
        
            // Agregar el mensaje payload al array de mensajes de cumplimiento
            JsonObject payloadMessage = new JsonObject();
            payloadMessage.add("payload", payloadObject);
            
        
            fulfillmentMessages.add(payloadMessage);
        
            
            fulfillmentMessage.add("fulfillmentMessages", fulfillmentMessages);
        
           }
           return fulfillmentMessage;
         }
        
    
    
    private JsonObject createFulfillmentMessageJson2(String municipio,List<ClinicaComunal> clinicas) {
        JsonObject fulfillmentMessage = new JsonObject();
        JsonArray fulfillmentMessages = new JsonArray();

        JsonObject textObject = new JsonObject();
        JsonArray textArray = new JsonArray();

        if (clinicas.isEmpty()) {
            textArray.add("No se encontraron clínicas en el municipio especificado.");
        } else {
            textArray.add("Aquí tienes los horarios ofrecidos: ");
            for (ClinicaComunal clinica : clinicas) {
                String clinicInfo = clinica.getNombre() + " - Horario lunes a viernes: "+ clinica.getHorarioInicioSemana().replace("\"", "\\\"") +" a "+ clinica.getHorarioFinSemana().replace("\"", "\\\"");
                textArray.add(clinicInfo);
                if (clinica.getHorarioInicioFinde() == null|| clinica.getHorarioInicioFinde().isEmpty()){
                    String clinicInfo2 = clinica.getNombre() + " - Horario sábado y domingo: Cerrado";
                    textArray.add(clinicInfo2);
                } else {
                String clinicInfo2 = clinica.getNombre() + " - Horario sábado y domingo: "+ clinica.getHorarioInicioFinde().replace("\"", "\\\"") +" a "+ clinica.getHorarioFinFinde().replace("\"", "\\\"");
                textArray.add(clinicInfo2);
               
            }}

        }
        
        textObject.add("text", textArray);
        JsonObject singleMessage = new JsonObject();
        singleMessage.add("text", textObject);
        fulfillmentMessages.add(singleMessage);
        fulfillmentMessage.add("fulfillmentMessages", fulfillmentMessages);

        return fulfillmentMessage;
    }
   
    private JsonObject createFulfillmentMessageJson3(String municipio, List<UnidadMedica> clinicas) {
        JsonObject fulfillmentMessage = new JsonObject();
        JsonArray fulfillmentMessages = new JsonArray();
    
        // Message for introductory text or no clinics found
        JsonObject textMessage1 = new JsonObject();
        JsonObject textObject = new JsonObject();
        textObject.add("text", new JsonArray());
        if (clinicas.isEmpty()) {
            textObject.getAsJsonArray("text").add("No se encontraron clínicas en el municipio especificado.");
        } else {
            textObject.getAsJsonArray("text").add("Aquí tienes la información de las clínicas:");
        }
        textMessage1.add("text", textObject);
        fulfillmentMessages.add(textMessage1);
        if (!clinicas.isEmpty()) {
            JsonArray richContentOuterArray = new JsonArray();
            JsonArray richContentInnerArray = new JsonArray();
        
            for (UnidadMedica clinica : clinicas) {
                // Agrega la imagen como un objeto separado
                JsonObject imageObject = new JsonObject();
                imageObject.addProperty("type", "image");
                imageObject.addProperty("rawUrl", "https://pbs.twimg.com/media/FnakFq3WQAcWDUk?format=jpg&name=4096x4096");  // Asegúrate de que la URL sea accesible públicamente
                imageObject.addProperty("accessibilityText", "Imagen de la clínica " + clinica.getNombre());
                richContentInnerArray.add(imageObject);
        
                // Agrega la información de la clínica como otro objeto
                JsonObject infoObject = new JsonObject();
                infoObject.addProperty("type", "info");
                infoObject.addProperty("title", clinica.getNombre());
                infoObject.addProperty("subtitle", "Dirección: " + clinica.getDireccion());
                infoObject.addProperty("actionLink", "https://www.google.com/maps/dir//JVV5%2BJW5,+C.+Alberto+Masferrer,+San+Salvador/@13.6439933,-89.2225477,12z/data=!4m8!4m7!1m0!1m5!1m1!1s0x8f63368504511375:0x746ee51e341edef8!2m2!1d-89.1401494!2d13.6439782?entry=ttu" + clinica.getId());
                richContentInnerArray.add(infoObject);
        
                // Añadir un objeto "divider" después de cada clinica
                JsonObject dividerObject = new JsonObject();
                dividerObject.addProperty("type", "divider");
                richContentInnerArray.add(dividerObject);
            }
        
            // Eliminar el último "divider" que se añadió después de la última clínica
            if (clinicas.size() > 0) {
                richContentInnerArray.remove(richContentInnerArray.size() - 1);
            }
        
            richContentOuterArray.add(richContentInnerArray);
        
            JsonObject payloadObject = new JsonObject();
            payloadObject.add("richContent", richContentOuterArray);
        
            // Agregar el mensaje payload al array de mensajes de cumplimiento
            JsonObject payloadMessage = new JsonObject();
            payloadMessage.add("payload", payloadObject);
            
        
            fulfillmentMessages.add(payloadMessage);
        
            
            fulfillmentMessage.add("fulfillmentMessages", fulfillmentMessages);
        
           }
           return fulfillmentMessage;
         }
        
    
    
    private JsonObject createFulfillmentMessageJson4(String municipio,List<UnidadMedica> clinicas) {
        JsonObject fulfillmentMessage = new JsonObject();
        JsonArray fulfillmentMessages = new JsonArray();

        JsonObject textObject = new JsonObject();
        JsonArray textArray = new JsonArray();

        if (clinicas.isEmpty()) {
            textArray.add("No se encontraron clínicas en el municipio especificado.");
        } else {
            textArray.add("Aquí tienes los horarios ofrecidos: ");
            for (UnidadMedica clinica : clinicas) {
                String clinicInfo = clinica.getNombre() + " - Horario lunes a viernes: "+ clinica.getHorarioInicioSemana().replace("\"", "\\\"") +" a "+ clinica.getHorarioFinSemana().replace("\"", "\\\"");
                textArray.add(clinicInfo);
                if (clinica.getHorarioInicioFinde() == null|| clinica.getHorarioInicioFinde().isEmpty()){
                    String clinicInfo2 = clinica.getNombre() + " - Horario sábado y domingo: Cerrado";
                    textArray.add(clinicInfo2);
                } else {
                String clinicInfo2 = clinica.getNombre() + " - Horario sábado y domingo: "+ clinica.getHorarioInicioFinde().replace("\"", "\\\"") +" a "+ clinica.getHorarioFinFinde().replace("\"", "\\\"");
                textArray.add(clinicInfo2);
               
            }}

        }
        
        textObject.add("text", textArray);
        JsonObject singleMessage = new JsonObject();
        singleMessage.add("text", textObject);
        fulfillmentMessages.add(singleMessage);
        fulfillmentMessage.add("fulfillmentMessages", fulfillmentMessages);

        return fulfillmentMessage;
    }
    
    private JsonObject createFulfillmentMessageServiciosOfrecidos() {
        JsonObject fulfillmentMessage = new JsonObject();
        JsonArray fulfillmentMessages = new JsonArray();
    
        JsonArray richContentOuterArray = new JsonArray();
        JsonArray richContentInnerArray = new JsonArray();
    
        String[] servicios = {
            "Medicina General", "Emergencia", "Emergencia Pediátrica", "Odontología General", 
            "Odontología Especializada", "Consulta de Especialidades", "Cirugía", 
            "Coloproctología", "Medicina Interna", "Dermatología", "Pediatría", 
            "Medicina del Trabajo", "Ginecoobstetricia", "Endocrinología", 
            "Fisiatría", "Otorrinolaringología", "Perinatología", "Nefrología"
        };
    
        for (String servicio : servicios) {
            JsonObject listItem = new JsonObject();
            listItem.addProperty("type", "list");
            listItem.addProperty("title", servicio);
            listItem.addProperty("subtitle", "Servicio de " + servicio);
            
            JsonObject eventObject = new JsonObject();
            eventObject.addProperty("name", "");
            eventObject.addProperty("languageCode", "");
            eventObject.add("parameters", new JsonObject());
            
            listItem.add("event", eventObject);
            richContentInnerArray.add(listItem);
            
            JsonObject dividerItem = new JsonObject();
            dividerItem.addProperty("type", "divider");
            richContentInnerArray.add(dividerItem);
        }
        
        // Remove the last divider if not needed
        if (richContentInnerArray.size() > 0) {
            richContentInnerArray.remove(richContentInnerArray.size() - 1);
        }
        
        richContentOuterArray.add(richContentInnerArray);
    
        JsonObject payloadObject = new JsonObject();
        payloadObject.add("richContent", richContentOuterArray);
        
        JsonObject payloadMessage = new JsonObject();
        payloadMessage.add("payload", payloadObject);
        fulfillmentMessages.add(payloadMessage);
    
        fulfillmentMessage.add("fulfillmentMessages", fulfillmentMessages);
        
        return fulfillmentMessage;
    }
    public String extractLocation(Map<String, String> location) {
        // Lista de campos de ubicación en orden de prioridad
        String[] locationFields = {"city", "subadmin-area", "admin-area", "business-name", "street-address"};
        
        for (String field : locationFields) {
            String value = location.get(field);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null; // o considera devolver un valor predeterminado o lanzar una excepción si es necesario
    }
    
}
