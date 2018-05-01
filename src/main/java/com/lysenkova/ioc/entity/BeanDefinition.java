package com.lysenkova.ioc.entity;

import java.util.Map;

public class BeanDefinition {
    private String id;
    private String beanClassName;
    private Map<String, String> dependencies;
    private Map<String, String> refDependencies;

    public BeanDefinition() {
    }

    public BeanDefinition(String id, String beanClassName, Map<String, String> dependencies, Map<String, String> refDependencies) {
        this.id = id;
        this.beanClassName = beanClassName;
        this.dependencies = dependencies;
        this.refDependencies = refDependencies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }

    public Map<String, String> getRefDependencies() {
        return refDependencies;
    }

    public void setRefDependencies(Map<String, String> refDependencies) {
        this.refDependencies = refDependencies;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", beanClassName='" + beanClassName + '\'' +
                ", dependencies=" + dependencies +
                ", refDependencies=" + refDependencies +
                '}';
    }
}
