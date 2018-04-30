package com.lysenkova.initbean.beanparser;

import com.lysenkova.initbean.beanparser.utils.BeanParserHandler;
import com.lysenkova.initbean.entity.BeanDefinition;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String[] paths;
    private List<BeanDefinition> beanDefinitions;

    public XMLBeanDefinitionReader(String[] paths) {
        this.paths=paths;
    }

    public List<BeanDefinition> readBeanDefinitions() throws RuntimeException{
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            BeanParserHandler beanParserHandler = new BeanParserHandler();
            for (String path : paths) {
                saxParser.parse(path, beanParserHandler);
                beanDefinitions = beanParserHandler.getBeanDefinitions();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return beanDefinitions;
    }
}
