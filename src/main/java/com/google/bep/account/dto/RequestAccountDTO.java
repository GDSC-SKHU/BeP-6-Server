package com.google.bep.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestAccountDTO {
    @Schema(description = "이메일", example = "googletest@gamil.com")
    private String email;

    @Schema(description = "이름", example = "김짱구")
    private String name;

    @Schema(description = "비밀번호", example = "00000000")
    private String password;

    @Schema(description = "유저 회원가입 정보 제공자", example = "google 또는 bep")
    private String provider;
}
