package sample.cafekiosk.spring.api.service.product;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProdectSellingStatus;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;


/**
 * readOnly = true : 읽기전용
 * CRUD 에서 CUD 동작 X / Only Read
 * JPA : CUD 스냅샷 저장, 변경감지 X (성능향상)
 * CORS - Command / Read 분리하자
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNumberFactory productNumberFactory;

    @Transactional
    public ProductResponse createdProduct(ProductCreateServiceRequest request) {

        String nextProductNumber = productNumberFactory.createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product saveProduct = productRepository.save(product);

        return ProductResponse.of(saveProduct);

    }


    public List<ProductResponse> getSellingProduct() {
        List<Product> productList = productRepository.findAllBySellingStatusIn(ProdectSellingStatus.forDisplay());

        return productList.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

    }

}
