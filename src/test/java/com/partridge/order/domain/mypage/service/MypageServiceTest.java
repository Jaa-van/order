package com.partridge.order.domain.mypage.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.partridge.order.domain.mypage.dto.MyPageOrderDTO;
import com.partridge.order.domain.mypage.repository.MypageRepository;
import com.partridge.order.global.entity.Order;

@ExtendWith(MockitoExtension.class)
class MypageServiceTest {
	@InjectMocks
	private MypageService mypageService;

	@Mock
	private MypageRepository mypageRepository;

	@Test
	void getMyPageOrderList_should_return_my_page_response_when_user_has_order() {
		//given
		Long userId = 1L;
		List<Order> orderList = List.of(Order.builder()
			.id(2L)
			.userId(1L)
			.key("uuid")
			.totalPrice(5000L)
			.deliveryFee(false)
			.status("001")
			.build());

		when(mypageRepository.findByUserId(userId)).thenReturn(orderList);

		//when
		MyPageOrderDTO.Response response = mypageService.getMyPageOrderList(userId);

		//then
		assertThat(response.getOrders()).hasSize(1);
		assertThat(response.getOrders().get(0).getStatus()).isEqualTo("001");
		assertThat(response.getOrders().get(0).getDeliveryFee()).isEqualTo(0L);
		assertThat(response.getOrders().get(0).getOrderId()).isEqualTo(2L);
	}
	//
	// @Test
	// void getMyPageOrderList_should_return_empty_list_when_user_has_no_order() {
	// 	//given
	//
	// 	//when
	//
	// 	//then
	// }
}