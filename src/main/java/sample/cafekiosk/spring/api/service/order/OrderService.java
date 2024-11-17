package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    //주문생성

    /**
     * 재고 감소 -> 동시성 문제
     * optimistic lock / pessimistic lock / ..
     */
    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDataTime) {
        List<String> productNumbers = request.getProductNumbers();
        //product
        List<Product> products = findProductsBy(productNumbers);

        deductStockQuantities(products);

        //Order
        Order order =  Order.create(products, registeredDataTime);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);

    }

    private void deductStockQuantities(List<Product> products) {

        List<String> stockProductNumbers = extractStockProductNumbers(products);

        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);
        Map<String, Long> procuctCountingMap = createCountingMapBy(stockProductNumbers);

        //재고 차감 시도
        for(String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = procuctCountingMap.get(stockProductNumber).intValue();

            if(stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }

            stock.deductQuantity(quantity);

        }
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

    //재고차감 체크가 필요한 상품들 filter
    private static List<String> extractStockProductNumbers(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.containsStockType(product.getType()))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
    }

    //재고 엔티티 조회 Map반환 (중복 문제)
    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        return stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, s -> s));
    }
    //상품별 counting
    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
    }

}
