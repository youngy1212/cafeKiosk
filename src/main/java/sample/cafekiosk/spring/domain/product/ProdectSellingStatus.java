package sample.cafekiosk.spring.domain.product;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProdectSellingStatus {

    SELLING("판매중"),
    HOLD("판매보류"),
    STOP_SELLING("판매중지");

    private final String name;


    public static List<ProdectSellingStatus> forDisplay() {
        return List.of(SELLING, HOLD);
    }

}
