package com.lysenkova.ioc.beanparser.xml;

import com.lysenkova.ioc.beanparser.BeanDefinitionReader;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private static final SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();

    private InputStream[] files;

    public XMLBeanDefinitionReader(String[] paths) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream[] filesContent = new InputStream[paths.length];
        for (int i = 0; i < paths.length; i++) {
            InputStream resource = classLoader.getResourceAsStream(paths[i]);
            if (resource == null) {
                throw new RuntimeException("Can not find file.");
            }
            filesContent[i] = resource;
        }
        this.files = filesContent;
    }

    public List<BeanDefinition> readBeanDefinitions() throws BeanInstantiationException {
        try {
            SAXParser saxParser = SAX_PARSER_FACTORY.newSAXParser();
            List<BeanDefinition> beanDefinitions = new ArrayList<>();

            for (InputStream file : files) {
                BeanParserHandler beanParserHandler = new BeanParserHandler();
                System.out.println(file);
                saxParser.parse(file, beanParserHandler);
                beanDefinitions.addAll(beanParserHandler.getBeanDefinitions());
            }
            return beanDefinitions;
        } catch (Exception e) {
            throw new RuntimeException("Exception during parsing file with beans.", e);
        }
    }
}
