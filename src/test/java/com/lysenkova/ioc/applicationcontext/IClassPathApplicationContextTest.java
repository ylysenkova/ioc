package com.lysenkova.ioc.applicationcontext;


import com.lysenkova.ioc.beanparser.BeanDefinitionReader;
import com.lysenkova.ioc.beanparser.XMLBeanDefinitionReader;
import com.lysenkova.ioc.testentities.Equipment;
import com.lysenkova.ioc.testentities.Person;
import com.lysenkova.ioc.testentities.Character;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class IClassPathApplicationContextTest {
    String[] paths = {
            "C:\\Users\\Yana\\IdeaProjects\\ioc\\src\\main\\resources\\root-context.xml",
            "C:\\Users\\Yana\\IdeaProjects\\ioc\\src\\main\\resources\\bean-context.xml"
    };



    @Test
    public void getBeanByClassName() {
        ApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        Class clazz = Person.class;
        Object expected = clazz.getName();
        Object actual = applicationContext.getBean(clazz).getClass().getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getBeanByIdAndClassName() {
        ApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        Class clazz = Equipment.class;
        Object expected = clazz.getName();
        Object actual = applicationContext.getBean("equipment", clazz).getClass().getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getBeanById() {
        ApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        String id = "character";
        Object expected = Character.class.getName();
        Object actual = applicationContext.getBean(id).getClass().getName();
        assertEquals(expected, actual);
    }

    @Test
    public void getBeanNames() {
        ApplicationContext applicationContext = new ClassPathApplicationContext(paths);
        List<String> expected = new ArrayList<>();
        expected.add("person");
        expected.add("cat");
        expected.add("character");
        expected.add("equipment");
        List<String> actual = applicationContext.getBeanNames();
        assertEquals(expected, actual);
    }

    @Test
    public void setBeanDefinitionReader() {
        BeanDefinitionReader reader = new XMLBeanDefinitionReader(paths);
        ApplicationContext applicationContext = new ClassPathApplicationContext();
        applicationContext.setBeanDefinitionReader(reader);
        List<String> expected = new ArrayList<>();
        expected.add("person");
        expected.add("cat");
        expected.add("character");
        expected.add("equipment");
        List<String> actual = applicationContext.getBeanNames();
        assertEquals(expected, actual);
    }
}
