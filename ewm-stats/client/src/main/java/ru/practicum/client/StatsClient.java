package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.StatsInputDTO;
import ru.practicum.StatsOutputDTO;
import ru.practicum.exception.ClientException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class StatsClient {

    private static final String POST_PATCH = "/hit";
    private static final String GET_PATCH = "/stats?start=%s&end=%s&uris=%s&unique=%s";
    private final RestTemplate restTemplate;

    @Autowired
    public StatsClient(@Value("${STATS_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {

        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void postStats(StatsInputDTO inputDTO) {
        if (inputDTO == null) {
            throw new IllegalArgumentException("Stats input data cannot be null");
        }

        try {
            restTemplate.postForLocation(POST_PATCH, inputDTO);
        } catch (HttpStatusCodeException e) {
            throw ClientException.builder().message(e.getMessage()).build();
        }
    }

    public List<StatsOutputDTO> getStats(String start, String end, List<String> uris, boolean unique) {
        String url = String.format(GET_PATCH, start, end, uris, unique);

        try {
            ResponseEntity<StatsOutputDTO[]> response = restTemplate.getForEntity(url, StatsOutputDTO[].class);

            return (response.getBody() != null) ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (HttpStatusCodeException e) {
            throw ClientException.builder().message(e.getMessage()).build();
        }
    }
}
