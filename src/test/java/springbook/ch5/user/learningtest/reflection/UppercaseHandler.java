package springbook.ch5.user.learningtest.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

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
