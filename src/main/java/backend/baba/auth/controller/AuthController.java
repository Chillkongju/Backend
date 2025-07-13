package backend.baba.auth.controller;

import backend.baba.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

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
}