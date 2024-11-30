package sample.cafekiosk.spring.api.service.mail;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class) //우리 Mock사용해서 클래스 만든다고 고지
class MailServiceTest {

    //어노테이션으로 리팩토링
    //@Mock
    @Spy
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;


    //단위 테스트에서 Mocking 테스트
    @DisplayName("메일 전송 테스트")
    @Test
    void test() {
        // given
        //MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
        //MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);
        //MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

//        when(mailSendClient.sendMail(anyString(),anyString(),anyString(),anyString()))
//                .thenReturn(true);
        //그럼sendMail 안의 mailSendHistoryRepository.save()는? -> 먹은 기본적으로 데이터가 없으면 기본값을 던져서 해결함
        //spy는 실제 객체 기반으로 가져오려해서 when 사용 X

        doReturn(true).when(mailSendClient)
                .sendMail(anyString(),anyString(),anyString(),anyString());

        // when
        boolean result = mailService.sendMail("", "", "", "");

        //좀 더 명확하게 전달하고 싶은 경우
        verify(mailSendHistoryRepository,times(1)).save(any(MailSendHistory.class));
        //mailSendHistoryRepository가 한번 불렸는지? MailSendClient가 불렸을 때

        // then
        assertThat(result).isTrue();

    }

}