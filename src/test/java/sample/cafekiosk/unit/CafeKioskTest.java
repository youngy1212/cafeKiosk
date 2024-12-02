package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

class CafeKioskTest {


    @Test
     void add_manual_test()  {
        CafeKiosk cafeKiosk = new CafeKiosk();

        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료의 수 : "+cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료의 이름 : "+cafeKiosk.getBeverages().get(0).getName());
        //수동 테스트: 결국 사람이 확인해야함. 그리고 어떤게 맞는건지 알 수 없음
        //또한 sysout 하니 무조건 성공하는 테스트임

    }

    //@DisplayName("음료 1개 추가 테스트") //junit5 기능
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
    @Test
    void 음료_1개_추가_테스트(){ //junit5가 없다면 한글메소드도 괜찮다.
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }


    @Test
    void addSeveraBeverages(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        //cafeKiosk.add(americano,2);

        //해피케이스
        //assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        //assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);

        //예외 케이스
        assertThatThrownBy(()-> cafeKiosk.add(americano,0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 한잔 이상 주문하실 수 있습니다.");


    }


    @Test
    void remove(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(cafeKiosk.getBeverages().get(0));
        assertThat(cafeKiosk.getBeverages().isEmpty()).isTrue();
    }

    @Test
    void claer(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());

        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    //TDD 1단계 : RED
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    @Test
    void calculateTotalPrice(){
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        // when
        int tatalPrice = cafeKiosk.calculateTotalPrice();

        // then
        assertThat(tatalPrice).isEqualTo(8500);
    }


    @Disabled
    @Test
    void CreateOrder(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);        Order order = cafeKiosk.createOrder();


        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

        //만약 테스트가 10~22 시가 아니라면 테스트는 실패하게 된다.
        //LocalDateTime.now(); 실행 될때마다 달라짐

    }

    @Test
    void CreateOrderWithCurrentTime(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2024,11,17,10,0));

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");

        //변하는 값이 아닌 내가 넣고싶은 값을 넣을 수 있음. (
    }

    @Test
    void CreateOrderOutSideOpenTime(){
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        //에외 발생
        assertThatThrownBy(()-> cafeKiosk.createOrder(LocalDateTime.of(2024,11,17,9,59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요");

    }



}