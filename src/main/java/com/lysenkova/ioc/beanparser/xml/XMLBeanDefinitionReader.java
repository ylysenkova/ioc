package com.lysenkova.ioc.beanparser.xml;

import com.lysenkova.ioc.beanparser.BeanDefinitionReader;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String[] paths;
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();

    public XMLBeanDefinitionReader(String[] paths) {
        for (int i = 0; i < paths.length; i++) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(paths[i]).getFile());
            paths[i] = file.getAbsolutePath();
        }
        this.paths = paths;
    }

    public List<BeanDefinition> readBeanDefinitions() throws BeanInstantiationException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            BeanParserHandler beanParserHandler = new BeanParserHandler();
            for (String path : paths) {
                saxParser.parse(path, beanParserHandler);
            }
            beanDefinitions.addAll(beanParserHandler.getBeanDefinitions());
        } catch (Exception e) {
            throw new RuntimeException("Exception during parsing file with beans.");
        }

        return beanDefinitions;
    }
}
