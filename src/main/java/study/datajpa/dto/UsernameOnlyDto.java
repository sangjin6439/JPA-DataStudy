package study.datajpa.dto;

import lombok.Getter;

@Getter
public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(final String username) {
        this.username = username;
    }
}