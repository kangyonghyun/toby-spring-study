package springbook.ch5.user.learningtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.ch5.user.learningtest.factorybean.Message;
import springbook.ch5.user.learningtest.factorybean.MessageFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/messageContext.xml")
public class FactoryBeanTest {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("팩토리 빈을 스프링으로 등록 -> 빈 오브젝트 타입 검증")
    void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message).isExactlyInstanceOf(Message.class);
        assertThat(((Message) message).getText()).isEqualTo(Message.newMessage("text").getText());
    }

    @Test
    @DisplayName("팩토리 빈을 스프링으로 등록 -> 빈 오브젝트를 팩토리 빈 타입으로 변환")
    void getFactoryBean() {
        Object message = context.getBean("&message");
        assertThat(message).isExactlyInstanceOf(MessageFactoryBean.class);
    }


}
