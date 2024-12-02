package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@RequiredArgsConstructor
@Component
public class ProductNumberFactory {

    private final ProductRepository productRepository;

    //동시성 이슈
    public String createNextProductNumber() {
        //productNumber
        //001, 002 , 003
        //DB에서 마지막 저장된 prodect의 상품 번호를 읽어와서 +1
        String lastProductNumber = productRepository.findLatestProduct();
        if(lastProductNumber == null) {
            return "001";
        }
        //nextProductNumber
        int lastProductNumberInt = Integer.parseInt(lastProductNumber);
        int NextProductNumberInt = lastProductNumberInt + 1;

        return String.format("%03d", NextProductNumberInt);
    }

}
