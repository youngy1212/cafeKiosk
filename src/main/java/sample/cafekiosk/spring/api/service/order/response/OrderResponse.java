package sample.cafekiosk.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDataTime;
    private List<ProductResponse> products;

    @Builder
    public OrderResponse(LocalDateTime registeredDataTime, Long id, List<ProductResponse> products, int totalPrice) {
        this.registeredDataTime = registeredDataTime;
        this.id = id;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDataTime(order.getRegisteredDataTime())
                .products(order.getOrderProduct().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }
}
