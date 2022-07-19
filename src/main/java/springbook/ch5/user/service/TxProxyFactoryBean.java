package springbook.ch5.user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean implements FactoryBean<Object> {

    private Object target;
    private final PlatformTransactionManager transactionManager;
    private final String pattern;
    private final Class<?> serviceInterface;

    public TxProxyFactoryBean(PlatformTransactionManager transactionManager, String pattern, Class<?> serviceInterface) {
        this.transactionManager = transactionManager;
        this.pattern = pattern;
        this.serviceInterface = serviceInterface;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{serviceInterface},
                new TransactionHandler(target, transactionManager, pattern));
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
