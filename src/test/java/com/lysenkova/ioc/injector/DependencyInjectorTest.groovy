package com.lysenkova.ioc.injector

import com.lysenkova.ioc.entity.Bean
import com.lysenkova.ioc.entity.BeanDefinition
import com.lysenkova.ioc.testentities.MailServiceImpl

import java.lang.reflect.Field

class DependencyInjectorTest extends GroovyTestCase {
    void testGetDependencies() {
        Injector injector = new DependencyInjector()
        def dependencies = new HashMap()
        dependencies.put('port', '8080')
        dependencies.put('protocol', 'POS3')
        BeanDefinition beanDefinition = new BeanDefinition()
        beanDefinition.setDependencies(dependencies)
        def actual = injector.getDependencies(beanDefinition)
        def expected = new HashMap()
        expected.put('port', '8080')
        expected.put('protocol', 'POS3')
        assertEquals(expected, actual)
    }

    void testInject() {
        Injector injector = new DependencyInjector()

        MailServiceImpl mailService = new MailServiceImpl()
        mailService.setPort(8080)
        mailService.setProtocol('DDL')
        Bean mailBean = new Bean()
        mailBean.setId('mailService')
        mailBean.setValue(mailService)
        def beans = new ArrayList()
        beans.add(mailBean)

        def dependencies = new HashMap()
        dependencies.put('port', '8080')
        dependencies.put('protocol', 'DDL')
        BeanDefinition beanDefinition = new BeanDefinition()
        beanDefinition.setId('mailService')
        beanDefinition.setBeanClassName('mailService')
        beanDefinition.setDependencies(dependencies)
        def beanDefinitions = new ArrayList()
        beanDefinitions.add(beanDefinition)

        injector.inject(beanDefinitions, beans)
        def expectedPort = 8080
        def actualPort = mailService.getPort()
        assertEquals(expectedPort, actualPort)
        def expectedProtocol = 'DDL'
        def actualProtocol = mailService.getProtocol()
        assertEquals(expectedProtocol, actualProtocol)

    }

    void testInjectValue() {
        Injector injector = new DependencyInjector()

        MailServiceImpl mailService = new MailServiceImpl()
        mailService.setPort(8080)
        mailService.setProtocol('DDL')
        Bean mailBean = new Bean()
        mailBean.setId('mailService')
        mailBean.setValue(mailService)
        def beans = new ArrayList()
        beans.add(mailBean)
        def setPort = injector.getSetterForField('port')
        def setProtocol = injector.getSetterForField('protocol')
        Field port = mailService.getClass().getDeclaredField('port')
        Field protocol = mailService.getClass().getDeclaredField('protocol')

        injector.injectValue(port, MailServiceImpl.class, mailBean.getValue(), '8080', beans, setPort)
        injector.injectValue(protocol, MailServiceImpl.class, mailBean.getValue(), 'DDL', beans, setProtocol)
        def expectedPort = 8080
        def actualPort = mailService.getPort()
        assertEquals(expectedPort, actualPort)
        def expectedProtocol = 'DDL'
        def actualProtocol = mailService.getProtocol()
        assertEquals(expectedProtocol, actualProtocol)
    }
}
