package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    //주문생성
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDataTime) {
        List<String> productNumbers = request.getProductNumbers();
        //product
        List<Product> products = productRepository.findALlByProductNumberIn(productNumbers);

        //Order
        Order order =  Order.create(products, registeredDataTime);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);

    }
}