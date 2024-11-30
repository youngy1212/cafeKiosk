package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;


@RequiredArgsConstructor
@Service
public class OrderStatisticsService {
    //이런 긴작업이 있는 서비스에서는 트랜잭션을 걸지않는게 좋음
    //어차피 데이터 조회해올 땐 레포단에서 걸릴것임!

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        //해당 일자의 결제완료된 주문을 가져와서
        List<Order> orders = orderRepository.findOrdersBy(
            orderDate.atStartOfDay(), //0시부터 다음날 0시까지
            orderDate.plusDays(1).atStartOfDay(),
            OrderStatus.PAYMENT_COMPLETED
        );
        //물론 이 시간은 결제시간과 주문 시간은 차이가 있으나, 임시로 주문시간 사용

        //총 매출 합계를 계산하고
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        //메일 전송
        boolean result = mailService.sendMail(
                "no-reply@cafekiosk"
                , email
                , String.format("[매출통게] %s", orderDate )
                , String.format("총 매출 합계는 %s원 입니다.", totalAmount)
        );

        if(!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }

        return true;
    }
}
