package com.partridge.order.context.mypage.service;

import static com.partridge.order.global.util.DeliveryUtil.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.partridge.order.context.mypage.dto.MyPageOrderDTO;
import com.partridge.order.context.mypage.repository.MypageRepository;
import com.partridge.order.context.order.domain.model.Order;
import com.partridge.order.global.logger.Log;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {
	private final MypageRepository mypageRepository;

	@Log
	public MyPageOrderDTO.Response getMyPageOrderList(Long userId) {
		return myPageResponseBuilder(Optional.of(mypageRepository.findByUserId(userId))
			.map(this::myPageOrderBuilder)
			.orElse(List.of(MyPageOrderDTO.MyPageResponseOrder.builder().build())));
	}

	private List<MyPageOrderDTO.MyPageResponseOrder> myPageOrderBuilder(List<Order> orders) {
		return orders.stream().map(order -> MyPageOrderDTO.MyPageResponseOrder.builder()
				.orderId(order.getId())
				.key(order.getKey())
				.totalPrice(order.getTotalPrice())
				.deliveryFee(caculateDeliveryFee(order.getDeliveryFee()))
				.status(order.getStatus())
				.build())
			.toList();
	}

	private MyPageOrderDTO.Response myPageResponseBuilder(
		List<MyPageOrderDTO.MyPageResponseOrder> myPageResponseOrder) {
		return MyPageOrderDTO.Response.builder()
			.orders(myPageResponseOrder)
			.totalCount((long)myPageResponseOrder.size())
			.build();
	}
}
