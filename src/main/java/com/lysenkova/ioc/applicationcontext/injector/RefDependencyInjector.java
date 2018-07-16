package com.lysenkova.ioc.applicationcontext.injector;

import com.google.common.annotations.VisibleForTesting;
import com.lysenkova.ioc.entity.Bean;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RefDependencyInjector extends Injector {


    @Override
    Map<String, String> getDependencies(BeanDefinition beanDefinition) {
        return beanDefinition.getRefDependencies();
    }

    @Override
    public void inject(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        super.inject(beanDefinitions, beans);
    }

    @Override
    @VisibleForTesting
    void injectValue(Field field, Class<?> clazz, Object beanValue, String refDependencyValue, List<Bean> beans, String setter) {
        try {
            Method method = clazz.getMethod(setter, field.getType());
            Object refDependencyObject = getRefBeanObject(refDependencyValue, beans);
            method.invoke(beanValue, refDependencyObject);
        } catch (Exception e) {
            throw new BeanInstantiationException("Reference dependency " + field.getName() + " can not be inserted", e);
        }
    }

    private Object getRefBeanObject(String refDependencyValue, List<Bean> beans) {
        for (Bean bean : beans) {
            if (bean.getId().equals(refDependencyValue)) {
                return bean.getValue();
            }
        }
        throw new BeanInstantiationException("Could not find reference object for reference: " + refDependencyValue);
    }
}
