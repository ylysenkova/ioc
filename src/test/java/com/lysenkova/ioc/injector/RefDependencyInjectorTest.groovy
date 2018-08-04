package com.lysenkova.ioc.injector

import com.lysenkova.ioc.entity.Bean
import com.lysenkova.ioc.entity.BeanDefinition
import com.lysenkova.ioc.testentities.MailServiceImpl
import com.lysenkova.ioc.testentities.UserService

import java.lang.reflect.Field

class RefDependencyInjectorTest extends GroovyTestCase {
    void testGetDependencies() {
        Injector injector = new RefDependencyInjector()
        def refDependencies = new HashMap()
        refDependencies.put('mailService', 'mailService')
        BeanDefinition beanDefinition = new BeanDefinition()
        beanDefinition.setRefDependencies(refDependencies)
        def actual = injector.getDependencies(beanDefinition)
        def expected = new HashMap()
        expected.put('mailService', 'mailService')
        assertEquals(expected, actual)
    }

    void testInject() {
        Injector injector = new RefDependencyInjector()

        UserService userService = new UserService()
        MailServiceImpl mailService = new MailServiceImpl()
        mailService.setPort(8080)
        mailService.setProtocol('DDL')
        Bean userBean = new Bean()
        userBean.setId('userService')
        userBean.setValue(userService)
        Bean mailBean = new Bean()
        mailBean.setId('mailService')
        mailBean.setValue(mailService)
        def beans = new ArrayList()
        beans.add(userBean)
        beans.add(mailBean)

        def refDependencies = new HashMap()
        refDependencies.put('mailService', 'mailService')
        BeanDefinition beanDefinition = new BeanDefinition()
        beanDefinition.setId('userService')
        beanDefinition.setBeanClassName('userService')
        beanDefinition.setRefDependencies(refDependencies)
        def beanDefinitions = new ArrayList()
        beanDefinitions.add(beanDefinition)

        injector.inject(beanDefinitions, beans)
        def expected = mailService
        def actual = userService.getMailService()
        assertEquals(expected, actual)

    }

    void testInjectValue() {
        Injector injector = new RefDependencyInjector()

        UserService userService = new UserService()
        MailServiceImpl mailService = new MailServiceImpl()
        mailService.setPort(8080)
        mailService.setProtocol('DDL')
        Bean userBean = new Bean()
        userBean.setId('userService')
        userBean.setValue(userService)
        Bean mailBean = new Bean()
        mailBean.setId('mailService')
        mailBean.setValue(mailService)
        def beans = new ArrayList()
        beans.add(userBean)
        beans.add(mailBean)
        def setMailService = injector.getSetterForField('mailService')
        Field field = userService.getClass().getDeclaredField('mailService')

        injector.injectValue(field, UserService.class, userBean.getValue(), mailBean.getId(), beans, setMailService)
        def expected = mailService
        def actual = userService.getMailService()
        assertEquals(expected, actual)
    }
}
