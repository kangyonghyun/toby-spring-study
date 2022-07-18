package springbook.ch5.user.learningtest;

import org.junit.jupiter.api.Test;
import springbook.ch5.user.learningtest.reflection.Hello;
import springbook.ch5.user.learningtest.reflection.HelloTarget;
import springbook.ch5.user.learningtest.reflection.HelloUppercase;
import springbook.ch5.user.learningtest.reflection.UppercaseHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    void reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "Spring";
        assertThat(name.length()).isEqualTo(6);

        Method lengthMethod1 = String.class.getMethod("length");
        Method lengthMethod2 = name.getClass().getMethod("length");
        assertThat(lengthMethod1).isEqualTo(lengthMethod2);

        assertThat(lengthMethod1.invoke(name)).isEqualTo(6);

        assertThat(name.charAt(0)).isEqualTo('S');
        assertThat((Character) String.class.getMethod("charAt", int.class).invoke(name, 0)).isEqualTo('S');
    }

    @Test
    void hello() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank you Toby");
    }

    @Test
    void simpleProxy() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    void dynamicProxy() {
        Hello helloProxy = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget()));
        assertThat(helloProxy.sayHello("toby")).isEqualTo("HELLO TOBY");
        assertThat(helloProxy.sayHi("toby")).isEqualTo("Hi toby");
        assertThat(helloProxy.sayThankYou("toby")).isEqualTo("Thank you toby");
    }

}
