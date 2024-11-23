package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.product.ProdectSellingStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
public class ProductCreateRequest {

    private String productNumber;
    private ProductType type;
    private ProdectSellingStatus sellingStatus;
    private String name;
    private int price;

    @Builder
    public ProductCreateRequest( String name, int price, ProdectSellingStatus sellingStatus,
                                ProductType type) {
        this.name = name;
        this.price = price;
        this.sellingStatus = sellingStatus;
        this.type = type;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .name(name)
                .sellingStatus(sellingStatus)
                .price(price)
                .build();
    }
}
