package com.google.bep.donation.controller;

import com.google.bep.account.domain.model.Account;
import com.google.bep.donation.Service.DonationService;
import com.google.bep.donation.domain.model.Donation;
import com.google.bep.donation.dto.RequestDonationDTO;
import com.google.bep.error.dto.ResponseError;
import com.google.bep.jwt.domain.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Donation", description = "기부 api 입니다.")
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class DonationController {
    private final DonationService donationService;

    @Operation(summary = "기부 api", description = "해당 카테고리에 로그인 유저의 포인트를 기부하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "갱신된 유저 포인트 Integer 타입으로 반환", content = @Content),
            @ApiResponse(responseCode = "400", description = "옳지 않은 기부포인트를 선택했을 경우", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 기부 카테고리 id", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping("/donations/{donationId}")
    public ResponseEntity<Integer> donateUserPoint(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable(value = "donationId") Long donationId,
                                               @RequestBody RequestDonationDTO donationDTO) {
        Account account = userDetails.getAccount();
        int userPoint = donationService.donate(account, donationDTO, donationId);
        return ResponseEntity.ok(userPoint);
    }

    @Operation(summary = "카테고리별 기부금액 api", description = "카테고리별 기부된 총금액 LIST 출력")
    @ApiResponse(responseCode = "200", description = "요청 처리 완료", content = @Content(schema = @Schema(implementation = Donation.class)))
    @GetMapping("/donations")
    public ResponseEntity<List<Donation>> getDonationList() {
        return ResponseEntity.ok(donationService.findAll());
    }
}
