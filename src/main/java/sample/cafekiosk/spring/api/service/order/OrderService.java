package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<Product> duplicateProducts = findProductsBy(productNumbers);

        //Order
        Order order =  Order.create(duplicateProducts, registeredDataTime);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);

    }

    //product 넘버를 가지고 products를 찾는 메소드로 분리하는 리팩토링
    private List<Product> findProductsBy(List<String> productNumbers) {
        List<Product> products = productRepository.findALlByProductNumberIn(productNumbers);
        Map<String, Product> productMaps= products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(productMaps::get)
                .collect(Collectors.toList());
    }
}
