package sample.cafekiosk.spring.api.controller.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.ProdectSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.") //타입이라 Notnull
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProdectSellingStatus sellingStatus;

    // String name -> 상품 이름은 20자 제한
    @NotBlank(message = "상품 이름은 필수입니다.") // "" "  " 둘다 안 됨
    //Max(20)
    private String name;

    @Positive(message = "상품 가격은 양수여야합니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType type, ProdectSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
