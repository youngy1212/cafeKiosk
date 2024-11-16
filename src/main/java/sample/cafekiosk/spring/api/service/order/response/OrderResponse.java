package sample.cafekiosk.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDataTime;
    private List<ProductResponse> products;
}
