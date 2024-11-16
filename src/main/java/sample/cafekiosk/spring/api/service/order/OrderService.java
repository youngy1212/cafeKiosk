package sample.cafekiosk.spring.api.service.order;

import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;

@Service
public class OrderService {

    public OrderResponse createOrder(OrderCreateRequest request) {
     //주문을 생성
        return null;
    }
}
