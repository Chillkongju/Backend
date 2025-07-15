package backend.baba.member.service;

import backend.baba.member.domain.Member;
import backend.baba.member.dto.MemberInfoResponseDto;
import backend.baba.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(String username, String name, String rawPassword) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Member member = Member.builder()
                .username(username)
                .name(name)
                .password(passwordEncoder.encode(rawPassword))
                .build();

        memberRepository.save(member);
    }

    public boolean isUsernameDuplicate(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    public MemberInfoResponseDto getMyInfo(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new MemberInfoResponseDto(member);
    }
}