package springbook.ch5.user.learningtest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.ch5.user.learningtest.reflection.Hello;
import springbook.ch5.user.learningtest.reflection.HelloTarget;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyFactoryBeanTest {
    
    @Test
    @DisplayName("다이내믹 프록시를 이용한 프록시 생성 테스트")
    void simpleProxy() {
        Hello hello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        assertThat(hello.sayHello("toby")).isEqualTo("HELLO TOBY");
        assertThat(hello.sayHi("toby")).isEqualTo("Hi toby");
        assertThat(hello.sayThankYou("toby")).isEqualTo("Thank you toby");
    }

    static class UppercaseHandler implements InvocationHandler {
        private final Hello hello;
        public UppercaseHandler(Hello hello) {
            this.hello = hello;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object helloReturn = method.invoke(hello, args);
            if (helloReturn instanceof String && method.getName().startsWith("sayHello")) {
                return  ((String) helloReturn).toUpperCase();
            } else {
                return helloReturn;
            }
        }
    }

    @Test
    @DisplayName("스프링 다이내믹 프록시를 이용한 프록시 생성 테스트")
    void proxyFactoryBean() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());

        Hello hello = (Hello) proxyFactoryBean.getObject();

        assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    @DisplayName("스프링 다이내믹 프록시를 이용한 프록시 생성 테스트 - advice, pointcut")
    void pointcutAdvisor() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello hello = (Hello) proxyFactoryBean.getObject();

        assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank you Toby");
    }

}
