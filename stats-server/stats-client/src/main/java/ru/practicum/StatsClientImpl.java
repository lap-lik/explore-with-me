package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.exception.ClientException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class StatsClientImpl implements StatsClient {

    private static final String POST_PATCH = "/hit";
    private static final String GET_PATCH = "/stats";
    private RestTemplate restTemplate;

    @Autowired
    public StatsClientImpl(@Value("${stats-service.url}") String serverUrl) {

        restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    @Override
    public List<StatsDtoOut> getStats(String start, String end, List<String> uris, boolean unique) {

        String urisParam = uris.isEmpty() ? "" : "&uris=" + String.join(",", uris);
        String url = String.format("%s?start=%s&end=%s%s&unique=%s", GET_PATCH, start, end, urisParam, unique);

        try {
            ResponseEntity<StatsDtoOut[]> response = restTemplate.getForEntity(url, StatsDtoOut[].class);

            return (response.getBody() != null) ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (HttpStatusCodeException e) {
            throw ClientException.builder().message(e.getMessage()).build();
        }
    }

    @Override
    public void postStats(StatsDtoIn inputDTO) {
        if (inputDTO == null) {
            throw new IllegalArgumentException("Stats input data cannot be null");
        }

        try {
            restTemplate.postForLocation(POST_PATCH, inputDTO);
        } catch (HttpStatusCodeException e) {
            throw ClientException.builder().message(e.getMessage()).build();
        }
    }
}
