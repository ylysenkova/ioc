package com.lysenkova.initbean.applicationcontext;

import com.lysenkova.initbean.beanparser.BeanDefinitionReader;
import com.lysenkova.initbean.beanparser.XMLBeanDefinitionReader;
import com.lysenkova.initbean.entity.Bean;
import com.lysenkova.initbean.entity.BeanDefinition;
import com.lysenkova.initbean.exception.BeanInstantiationException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext implements ApplicationContext {
    private String[] paths;
    private BeanDefinitionReader reader;
    private List<Bean> beans;
    private List<BeanDefinition> beanDefinitions;

    public ClassPathApplicationContext() {
        setBeanDefinitionReader(reader);
    }

    public ClassPathApplicationContext(String path) {
        this(new String[]{path});
    }

    public ClassPathApplicationContext(String[] paths) {
        beans = new ArrayList<>();
        beanDefinitions = new ArrayList<>();
        setBeanDefinitionReader(reader);
        createBeansFromBeanDefinitions();
        injectDependencies();
        injectRefDependencies();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getValue().equals(clazz)) {
                return clazz.cast(bean.getValue());
            }
        }
        throw new RuntimeException("Bean was not found for Class: " + clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getId().equals(name)) {
                return clazz.cast(getBean(clazz));
            }
        }
        throw new RuntimeException("Bean was not found for Class: " + clazz);
    }

    @Override
    public Object getBean(String name) {
        for (Bean bean : beans) {
            if (bean.getId().equals(name)) {
                return bean.getValue();
            }
        }
        throw new RuntimeException("Bean with id: " + name + " not found.");
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
    }

    private void createBeansFromBeanDefinitions() throws BeanInstantiationException {
        beanDefinitions = reader.readBeanDefinitions();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Bean bean = new Bean();
                bean.setId(beanDefinition.getId());
                bean.setValue(Class.forName(beanDefinition.getBeanClassName()).newInstance());
                beans.add(bean);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Bean creation error.");
            }
        }
    }


    private void injectDependencies() throws BeanInstantiationException {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object beanName = getBean(beanDefinition.getId());
            Class beanClass = beanName.getClass();
            Map<String, String> dependencies = beanDefinition.getDependencies();
            for (String fieldName : dependencies.keySet()) {
                injectValueDependency(fieldName, beanClass, beanName, dependencies.get(fieldName));
            }
        }
    }


    private void injectRefDependencies() throws BeanInstantiationException {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object beanName = getBean(beanDefinition.getId());
            Class beanClass = beanName.getClass();
            Map<String, String> beanRefDependencies = beanDefinition.getRefDependencies();
            if (beanRefDependencies == null) {
                return;
            } else {
                for (String fieldName : beanRefDependencies.keySet()) {
                    injectValueRefDependency(fieldName, beanClass, beanName, beanRefDependencies.get(fieldName));
                }
            }
        }
    }


    private String getSetterForField(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private Object getDependencyType(Class type, String beanDefinitionValue) throws BeanInstantiationException {
        if (type == Integer.class) {
            return Integer.parseInt(beanDefinitionValue);
        } else if (type == Double.class) {
            return Double.parseDouble(beanDefinitionValue);
        } else if (type == String.class) {
            return beanDefinitionValue;
        }
        throw new RuntimeException(beanDefinitionValue + "type can not be converted to " + type);
    }

    private void injectValueDependency(String fieldName, Class<?> clazz, Object beanValue, String dependencyValue) {
        try {
            String setter = getSetterForField(fieldName);
            Field field = clazz.getDeclaredField(fieldName);
            Method method = clazz.getMethod(setter, field.getType());
            method.invoke(beanValue, getDependencyType(field.getType(), dependencyValue));
        } catch (Exception e) {
            throw new RuntimeException("Can not get setter for field: " + fieldName, e);
        }
    }

    private void injectValueRefDependency(String fieldName, Class<?> clazz, Object beanValue, String refDependencyValue) {
        try {
            String setter = getSetterForField(fieldName);
            Field field = clazz.getDeclaredField(fieldName);
            Method method = clazz.getMethod(setter, field.getType());
            method.invoke(beanValue, refDependencyValue);
        } catch (Exception e) {
            throw new RuntimeException("Reference dependency" + fieldName + "can not be inserted", e);
        }
    }

}
