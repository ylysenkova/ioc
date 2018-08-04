package com.lysenkova.ioc.beanparser.xml;

import com.lysenkova.ioc.beanparser.BeanDefinitionReader;
import com.lysenkova.ioc.entity.BeanDefinition;
import com.lysenkova.ioc.exception.BeanInstantiationException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private static final SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();

    private String[] paths;

    public XMLBeanDefinitionReader(String[] paths) {
        ClassLoader classLoader = getClass().getClassLoader();
        for (int i = 0; i < paths.length; i++) {
            URL resource = classLoader.getResource(paths[i]);
            if (resource == null) {
                throw new RuntimeException("Can not find file.");
            }
            File file = new File(resource.getFile());
            paths[i] = file.getAbsolutePath();
        }
        this.paths = paths;
    }

    public List<BeanDefinition> readBeanDefinitions() throws BeanInstantiationException {
        try {
            SAXParser saxParser = SAX_PARSER_FACTORY.newSAXParser();
            List<BeanDefinition> beanDefinitions = new ArrayList<>();

            for (String path : paths) {
                BeanParserHandler beanParserHandler = new BeanParserHandler();
                saxParser.parse(path, beanParserHandler);
                beanDefinitions.addAll(beanParserHandler.getBeanDefinitions());
            }
            return beanDefinitions;
        } catch (Exception e) {
            throw new RuntimeException("Exception during parsing file with beans.");
        }
    }
}
