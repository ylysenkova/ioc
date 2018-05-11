package com.lysenkova.ioc.entity;

import java.util.Objects;

public class Bean {
    private Object value;
    private String id;

    public Bean() {
    }

    public Bean(Object value, String id) {
        this.value = value;
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bean)) return false;
        Bean bean = (Bean) o;
        return Objects.equals(getValue(), bean.getValue()) &&
                Objects.equals(getId(), bean.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getValue(), getId());
    }

    @Override
    public String toString() {
        return "Bean{" +
                "value=" + value +
                ", id='" + id + '\'' +
                '}';
    }
}
