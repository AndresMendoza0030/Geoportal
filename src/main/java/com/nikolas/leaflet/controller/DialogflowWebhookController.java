import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nikolas.leaflet.domain.ClinicaComunal;
import com.nikolas.leaflet.dto.DialogFlowRequest;
import com.nikolas.leaflet.service.ClinicaComunalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/dialogflow")
public class DialogflowWebhookController {

    @Autowired
    private ClinicaComunalService clinicaComunalService;

    private static final Logger logger = Logger.getLogger(DialogflowWebhookController.class.getName());

    @PostMapping("/webhook")
    public ResponseEntity<JsonObject> handleDialogflowRequest(@RequestBody DialogFlowRequest request) {
        try {
            String intentName = request.getQueryResult().getIntent().getDisplayName();
            logger.info("Intent received: " + intentName);

            if ("buscarClinicasPorMunicipio".equals(intentName)) {
                logger.info("Handling buscarClinicasPorMunicipio intent");
                return handleBuscarClinicasPorMunicipio(request);
            } else if ("buscarHorarioClinica".equals(intentName)) {
                logger.info("Handling buscarHorarioClinica intent");
                return handleBuscarHorarioClinica(request);
            } else {
                logger.warning("No valid intent matched.");
                JsonObject defaultMessage = new JsonObject();
                defaultMessage.addProperty("fulfillmentText", "No valid intent matched.");
                return ResponseEntity.ok(defaultMessage);
            }
        } catch (Exception e) {
            logger.severe("Error handling request: " + e.getMessage());
            e.printStackTrace();
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson);
        }
    }

    private ResponseEntity<JsonObject> handleBuscarClinicasPorMunicipio(DialogFlowRequest request) {
        try {
            Map<String, Object> params = request.getQueryResult().getParameters();
            Map<String, String> location = (Map<String, String>) params.get("location");
            String municipio = extractLocation(location);
            logger.info("Municipio extracted: " + municipio);
            List<ClinicaComunal> clinicas = clinicaComunalService.buscarPorMunicipio(municipio);
            logger.info("Clinicas found: " + clinicas.size());
            JsonObject responseJson = createFulfillmentMessageJson(municipio, clinicas);
            return ResponseEntity.ok().body(responseJson);
        } catch (Exception e) {
            logger.severe("Error in handleBuscarClinicasPorMunicipio: " + e.getMessage());
            e.printStackTrace();
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson);
        }
    }

    private ResponseEntity<JsonObject> handleBuscarHorarioClinica(DialogFlowRequest request) {
        try {
            Map<String, Object> params = request.getQueryResult().getParameters();
            Map<String, String> location = (Map<String, String>) params.get("location");
            String municipio = location.get("business-name");
            logger.info("Business name extracted: " + municipio);
            List<ClinicaComunal> clinicas = clinicaComunalService.findByNombreContaining(municipio);
            logger.info("Clinicas found with business name: " + clinicas.size());
            JsonObject responseJson = createFulfillmentMessageJson2(municipio, clinicas);
            return ResponseEntity.ok().body(responseJson);
        } catch (Exception e) {
            logger.severe("Error in handleBuscarHorarioClinica: " + e.getMessage());
            e.printStackTrace();
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson);
        }
    }

    private String extractLocation(Map<String, String> location) {
        String[] locationFields = {"city", "subadmin-area", "admin-area", "business-name", "street-address"};
        for (String field : locationFields) {
            String value = location.get(field);
            if (value != null && !value.isEmpty()) {
                logger.info("Location field " + field + " extracted with value: " + value);
                return value;
            }
        }
        logger.warning("No location field found with value.");
        return null;
    }

    private JsonObject createFulfillmentMessageJson(String municipio, List<ClinicaComunal> clinicas) {
        JsonObject fulfillmentMessage = new JsonObject();
        JsonArray fulfillmentMessages = new JsonArray();

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
                JsonObject imageObject = new JsonObject();
                imageObject.addProperty("type", "image");
                imageObject.addProperty("rawUrl", "https://pbs.twimg.com/media/FnakFq3WQAcWDUk?format=jpg&name=4096x4096");
                imageObject.addProperty("accessibilityText", "Imagen de la clínica " + clinica.getNombre());
                richContentInnerArray.add(imageObject);

                JsonObject infoObject = new JsonObject();
                infoObject.addProperty("type", "info");
                infoObject.addProperty("title", clinica.getNombre());
                infoObject.addProperty("subtitle", "Dirección: " + clinica.getDireccion());
                infoObject.addProperty("actionLink", "https://www.google.com/maps/dir//JVV5%2BJW5,+C.+Alberto+Masferrer,+San+Salvador/@13.6439933,-89.2225477,12z/data=!4m8!4m7!1m0!1m5!1m1!1s0x8f63368504511375:0x746ee51e341edef8!2m2!1d-89.1401494!2d13.6439782?entry=ttu" + clinica.getId());
                richContentInnerArray.add(infoObject);

                JsonObject dividerObject = new JsonObject();
                dividerObject.addProperty("type", "divider");
                richContentInnerArray.add(dividerObject);
            }

            if (clinicas.size() > 0) {
                richContentInnerArray.remove(richContentInnerArray.size() - 1);
            }

            richContentOuterArray.add(richContentInnerArray);

            JsonObject payloadObject = new JsonObject();
            payloadObject.add("richContent", richContentOuterArray);

            JsonObject payloadMessage = new JsonObject();
            payloadMessage.add("payload", payloadObject);

            fulfillmentMessages.add(payloadMessage);

            fulfillmentMessage.add("fulfillmentMessages", fulfillmentMessages);
        }
        return fulfillmentMessage;
    }

    private JsonObject createFulfillmentMessageJson2(String municipio, List<ClinicaComunal> clinicas) {
        JsonObject fulfillmentMessage = new JsonObject();
        JsonArray fulfillmentMessages = new JsonArray();

        JsonObject textObject = new JsonObject();
        JsonArray textArray = new JsonArray();

        if (clinicas.isEmpty()) {
            textArray.add("No se encontraron clínicas en el municipio especificado.");
        } else {
            textArray.add("Aquí tienes los horarios ofrecidos: ");
            for (ClinicaComunal clinica : clinicas) {
                String clinicInfo = clinica.getNombre() + " - Horario lunes a viernes: " + clinica.getHorarioInicioSemana().replace("\"", "\\\"") + " a " + clinica.getHorarioFinSemana().replace("\"", "\\\"");
                textArray.add(clinicInfo);
                if (clinica.getHorarioInicioFinde() == null || clinica.getHorarioInicioFinde().isEmpty()) {
                    String clinicInfo2 = clinica.getNombre() + " - Horario sábado y domingo: Cerrado";
                    textArray.add(clinicInfo2);
                } else {
                    String clinicInfo2 = clinica.getNombre() + " - Horario sábado y domingo: " + clinica.getHorarioInicioFinde().replace("\"", "\\\"") + " a " + clinica.getHorarioFinFinde().replace("\"", "\\\"");
                    textArray.add(clinicInfo2);
                }
            }
        }

        textObject.add("text", textArray);
        JsonObject singleMessage = new JsonObject();
        singleMessage.add("text", textObject);
        fulfillmentMessages.add(singleMessage);
        fulfillmentMessage.add("fulfillmentMessages", fulfillmentMessages);

        return fulfillmentMessage;
    }
}
