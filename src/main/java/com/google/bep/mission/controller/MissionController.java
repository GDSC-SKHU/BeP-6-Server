package com.google.bep.mission.controller;

import com.google.bep.account.domain.model.Account;
import com.google.bep.error.dto.ResponseError;
import com.google.bep.jwt.domain.UserDetailsImpl;
import com.google.bep.mission.dto.ResponseDetailDTO;
import com.google.bep.mission.dto.ResponseMissionDTO;
import com.google.bep.mission.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Main", description = "미션 api 입니다.")
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MissionController {
    private final MissionService missionService;

    @Operation(summary = "퀴즈 할당", description = "main 페이지 요청시 user_mission 테이블의 데이터 개수가 3개 이하면 필요한 개수만큼 미션을 할당합니다.")
    @ApiResponse(responseCode = "200", description = "미션 응답 완료", content = @Content(schema = @Schema(implementation = ResponseMissionDTO.class)))
    @GetMapping("")
    public ResponseEntity<List<ResponseMissionDTO>> main(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Account account = userDetails.getAccount();  // UserDetails 객체 반환
        List<ResponseMissionDTO> response = missionService.getMissions(account); // getUsername이 반환하는 email로 로그인한 유저의 미션 불러오기
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "미션 완료", description = "미션이 완료되면 해당 미션의 정보를 보내주고, 유저 포인트를 적립합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 처리 완료", content = @Content(schema = @Schema(implementation = ResponseDetailDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 미션 id 요청", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "404", description = "유저가 할당받은 미션이 아닙니다.", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("/{missionId}")
    public ResponseEntity<ResponseDetailDTO> missionDetail(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable(value = "missionId") Long missionId) {
        Account account = userDetails.getAccount();
        ResponseDetailDTO response = missionService.completeMission(account, missionId);
        return ResponseEntity.ok(response);
    }
}
