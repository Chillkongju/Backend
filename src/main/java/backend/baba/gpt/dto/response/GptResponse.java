package backend.baba.gpt.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class GptResponse {
    private List<Choice> choices;

    @Getter
    @NoArgsConstructor
    public static class Choice {
        private Message message;
    }

    @Getter
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
