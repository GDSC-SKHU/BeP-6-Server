package com.google.bep.controller;

import com.google.bep.dto.ResponseMissionDTO;
import com.google.bep.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Main", description = "퀴즈 api 입니다.")
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @Operation(summary = "퀴즈 할당", description = "main 페이지 요청시 user_mission 테이블의 데이터 개수가 3개 이하면 미션을 할당합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "미션 응답 완료", content = @Content(schema = @Schema(implementation = ResponseMissionDTO.class))),
            @ApiResponse(responseCode = "403", description = "유효하지 않은 JWT", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    // @AuthenticationPrincipal가 null인 이유는 내 코드의 TokenProvider의 getAuthentication 메서드가 DB에서 유저정보를 가져오는 것이 아님.
    @GetMapping("")
    public ResponseEntity<List<ResponseMissionDTO>> main(Authentication authentication) {
        UserDetails account = (UserDetails) authentication.getPrincipal();  // UserDetails 객체 반환
        return ResponseEntity.ok(mainService.getMissions(account.getUsername()));   // getUsername이 반환하는 email 반환
    }
}
