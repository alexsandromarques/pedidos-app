package com.aom.client;

import com.aom.model.Pedido;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

public class PedidoClient {

    private static final String ENDPOINT = "http://localhost:8080/api/pedidos";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PedidoClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public UUID enviar(Pedido pedido) {
        try {
            String json = objectMapper.writeValueAsString(pedido);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 202) {
                Map<String, Object> resposta = objectMapper.readValue(response.body(), Map.class);
                String idString = (String) resposta.get("id");
                UUID id = UUID.fromString(idString);
                System.out.println("Pedido enviado com sucesso: " + id);
                return id;
            } else {
                System.err.println("Erro ao enviar pedido: HTTP " + response.statusCode());
                System.err.println("Resposta: " + response.body());
                throw new IllegalStateException("Erro ao enviar pedido: HTTP " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Falha na comunicação com o servidor:");
            e.printStackTrace();
            throw new RuntimeException("Erro ao enviar pedido: " + e.getMessage(), e);
        }
    }

    public String status(UUID id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT + "/status/" + id))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String status = response.body().replace("\"", "");
                System.out.println("Status consultado: " + status);
                return status;
            } else {
                System.err.println("Erro ao consultar status: HTTP " + response.statusCode());
                return "Indisponível";
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao consultar status do pedido " + id);
            e.printStackTrace();
            return "Erro ao consultar status";
        }
    }
}
