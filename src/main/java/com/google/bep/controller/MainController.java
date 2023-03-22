package com.google.bep.controller;

import com.google.bep.dto.DonationDTO;
import com.google.bep.dto.ResponseDetailDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Main", description = "퀴즈 api 입니다.")
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @Operation(summary = "퀴즈 할당", description = "main 페이지 요청시 user_mission 테이블의 데이터 개수가 3개 이하면 필요한 개수만큼 미션을 할당합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "미션 응답 완료", content = @Content(schema = @Schema(implementation = ResponseMissionDTO.class))),
            @ApiResponse(responseCode = "403", description = "유효하지 않은 JWT", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    // @AuthenticationPrincipal가 null인 이유는 내 코드의 TokenProvider의 getAuthentication 메서드가 DB에서 유저정보를 가져오는 것이 아님.
    @GetMapping("")
    public ResponseEntity<List<ResponseMissionDTO>> main(Authentication authentication) throws Exception {
        UserDetails account = (UserDetails) authentication.getPrincipal();  // UserDetails 객체 반환
        List<ResponseMissionDTO> response = mainService.getMissions(account.getUsername()); // getUsername이 반환하는 email로 로그인한 유저의 미션 불러오기
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "미션 완료", description = "미션이 완료되면 해당 미션의 정보를 보내주고, 유저 포인트 적립, 미션 1개 새로 할당")
    @ApiResponse(responseCode = "200", description = "요청 처리 완료", content = @Content(schema = @Schema(implementation = ResponseDetailDTO.class)))
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailDTO> missionDetail(Authentication authentication, @PathVariable(value = "id") Long missionId) {
        UserDetails account = (UserDetails) authentication.getPrincipal();
        ResponseDetailDTO response = mainService.completeMission(account.getUsername(), missionId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "기부 api", description = "해당 카테고리에 로그인 유저의 포인트를 기부하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 처리 완료"),
            @ApiResponse(responseCode = "500", description = "유저의 포인트가 기부할 포인트보다 적을 경우")
    })
    @PostMapping("/donation")
        public String donatePoint (Authentication authentication, @RequestBody DonationDTO donationDTO) throws Exception {
        UserDetails account = (UserDetails) authentication.getPrincipal();
        int userPoint = mainService.donate(account.getUsername(), donationDTO);
        return userPoint+" 포인트 남았습니다.";
    }

    @Operation(summary = "카테고리별 기부금액 api", description = "카테고리별 기부된 총금액 LIST 출력")
    @ApiResponse(responseCode = "200", description = "요청 처리 완료", content = @Content(schema = @Schema(implementation = DonationDTO.class)))
    @GetMapping("/donations")
    public ResponseEntity<List<DonationDTO>> donationList () {
        return ResponseEntity.ok(mainService.getDonations());
    }
}
