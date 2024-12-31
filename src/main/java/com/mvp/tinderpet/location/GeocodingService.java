package com.mvp.tinderpet.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeocodingService {

    @Value("${tomtom.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.tomtom.com/search/2/geocode/";

    private final RestTemplate restTemplate;

    @Autowired
    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double[] geocode(String endereco) {
        String url = BASE_URL + endereco + ".json?key=" + apiKey;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return parseCoordinates(response.getBody());

            } else {
                throw new RuntimeException("Erro na requisição: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o endereço: " + e.getMessage(), e);
        }
    }

    // Método para extrair latitude e longitude da resposta JSON
    private double[] parseCoordinates(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode position = rootNode.path("results").get(0).path("position");

            double latitude = position.path("lat").asDouble();
            double longitude = position.path("lon").asDouble();

            return new double[]{latitude, longitude};
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a resposta JSON: " + e.getMessage(), e);
        }
    }
}