package com.lysenkova.initbean.beanparser.utils;

import com.lysenkova.initbean.entity.BeanDefinition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanParserHandler extends DefaultHandler {
    private List<BeanDefinition> beanDefinitions;

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    @Override
    public void startElement(String path, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("bean")) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinitions = new ArrayList<>();
            Map<String, String> dependencies = new HashMap<>();
            Map<String, String> references = new HashMap<>();

            String id = attributes.getValue("id");
            beanDefinition.setId(id);

            String className = attributes.getValue("class");
            beanDefinition.setBeanClassName(className);
            beanDefinition.setDependencies(dependencies);
            beanDefinition.setRefDependencies(references);
            beanDefinitions.add(beanDefinition);
        } else if (qName.equalsIgnoreCase("property")) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            String reference = attributes.getValue("ref");
            if (value != null) {
                beanDefinitions.get(beanDefinitions.size() - 1).getDependencies().put(name, value);
            } else if (reference != null) {
                beanDefinitions.get(beanDefinitions.size() - 1).getRefDependencies().put(name, reference);
            }
        }
    }

    @Override
    public void endElement(String path, String localName, String qName) {
    }

}