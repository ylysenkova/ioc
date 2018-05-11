package com.lysenkova.ioc.applicationcontext;

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
    void inject(List<BeanDefinition> beanDefinitions,  List<Bean> beans) {
        super.inject(beanDefinitions, beans);
    }

    @Override
    void injectValue(String fieldName, Class<?> clazz, Object beanValue, String refDependencyValue, List<Bean> beans) {
        try {
            String setter = getSetterForField(fieldName);
            Field field = clazz.getDeclaredField(fieldName);
            Method method = clazz.getMethod(setter, field.getType());
            Object refDependencyObject = getRefBeanObject(refDependencyValue, beans);
            method.invoke(beanValue, refDependencyObject);
        } catch (Exception e) {
            throw new BeanInstantiationException("Reference dependency " + fieldName + " can not be inserted", e);
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
