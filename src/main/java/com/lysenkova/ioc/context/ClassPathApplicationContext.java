package com.lysenkova.ioc.context;

import com.google.common.annotations.VisibleForTesting;
import com.lysenkova.ioc.exception.NotUniqueBeanExeption;
import com.lysenkova.ioc.injector.DependencyInjector;
import com.lysenkova.ioc.injector.RefDependencyInjector;
import com.lysenkova.ioc.beanparser.BeanDefinitionReader;
import com.lysenkova.ioc.beanparser.xml.XMLBeanDefinitionReader;
import com.lysenkova.ioc.entity.Bean;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import java.util.ArrayList;
import java.util.List;

public class ClassPathApplicationContext implements ApplicationContext {
    private BeanDefinitionReader reader;
    private List<Bean> beans;
    private List<Bean> postProcessorBeans;
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
        boolean isExist = false;
        T result = null;
        for (Bean bean : beans) {
            if (clazz.isAssignableFrom(bean.getValue().getClass())) {
                if (!isExist) {
                    result = clazz.cast(bean.getValue());
                    isExist = true;
                } else {
                    throw new NotUniqueBeanExeption("For " + clazz + " more than 1 bean initialized.");
                }
            }
        }
        if (isExist) {
            return result;
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
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        this.reader = beanDefinitionReader;
        startInitialization();
    }


    @VisibleForTesting
    void createBeansFromBeanDefinitions() {
    try {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = new Bean();
            bean.setId(beanDefinition.getId());
            Object beanValue = Class.forName(beanDefinition.getBeanClassName()).newInstance();
            bean.setValue(beanValue);
            addBean(bean);
            if (bean.getValue() instanceof BeanPostProcessor) {
                postProcessorBeans.add(bean);
            }
        }
        beans.removeAll(postProcessorBeans);
    } catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        throw new BeanInstantiationException("Bean creation error.", e);
    }
}

    private void addBean(Bean bean) {
        for (Bean beanElement : beans) {
            if (beanElement.getId().equals(bean.getId())) {
                throw new BeanInstantiationException("Bean with id: " + beanElement.getId() + " has already been initialized.");
            }
        }
        beans.add(bean);
    }

    private List<BeanDefinition> getFactoryPostProcessorBeanDefinition(List<BeanDefinition> beanDefinitions) throws ClassNotFoundException {
        List<BeanDefinition> factoryBeanDefinitions = new ArrayList<>();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class clazz = Class.forName(beanDefinition.getBeanClassName());
            if (BeanFactoryPostProcessor.class.isAssignableFrom(clazz)) {
                factoryBeanDefinitions.add(beanDefinition);
            }
        }
        beanDefinitions.removeAll(factoryBeanDefinitions);
        return factoryBeanDefinitions;
    }

    private void startInitialization() {
        beans = new ArrayList<>();
        postProcessorBeans = new ArrayList<>();

        //read beanDefinitions
        beanDefinitions = reader.readBeanDefinitions();

        //run factories
        try {
            List<BeanDefinition> factoryBeanDefinitions = getFactoryPostProcessorBeanDefinition(beanDefinitions);
            new BeanFactoryPostProcessorInvoker(beanDefinitions, factoryBeanDefinitions).invokePostProcessBeanFactoryMethod();
        } catch (ClassNotFoundException e1) {
            throw new BeanInstantiationException("Bean creation error.", e1);
        }

        //construct
        createBeansFromBeanDefinitions();

        //dependency injection
        new DependencyInjector().inject(beanDefinitions, beans);
        new RefDependencyInjector().inject(beanDefinitions, beans);

        BeanPostProcessorInvoker beanPostProcessorInvoker = new BeanPostProcessorInvoker(postProcessorBeans, beans);

        //before initialization
        beans = beanPostProcessorInvoker.invokeBeforeMethod();

        //initialization
        beanPostProcessorInvoker.invokeInitMethod(beanDefinitions);

        //after initialization
        beans = beanPostProcessorInvoker.invokeAfterMethod();
    }
}