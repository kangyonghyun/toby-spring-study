package springbook.ch5.user.learningtest;

import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.ch5.user.learningtest.proxyfactorybean.UppercaseAdvice;
import springbook.ch5.user.learningtest.reflection.Hello;
import springbook.ch5.user.learningtest.reflection.HelloTarget;

import static org.assertj.core.api.Assertions.assertThat;

public class PointcutTest {

    @Test
    void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        checkAdvice(new HelloTarget(), classMethodPointcut, true);
        class HelloWord extends HelloTarget{};
        checkAdvice(new HelloWord(), classMethodPointcut, false);
        class HelloToby extends HelloTarget{};
        checkAdvice(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdvice(Object target, Pointcut pointcut, boolean isAdvice) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello hello = (Hello) pfBean.getObject();

        if (isAdvice) {
            assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
            assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
            assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank you Toby");
        } else {
            assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
            assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
            assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank you Toby");
        }
    }
}
