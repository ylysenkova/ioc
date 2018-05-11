package com.lysenkova.ioc.applicationcontext;

import com.google.common.annotations.VisibleForTesting;
import com.lysenkova.ioc.entity.Bean;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class DependencyInjector extends Injector {


    @Override
    Map<String, String> getDependencies(BeanDefinition beanDefinition) {
        return  beanDefinition.getDependencies();
    }

    @Override
    @VisibleForTesting
    void inject(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        super.inject(beanDefinitions, beans);
    }

    @Override
    @VisibleForTesting
    void injectValue(String fieldName, Class<?> clazz, Object beanValue, String dependencyValue, List<Bean> beans) {
        try {
            String setter = getSetterForField(fieldName);
            Field field = clazz.getDeclaredField(fieldName);
            Method method = clazz.getMethod(setter, field.getType());
            method.invoke(beanValue, getDependencyType(field.getType(), dependencyValue));
        } catch (Exception e) {
            throw new BeanInstantiationException("Can not get setter for field: " + fieldName, e);
        }
    }

    private Object getDependencyType(Class type, String beanDefinitionValue) {
        if (type == Integer.TYPE || type == Integer.class) {
            return Integer.parseInt(beanDefinitionValue);
        } else if (type == Double.TYPE || type == Double.class) {
            return Double.parseDouble(beanDefinitionValue);
        } else if (type == Character.TYPE || type == Character.class) {
            return beanDefinitionValue.charAt(0);
        } else if (type == Byte.TYPE || type == Byte.class) {
            return Byte.parseByte(beanDefinitionValue);
        } else if (type == Boolean.TYPE || type == Boolean.class) {
            return Boolean.parseBoolean(beanDefinitionValue);
        } else if (type == Short.TYPE || type == Short.class) {
            return Short.parseShort(beanDefinitionValue);
        } else if (type == Long.TYPE || type == Long.class) {
            return Long.parseLong(beanDefinitionValue);
        } else if (type == Float.TYPE || type == Float.class) {
            return Float.parseFloat(beanDefinitionValue);
        } else if (type == String.class) {
            return beanDefinitionValue;
        }
        throw new BeanInstantiationException(beanDefinitionValue + " type can not be converted to " + type);
    }
}
