package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSendClient mailSendClient;

    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromEmail, String toEmail, String subject, String content ) {

        //실제 메일은 mailSendClient.sendMail에 위임
        boolean result = mailSendClient.sendMail(fromEmail, toEmail, subject, content);

        //메일을 보내고, 히스토리를 저장
        if(result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromEmail(fromEmail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(content)
                    .build()
            );

            mailSendClient.a();
            mailSendClient.b();
            mailSendClient.c();
            //테스트를 할때 mailSendClient 만 mock으로 돌고
            //a,b,c는 정상작동 했으면 좋겠다 -> 이때 Spy를 사용

            return true;
        }
        return false;
    } //가정
    


}
