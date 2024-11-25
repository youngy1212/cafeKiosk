package sample.cafekiosk.spring.api.controller.product;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/product/new")
    public ApiResponse<ProductResponse> createdProduct(@Valid @RequestBody ProductCreateRequest request){
        return ApiResponse.ok(productService.createdProduct(request.toServiceRequest()));
    }

    @GetMapping("api/v1/products/selling")
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.ok(productService.getSellingProduct());
    }


}
