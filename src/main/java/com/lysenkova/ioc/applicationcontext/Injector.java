package com.lysenkova.ioc.applicationcontext;

import com.lysenkova.ioc.entity.Bean;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import java.util.List;
import java.util.Map;

abstract class Injector {
       abstract void injectValue(String fieldName, Class<?> clazz, Object beanValue, String dependencyValue, List<Bean> beans);

    abstract Map<String, String> getDependencies(BeanDefinition beanDefinition);

    void inject(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        for (BeanDefinition beanDef : beanDefinitions) {
            Object beanName = getBeanById(beanDef.getId(), beans);
            Class beanClass = beanName.getClass();
            Map<String, String> dependencies = getDependencies(beanDef);
            for (String fieldName : dependencies.keySet()) {
                injectValue(fieldName, beanClass, beanName, dependencies.get(fieldName), beans);
            }
        }
    }

    private Object getBeanById(String name, List<Bean> beans) {
        for (Bean bean : beans) {
            if (bean.getId().equals(name)) {
                return bean.getValue();
            }
        }
        throw new BeanInstantiationException("Bean with id: " + name + " not found.");
    }

    String getSetterForField(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }


}
