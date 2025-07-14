package backend.baba.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "사용자 아이디", example = "testuser")
    private String username;

    @Schema(description = "비밀번호", example = "password123")
    private String password;
}