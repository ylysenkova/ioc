package com.lysenkova.ioc.applicationcontext;

import com.google.common.annotations.VisibleForTesting;
import com.lysenkova.ioc.beanparser.BeanDefinitionReader;
import com.lysenkova.ioc.beanparser.XMLBeanDefinitionReader;
import com.lysenkova.ioc.entity.Bean;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext implements ApplicationContext {
    private BeanDefinitionReader reader;
    private List<Bean> beans;
    private List<BeanDefinition> beanDefinitions;

    public ClassPathApplicationContext() {
    }

    public ClassPathApplicationContext(String path) {
        this(new String[]{path});
    }

    public ClassPathApplicationContext(String[] paths) {
        reader = new XMLBeanDefinitionReader(paths);
        startInitialization();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        validateBeanClass(beans, clazz);
        for (Bean bean : beans) {
            if (bean.getValue().getClass() == clazz) {
                return clazz.cast(bean.getValue());
            }
        }
        throw new BeanInstantiationException("Bean was not found for Class: " + clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getId().equals(name)) {
                return clazz.cast(getBean(clazz));
            }
        }
        throw new BeanInstantiationException("Bean with id: " + name + " not found.");
    }

    @Override
    public Object getBean(String name) {
        for (Bean bean : beans) {
            if (bean.getId().equals(name)) {
                return bean.getValue();
            }
        }
        throw new BeanInstantiationException("Bean with id: " + name + " not found.");
    }

    @Override
    public List<String> getBeanNames() {
        List<String> beanNames = new ArrayList<>();
        for (Bean bean : beans) {
            beanNames.add(bean.getId());
        }
        return beanNames;
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        this.reader = beanDefinitionReader;
        startInitialization();
    }

    @VisibleForTesting
    void createBeansFromBeanDefinitions() {
        beanDefinitions = reader.readBeanDefinitions();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Bean bean = new Bean();
                bean.setId(beanDefinition.getId());
                Object beanClass = Class.forName(beanDefinition.getBeanClassName()).newInstance();
                bean.setValue(beanClass);
                beans = validateBeanId(bean);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new BeanInstantiationException("Bean creation error.", e);
            }
        }
    }

    private List<Bean> validateBeanId(Bean bean) {
        if (beans.size() > 0) {
            for (Bean beanListElement : beans) {
                if (beanListElement.getId().equals(bean.getId())) {
                    throw new BeanInstantiationException("Bean with id: " + beanListElement.getId() + " has already been initialized.");
                }
            }
        }
        beans.add(bean);
        return beans;
    }

    private void validateBeanClass(List<Bean> beans, Class clazz) {
        List<Bean> beansForTheSameClass = new ArrayList<>();
        for (Bean bean : beans) {
            if (bean.getValue().getClass() == clazz) {
                beansForTheSameClass.add(bean);
            }
        }
        if (beansForTheSameClass.size() > 1) {
            throw new BeanInstantiationException("For " + clazz + " more than 1 bean initialized.");
        }
    }

    private void startInitialization() {
        beans = new ArrayList<>();
        beanDefinitions = new ArrayList<>();
        createBeansFromBeanDefinitions();
        new DependencyInjector().inject(beanDefinitions, beans);
        new RefDependencyInjector().inject(beanDefinitions, beans);
    }

}
