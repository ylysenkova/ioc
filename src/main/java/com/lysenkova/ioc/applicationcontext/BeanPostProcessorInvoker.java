package com.lysenkova.ioc.applicationcontext;

import com.google.common.annotations.VisibleForTesting;
import com.lysenkova.ioc.entity.Bean;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BeanPostProcessorInvoker {
    private List<Bean> postProcessorBeans;

    public BeanPostProcessorInvoker(List<Bean> postProcessorBeans) {
        this.postProcessorBeans = postProcessorBeans;
    }

    public void invokeBeforeMethod(List<Bean> beans) {
        Bean bean = getNotPostProcessorBean(beans);
        for (Bean postProcessorBean : postProcessorBeans) {
            ((BeanPostProcessor) postProcessorBean.getValue()).postProcessBeforeInitialization(bean, bean.getId());
        }
    }

    public void invokeInitMethod(List<Bean> beans, List<BeanDefinition> beanDefinitions) {
        Bean bean = getNotPostProcessorBean(beans);
        try {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                if (beanDefinition.getId().equals(bean.getId())) {
                    if (isInitMethodExist(beanDefinition)) {
                        Method initMethod = getInitMethodFromBeanDefinition(bean, beanDefinition);
                        initMethod.invoke(bean.getValue());
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeanInstantiationException("Can not invoke init method for bean.", e);
        }

    }

    public void invokeAfterMethod(List<Bean> beans) {
        Bean bean = getNotPostProcessorBean(beans);
        for (Bean postProcessorBean : postProcessorBeans) {
            ((BeanPostProcessor) postProcessorBean.getValue()).postProcessAfterInitialization(bean, bean.getId());
        }
    }

    private boolean isInitMethodExist(BeanDefinition beanDefinition) {
        if (beanDefinition.getInitMethod() != null) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    Method getInitMethodFromBeanDefinition(Bean bean, BeanDefinition beanDefinition) throws NoSuchMethodException {
        Class beanClazz = bean.getValue().getClass();
        Method method = beanClazz.getMethod(beanDefinition.getInitMethod());
        return method;
    }

    private Bean getNotPostProcessorBean(List<Bean> beans) {
        for (Bean bean : beans) {
            if (!postProcessorBeans.contains(bean)) {
                return bean;
            }
        }
        throw new RuntimeException("Bean not found");
    }
}
