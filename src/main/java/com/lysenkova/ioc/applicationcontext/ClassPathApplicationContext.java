package com.lysenkova.ioc.applicationcontext;

import com.google.common.annotations.VisibleForTesting;
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
    private List<BeanDefinition> factoryBeanDefinitions;

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
        beanDefinitions = reader.readBeanDefinitions();
        try {
            factoryBeanDefinitions = getFactoryPostProcessorBeanDefinition(beanDefinitions);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                new BeanFactoryPostProcessorInvoker(this).invokePostProcessBeanFactoryMethod(beanDefinition, factoryBeanDefinitions);
                Bean bean = new Bean();
                bean.setId(beanDefinition.getId());
                Object beanClass = Class.forName(beanDefinition.getBeanClassName()).newInstance();
                bean.setValue(beanClass);
                beans = validateBeanId(bean);
                postProcessorBeans = getPostProcessorBeans(bean);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new BeanInstantiationException("Bean creation error.", e);
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

    private List<Bean> getPostProcessorBeans(Bean bean) {
        if (bean.getValue() instanceof BeanPostProcessor) {
            postProcessorBeans.add(bean);
        }
        return postProcessorBeans;
    }

    private List<BeanDefinition> getFactoryPostProcessorBeanDefinition(List<BeanDefinition> beanDefinitions) throws ClassNotFoundException {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class clazz = Class.forName(beanDefinition.getBeanClassName());
            Class[] interfaces = clazz.getInterfaces();
            for (Class beanFactoryInterface : interfaces) {
                if (beanFactoryInterface == BeanFactoryPostProcessor.class) {
                    factoryBeanDefinitions.add(beanDefinition);
                }
            }
        }
        return factoryBeanDefinitions;
    }

    private void startInitialization() {
        beans = new ArrayList<>();
        postProcessorBeans = new ArrayList<>();
        beanDefinitions = new ArrayList<>();
        factoryBeanDefinitions = new ArrayList<>();
        createBeansFromBeanDefinitions();
        new DependencyInjector().inject(beanDefinitions, beans);
        new RefDependencyInjector().inject(beanDefinitions, beans);
        BeanPostProcessorInvoker beanPostProcessorInvoker = new BeanPostProcessorInvoker(postProcessorBeans);
        beanPostProcessorInvoker.invokeBeforeMethod(beans);
        beanPostProcessorInvoker.invokeInitMethod(beans, beanDefinitions);
        beanPostProcessorInvoker.invokeAfterMethod(beans);
    }

}
