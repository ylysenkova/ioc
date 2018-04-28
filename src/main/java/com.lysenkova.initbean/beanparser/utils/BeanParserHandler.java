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
    private BeanDefinition beanDefinition;
    private Map<String, String> dependencies;
    private Map<String, String> referencies;

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    @Override
    public void startElement(String path, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("bean")) {
            beanDefinition = new BeanDefinition();
            String id = attributes.getValue("id");
            beanDefinition.setId(id);
            String className = attributes.getValue("class");
            beanDefinition.setBeanClassName(className);
        } else if (qName.equalsIgnoreCase("property")) {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            String reference = attributes.getValue("ref");
            if (name != null && value != null) {
                if (dependencies == null) {
                    dependencies = new HashMap<String, String>();
                }
                dependencies.put(name, value);
                beanDefinition.setDependencies(dependencies);
            } else if (name != null && reference != null) {
                if (referencies == null) {
                    referencies = new HashMap<String, String>();
                }
                referencies.put(name, reference);
                beanDefinition.setRefDependencies(referencies);
            }
        }
    }

    @Override
    public void endElement(String path, String localName, String qName) throws SAXException {
        if (beanDefinitions == null) {
            beanDefinitions = new ArrayList<BeanDefinition>();
        }
        if (qName.equalsIgnoreCase("bean")) {
            beanDefinitions.add(beanDefinition);
        }
    }

}
