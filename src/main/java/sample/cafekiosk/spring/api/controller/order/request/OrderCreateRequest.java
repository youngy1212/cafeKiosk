package sample.cafekiosk.spring.api.controller.order.request;

import java.util.List;
import lombok.Builder;

public class OrderCreateRequest {

    private List<String> productNumbers;

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
