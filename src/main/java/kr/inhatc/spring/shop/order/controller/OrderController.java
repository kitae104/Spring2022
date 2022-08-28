package kr.inhatc.spring.shop.order.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.inhatc.spring.shop.order.dto.OrderDto;
import kr.inhatc.spring.shop.order.dto.OrderHistDto;
import kr.inhatc.spring.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController
{
	private final OrderService orderService;

	/**
	 * 상품 주문에서 웹 페이지 새로 고침 없이 서버에 주문을 요청하기 위해서 비동기 방식 사용
	 * @RequestBody : HTTP 요청의 본문 body에 담긴 내용을 자바 객체로 전달 
	 * @ResponseBody : 자바 객체를 HTTP 요청의 body로 전달  
	 *  
	 * @param orderDto
	 * @param bindingResult
	 * @param principal
	 * @return
	 */
	@PostMapping(value = "/order")
	public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, 
			BindingResult bindingResult, Principal principal)
	{

		// 데이터 바인딩 시 에러가 있는지 확인
		if (bindingResult.hasErrors())
		{
			StringBuilder sb = new StringBuilder();
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError fieldError : fieldErrors)
			{
				sb.append(fieldError.getDefaultMessage());
			}
			// 에러 정보를 ResponseEntity에 담아서 반환
			return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
		}
		
		String email = principal.getName();		// 현재 로그인 한 회원의 email 정보 
		Long orderId;
		
		try
		{
			// 주문 정보와 이메일 정보를 이용하여 주문 로직 호출
			orderId = orderService.order(orderDto, email);	
		} 
		catch (Exception e)
		{
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		// 결과값으로 생성된 주문 번호와 요청이 성공했다는 응답 상태 코드 반환  
		return new ResponseEntity<Long>(orderId, HttpStatus.OK);
	}
	
	@GetMapping(value = {"/orders", "/orders/{page}"})
	public String orderHistory(@PathVariable("page") Optional<Integer> page, 
			Principal principal, Model model)
	{
		
		log.info("==============> 구매 이력 페이지 이동");
		// 한 번에 가지고 올 주문의 개수는 4개 
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get(): 0, 4);
		Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
		
		model.addAttribute("orders", orderHistDtoList);
		model.addAttribute("page", pageable.getPageNumber());
		model.addAttribute("maxPage", 5);
		return "order/orderHistory";
	}
	
}
