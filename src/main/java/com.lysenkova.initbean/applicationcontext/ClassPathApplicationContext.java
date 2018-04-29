package com.lysenkova.initbean.applicationcontext;

import com.lysenkova.initbean.beanparser.BeanDefinitionReader;
import com.lysenkova.initbean.beanparser.XMLBeanDefinitionReader;
import com.lysenkova.initbean.entity.Bean;
import com.lysenkova.initbean.entity.BeanDefinition;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext<T> implements ApplicationContext<T> {
    private String[] paths;
    private BeanDefinitionReader reader;
    private List<Bean> beans;
    private List<BeanDefinition> beanDefinitions;

    public ClassPathApplicationContext(String[] paths) throws InstantiationException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException {
        createBeansFromBeanDefinitions();
        injectDependencies();
        injectRefDependencies();
    }

    @Override
    public T getBean(Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getValue().equals(clazz)) {
                return (T) bean.getValue();
            }
        }
        return null;
    }

    @Override
    public T getBean(String name, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getId().equals(name) && bean.getValue().getClass().equals(clazz)) {
                return (T) bean.getValue();
            }
        }
        return null;
    }

    @Override
    public Object getBean(String name) {
        for (Bean bean : beans) {
            if (bean.getId().equals(name)) {
                return bean.getValue();
            }
        }
        return null;
    }

    @Override
    public List<String> getBeanNames() {
        List<String> beanNames = new ArrayList<>();
        for (Bean bean : beans) {
            beanNames.add(bean.getValue().toString());
        }
        return beanNames;
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        String xml = ".xml";
        for (String path : paths) {
            if (path.endsWith(xml)) {
                String[] xmlPaths = new String[paths.length];
                for (int i = 0; i < xmlPaths.length; i++) {
                    xmlPaths[i] = path;
                }
                reader = new XMLBeanDefinitionReader(xmlPaths);
            }
        }

    }

    private void createBeansFromBeanDefinitions() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        beans = new ArrayList<>();
        beanDefinitions = new ArrayList<>();
        setBeanDefinitionReader(reader);
        beanDefinitions = reader.readBeanDefinitions();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = new Bean();
            bean.setId(beanDefinition.getId());
            bean.setValue(Class.forName(beanDefinition.getBeanClassName()).newInstance());
            beans.add(bean);
        }
    }

    private void injectDependencies() throws IllegalAccessException, InvocationTargetException {
        for (Bean bean : beans) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                if (bean.getId().equalsIgnoreCase(beanDefinition.getId())) {
                    Field[] fields = bean.getValue().getClass().getDeclaredFields();
                    for (Field field : fields) {
                        Map<String, String> beanDependencies = beanDefinition.getDependencies();
                        for (Map.Entry<String, String> beanDependency : beanDependencies.entrySet()) {
                            if (beanDependency.getKey().equals(field.getName())) {
                                String setter = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                Method[] methods = bean.getValue().getClass().getDeclaredMethods();
                                for (Method method : methods) {
                                    if (method.getName().equals(setter)) {
                                        if (field.getType().equals(Integer.class)) {
                                            method.invoke(bean.getValue(), Integer.parseInt(beanDependency.getValue()));
                                        } else if (field.getType().equals(Double.class)) {
                                            method.invoke(bean.getValue(), Double.parseDouble(beanDependency.getValue()));
                                        } else {
                                            method.invoke(bean.getValue(), beanDependency.getValue());
                                        }

                                    }

                                }
                            }

                        }

                    }
                }
            }
        }
    }

    private void injectRefDependencies() throws InvocationTargetException, IllegalAccessException {
        for (Bean bean : beans) {
            for (BeanDefinition beanDefinition : beanDefinitions) {
                if (bean.getId().equalsIgnoreCase(beanDefinition.getId())) {
                    Field[] fields = bean.getValue().getClass().getDeclaredFields();
                    for (Field field : fields) {
                        Map<String, String> beanRefDependencies = beanDefinition.getRefDependencies();
                        if (beanRefDependencies == null) {
                            return;
                        } else {
                            for (Map.Entry<String, String> beanRefDependency : beanRefDependencies.entrySet()) {
                                if (beanRefDependency.getKey().equals(field.getName())) {
                                    String setter = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                                    Method[] methods = bean.getValue().getClass().getDeclaredMethods();
                                    for (Method method : methods) {
                                        if (method.getName().equals(setter)) {
                                            String refBeanId = beanRefDependency.getValue();
                                            method.invoke(bean.getValue(), getBean(refBeanId));

                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
