package springbook.ch5.user.learningtest;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    @Test
    void invokeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "Spring";
        assertThat(name.length()).isEqualTo(6);

        Method lengthMethod1 = String.class.getMethod("length");
        Method lengthMethod2 = name.getClass().getMethod("length");
        assertThat(lengthMethod1).isEqualTo(lengthMethod2);

        assertThat(lengthMethod1.invoke(name)).isEqualTo(6);

        assertThat(name.charAt(0)).isEqualTo('S');
        assertThat((Character) String.class.getMethod("charAt", int.class).invoke(name, 0)).isEqualTo('S');
    }

    interface Hello {
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    class HelloTarget implements Hello {

        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankYou(String name) {
            return "Thank you " + name;
        }
    }

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank you Toby");
    }

    class HelloUppercase implements Hello {
        Hello target;

        public HelloUppercase(Hello target) {
            this.target = target;
        }

        @Override
        public String sayHello(String name) {
            return target.sayHello(name).toUpperCase();
        }

        @Override
        public String sayHi(String name) {
            return target.sayHi(name).toUpperCase();
        }

        @Override
        public String sayThankYou(String name) {
            return target.sayThankYou(name).toUpperCase();
        }
    }

    @Test
    void HelloUppercase() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

}
