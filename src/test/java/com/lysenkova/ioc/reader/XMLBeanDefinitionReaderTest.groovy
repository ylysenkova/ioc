package com.lysenkova.ioc.reader

import com.lysenkova.ioc.beanparser.BeanDefinitionReader
import com.lysenkova.ioc.beanparser.XMLBeanDefinitionReader
import com.lysenkova.ioc.testentities.providers.BeanDefinitionDataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class XMLBeanDefinitionReaderTest {
    @Test(dataProvider = "provideBeanDefinitions", dataProviderClass = BeanDefinitionDataProvider.class)
    void testReadBeanDefinitionsByPath(expectedBeanDefinitions) {
        BeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader("src\\test\\resources\\full-context.xml")
        def actualBeanDefinitions = xmlBeanDefinitionReader.readBeanDefinitions()
        assertEquals(actualBeanDefinitions, expectedBeanDefinitions)
    }

    @Test(dataProvider = "provideBeanDefinitionPaths", dataProviderClass = BeanDefinitionDataProvider.class)
    void testReadBeanDefinitionsByPaths(expectedBeanDefinitions) {
        BeanDefinitionReader xmlBeanDefinitionReader = new XMLBeanDefinitionReader("src\\test\\resources\\full-context.xml", "src\\test\\resources\\email-context.xml")
        def actualBeanDefinitions = xmlBeanDefinitionReader.readBeanDefinitions()
        assertEquals(actualBeanDefinitions, expectedBeanDefinitions)
    }

}
