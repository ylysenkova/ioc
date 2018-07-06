package com.lysenkova.ioc.applicationcontext.injector

import com.lysenkova.ioc.entity.Bean
import com.lysenkova.ioc.entity.BeanDefinition
import com.lysenkova.ioc.testentities.MailService

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

        MailService mailService = new MailService()
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

        MailService mailService = new MailService()
        mailService.setPort(8080)
        mailService.setProtocol('DDL')
        Bean mailBean = new Bean()
        mailBean.setId('mailService')
        mailBean.setValue(mailService)
        def beans = new ArrayList()
        beans.add(mailBean)

        injector.injectValue('port', MailService.class, mailBean.getValue(), '8080', beans)
        injector.injectValue('protocol', MailService.class, mailBean.getValue(), 'DDL', beans)
        def expectedPort = 8080
        def actualPort = mailService.getPort()
        assertEquals(expectedPort, actualPort)
        def expectedProtocol = 'DDL'
        def actualProtocol = mailService.getProtocol()
        assertEquals(expectedProtocol, actualProtocol)
    }
}
