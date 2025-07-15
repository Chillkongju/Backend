package backend.baba.gpt.service;

import backend.baba.gpt.dto.response.GptResponse;
import backend.baba.gpt.dto.request.GptRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;

    public String askToGpt(String userPrompt) {
        GptRequest.Message message = GptRequest.Message.builder()
                .role("user")
                .content(userPrompt)
                .build();

        GptRequest request = GptRequest.builder()
                .model(model)
                .messages(Collections.singletonList(message))
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<GptRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GptResponse> response = restTemplate.postForEntity(apiUrl, entity, GptResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getChoices().get(0).getMessage().getContent();
        } else {
            throw new RuntimeException("GPT API 호출 실패: " + response.getStatusCode());
        }
    }
}
