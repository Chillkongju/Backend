package backend.baba.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProfileRequestDto {
    private String name;
    private String profileImageUrl;
    private String bio;
    private String preference;
    private String link;
}