package com.partridge.order.context.mypage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.partridge.order.context.mypage.dto.MyPageOrderDTO;
import com.partridge.order.context.mypage.service.MypageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/my-page")
@RequiredArgsConstructor
public class MypageController {
	private final MypageService mypageService;

	@GetMapping("/orders/{userId}")
	public ResponseEntity<MyPageOrderDTO.Response> getMyPageOrderList(@PathVariable Long userId) {
		return ResponseEntity.ok().body(mypageService.getMyPageOrderList(userId));
	}

}
