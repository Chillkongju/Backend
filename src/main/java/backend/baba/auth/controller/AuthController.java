package backend.baba.auth.controller;

import backend.baba.auth.dto.LoginRequestDto;
import backend.baba.auth.service.AuthService;
import backend.baba.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam String username,
                                         @RequestParam String name,
                                         @RequestParam String password,
                                         @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        memberService.signup(username, name, password);
        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean isDuplicate = memberService.isUsernameDuplicate(username);
        return ResponseEntity.ok(isDuplicate);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
        try {
            authService.login(requestDto);
            return ResponseEntity.ok("로그인 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 서버 세션 무효화 (로그아웃 처리)
        request.getSession().invalidate();
        return ResponseEntity.ok("로그아웃 완료");
    }

}