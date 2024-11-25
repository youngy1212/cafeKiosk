package sample.cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProdectSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@WebMvcTest(controllers = ProductController.class) //컨트롤러 관련 빈들만 올리는 수 있는 가벼운 어노테이션
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; //직렬화, 역질렬화 json과 오브텍트

    @MockBean
    private ProductService productService;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProdectSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build(); //http는 json형태로 넣어줘야함 역직렬화


        // when // then
        mockMvc.perform(
                    post("/api/v1/product/new")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) //로그확인
                .andExpect(status().isOk());
    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(ProdectSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();


        // when // then
        mockMvc.perform(
                        post("/api/v1/product/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //로그확인
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }



    @DisplayName("신규 상품을 등록할 때 상품 판매상태은 필수값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();


        // when // then
        mockMvc.perform(
                        post("/api/v1/product/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //로그확인
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProdectSellingStatus.SELLING)
                .price(4000)
                .build();


        // when // then
        mockMvc.perform(
                        post("/api/v1/product/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //로그확인
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 가격은 양수값이다.")
    @Test
    void createProductWithZeroPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProdectSellingStatus.SELLING)
                .name("아메리카노")
                .price(0)
                .build();


        // when // then
        mockMvc.perform(
                        post("/api/v1/product/new")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) //로그확인
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 양수여야합니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProduct()).thenReturn(result);
        // when // then
        mockMvc.perform(
                        get("/api/v1/products/selling")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
        //저장 잘되고, 내용은 이미 서비스단에서 했으니, List로 넘어오는지만 검증
    }

}