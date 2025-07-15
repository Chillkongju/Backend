package backend.baba.member.controller;

import backend.baba.member.domain.Member;
import backend.baba.member.dto.MemberInfoResponseDto;
import backend.baba.member.dto.UpdateProfileRequestDto;
import backend.baba.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponseDto> getMyInfo(HttpServletRequest request) {
        Member member = (Member) request.getSession().getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인되지 않은 상태
        }

        MemberInfoResponseDto dto = memberService.getMyInfo(member.getUsername());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/me")
    public ResponseEntity<String> updateMyInfo(@RequestBody UpdateProfileRequestDto dto,
                                               HttpServletRequest request) {
        // 세션에서 로그인된 사용자 가져오기
        Member member = (Member) request.getSession().getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 회원 정보 수정
        memberService.updateProfile(member.getUsername(), dto);

        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }


}
