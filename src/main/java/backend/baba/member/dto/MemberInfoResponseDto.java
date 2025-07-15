package backend.baba.member.dto;


import backend.baba.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberInfoResponseDto {

    private String username;
    private String name;
    private String profileImageUrl;
    private String bio;
    private String preference;
    private String link;

    public MemberInfoResponseDto(Member member) {
        this.username = member.getUsername();
        this.name = member.getName();
        this.profileImageUrl = member.getProfileImageUrl();
        this.bio = member.getBio();
        this.preference = member.getPreference();
        this.link = member.getLink();
    }
}