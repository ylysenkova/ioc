package com.lysenkova.ioc.entity;


import java.util.Map;
import java.util.Objects;

public class BeanDefinition {
    private String id;
    private String beanClassName;
    private String initMethod;
    private Map<String, String> dependencies;
    private Map<String, String> refDependencies;

    public BeanDefinition() {
    }

    public BeanDefinition(String id, String beanClassName, String initMethod, Map<String, String> dependencies, Map<String, String> refDependencies) {
        this.id = id;
        this.beanClassName = beanClassName;
        this.initMethod = initMethod;
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

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeanDefinition)) return false;
        BeanDefinition that = (BeanDefinition) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getBeanClassName(), that.getBeanClassName()) &&
                Objects.equals(getInitMethod(), that.getInitMethod()) &&
                Objects.equals(getDependencies(), that.getDependencies()) &&
                Objects.equals(getRefDependencies(), that.getRefDependencies());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getBeanClassName(), getInitMethod(), getDependencies(), getRefDependencies());
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "id='" + id + '\'' +
                ", beanClassName='" + beanClassName + '\'' +
                ", initMethod='" + initMethod + '\'' +
                ", dependencies=" + dependencies +
                ", refDependencies=" + refDependencies +
                '}';
    }
}
