package com.nikolas.leaflet.controller;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class ChatBotController {

    @PostMapping("/chatbot")
    public String getBotResponse(@RequestBody UserInput input) {
        String userInput = input.getText();
        try (SessionsClient sessionsClient = createSessionsClient()) {
            String sessionId = "some-session-id";
            SessionName session = SessionName.of("geoagente-vryp", sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(userInput).setLanguageCode("es-ES");
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
            QueryResult response = sessionsClient.detectIntent(session, queryInput).getQueryResult();
            return response.getFulfillmentText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred in Dialogflow session";
        }
    }

   private SessionsClient createSessionsClient() throws IOException {
    InputStream credentialsStream = getClass().getResourceAsStream("/credenciales.json");
    GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
    SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
        .build();
    return SessionsClient.create(sessionsSettings);
}
    static class UserInput {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
