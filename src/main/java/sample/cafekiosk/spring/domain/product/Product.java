package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProdectSellingStatus sellingStatus;

    private String name;

    private int price;

    @Builder
    public Product(String name, int price, String productNumber, ProdectSellingStatus sellingStatus, ProductType type) {
        this.name = name;
        this.price = price;
        this.productNumber = productNumber;
        this.sellingStatus = sellingStatus;
        this.type = type;
    }
}


