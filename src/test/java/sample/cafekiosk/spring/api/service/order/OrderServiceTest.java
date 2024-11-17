package sample.cafekiosk.spring.api.service.order;
import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProdectSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductrRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductrRepository orderProductrRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() { //테스트 둘다 테스트 할 경우 List 조회해서 map어 넣어줄때 키 중복 오류가 생김
        //테스트가 끝날때마다 삭제
        orderProductrRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();

    }


    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Product product = createProduct(HANDMADE,"001", 1000);
        Product product2 = createProduct(HANDMADE,"002", 3000);
        Product product3 = createProduct(HANDMADE,"003", 5000);
        productRepository.saveAll(List.of(product, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request,now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDataTime", "totalPrice")
                .contains(now, 4000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",1000),
                        Tuple.tuple("002",3000)
                );

    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Product product = createProduct(HANDMADE,"001", 1000);
        Product product2 = createProduct(HANDMADE,"002", 3000);
        Product product3 = createProduct(HANDMADE,"003", 5000);
        productRepository.saveAll(List.of(product, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request,now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDataTime", "totalPrice")
                .contains(now, 2000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber","price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001",1000),
                        Tuple.tuple("001",1000)
                );

    }

    //그냥 빌더패턴을 사용하면 너무 길어서 따로 뻄
    //테스트에 필요한 데이터만 파라미터로 받고 나머지는 기본 값으로 채우기
    private Product createProduct(ProductType type
    ,String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }
}