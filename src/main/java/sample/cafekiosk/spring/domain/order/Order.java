package sample.cafekiosk.spring.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.BaseEntity;
import sample.cafekiosk.spring.domain.product.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private int totalPrice;

    private LocalDateTime registeredDataTime;

    //하나의 order는 여러개의 orderProuct를 가질 수 있음.
    //연관관계의 주인은 orderProuct의 order임 (외래키 order_id orderProuct에서 관리)
    //order 엔티티가 수정되면 orderProduct도 함께 수행됨
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProduct = new ArrayList<>();

    public Order(List<Product> products, LocalDateTime registeredDataTime){ //테스트가 필요함 단위테스트 TDD
        this.status = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDataTime = registeredDataTime;
        this.orderProduct = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    private int calculateTotalPrice(List<Product> products) { //그린 TDD 리팩토링
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

    public static Order create(List<Product> products, LocalDateTime registeredDataTime) {
        return new Order(products, registeredDataTime);
    }
}
