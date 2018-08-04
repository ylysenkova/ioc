package com.lysenkova.ioc.context;

import com.google.common.annotations.VisibleForTesting;
import com.lysenkova.ioc.entity.Bean;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class BeanPostProcessorInvoker {
    private List<Bean> postProcessorBeans;
    private List<Bean> beans;

    public BeanPostProcessorInvoker(List<Bean> postProcessorBeans, List<Bean> beans) {
        this.postProcessorBeans = postProcessorBeans;
        this.beans = beans;
    }

    public List<Bean> invokeBeforeMethod() {
        for (Bean postProcessorBean : postProcessorBeans) {
            beans = beans.stream().peek(bean -> ((BeanPostProcessor) postProcessorBean.getValue()).postProcessBeforeInitialization(bean, bean.getId()))
                    .collect(Collectors.toList());
        }
        return beans;
    }

    public void invokeInitMethod(List<BeanDefinition> beanDefinitions) {
        try {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                for (Bean bean : beans) {
                    if (beanDefinition.getId().equals(bean.getId())) {
                        if (beanDefinition.getInitMethod() != null) {
                            Method initMethod = getInitMethodFromBeanDefinition(bean, beanDefinition);
                            initMethod.invoke(bean.getValue());
                        }
                    }
                }

            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeanInstantiationException("Can not invoke init method for bean.", e);
        }

    }

    public List<Bean> invokeAfterMethod() {
        for (Bean postProcessorBean : postProcessorBeans) {
            beans = beans.stream()
                    .peek(bean -> ((BeanPostProcessor) postProcessorBean.getValue())
                            .postProcessAfterInitialization(bean, bean.getId()))
                    .collect(Collectors.toList());
        }
        return beans;
    }

    @VisibleForTesting
    Method getInitMethodFromBeanDefinition(Bean bean, BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class<?> beanClazz = bean.getValue().getClass();
        return beanClazz.getMethod(beanDefinition.getInitMethod());
    }

}
