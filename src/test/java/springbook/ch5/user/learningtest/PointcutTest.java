package springbook.ch5.user.learningtest;

import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.ch5.user.learningtest.pointcut.Bean;
import springbook.ch5.user.learningtest.pointcut.Target;
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

    @Test
    void methodSignaturePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int springbook.ch5.user.learningtest.pointcut.Target.minus(int,int) " +
                "throws java.lang.RuntimeException)");
        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null))
                .isTrue();
        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null))
                .isFalse();
        assertThat(pointcut.getClassFilter().matches(Bean.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("method"), null))
                .isFalse();
    }

    @Test
    void pointcut_allMethod() throws NoSuchMethodException {
        targetClassPointcutMatches("execution(* *(..))", true, true, true, true, true, true);
    }

    private void targetClassPointcutMatches(String expression, boolean... expected) throws NoSuchMethodException {
        pointcutMatches(expression, expected[0], Target.class, "hello");
        pointcutMatches(expression, expected[1], Target.class, "hello", String.class);
        pointcutMatches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointcutMatches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointcutMatches(expression, expected[4], Target.class, "method");
        pointcutMatches(expression, expected[5], Bean.class, "method");
    }

    private void pointcutMatches(String expression, boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat(pointcut.getClassFilter().matches(clazz) &&
                pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null))
                .isEqualTo(expected);
    }

}
