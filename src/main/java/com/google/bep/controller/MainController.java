package com.google.bep.controller;

import com.google.bep.dto.ResponseMissionDTO;
import com.google.bep.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    // @AuthenticationPrincipal가 null인 이유는 내 코드의 TokenProvider의 getAuthentication 메서드가 DB에서 유저정보를 가져오는 것이 아님.
    @GetMapping("")
    public ResponseEntity<List<ResponseMissionDTO>> hasUser(Authentication authentication) {
        UserDetails account = (UserDetails) authentication.getPrincipal();  // UserDetails 객체 반환
        return ResponseEntity.ok(mainService.getMissions(account.getUsername()));   // getUsername이 반환하는 email 반환
    }
}
