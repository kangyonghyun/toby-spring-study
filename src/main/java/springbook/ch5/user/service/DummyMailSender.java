package springbook.ch5.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Slf4j
public class DummyMailSender implements MailSender {
    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        log.info("테스트 스텁 : 메일 전송 완료");
    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages) throws MailException {
        log.info("테스트 스텁 : 메일 전송 완료");
    }
}
