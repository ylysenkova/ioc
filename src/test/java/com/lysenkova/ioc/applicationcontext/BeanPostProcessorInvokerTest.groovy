package com.lysenkova.ioc.applicationcontext

import com.lysenkova.ioc.entity.Bean
import com.lysenkova.ioc.entity.BeanDefinition
import com.lysenkova.ioc.testentities.BeanPostProcessorTestEntity
import com.lysenkova.ioc.testentities.UserService


class BeanPostProcessorInvokerTest extends GroovyTestCase {
    UserService userService
    List<Bean> beans
    List<Bean> beanPostProcessBeans
    List<BeanDefinition> beanDefinitions
    Bean bean
    Bean postProcessBean
    BeanDefinition beanDefinition

    void setUp() {
        userService = new UserService()

        beans = new ArrayList()
        bean = new Bean()
        bean.setId('bean')
        bean.setValue(userService)
        beans.add(bean)

        postProcessBean = new Bean()
        postProcessBean.setId('postProcessBean')
        postProcessBean.setValue(new BeanPostProcessorTestEntity())
        beanPostProcessBeans = new ArrayList()
        beanPostProcessBeans.add(postProcessBean)
        beans.add(postProcessBean)

        beanDefinition = new BeanDefinition()
        beanDefinition.setId('bean')
        beanDefinition.setBeanClassName('UserService')
        beanDefinition.setInitMethod('init')
        beanDefinitions = new ArrayList()
        beanDefinitions.add(beanDefinition)
    }

    void testInvokeBeforeMethod() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeBeforeMethod(beans)
        def actual = bean.getId()
        def expected = 'changedBean'
        assertEquals(expected, actual)
    }

    void testInvokeBeforeMethodNeg() {
        def beanPostProcessBeans = new ArrayList()

        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeBeforeMethod(beans)
        def actual = bean.getId()
        def expected = 'bean'
        assertEquals(expected, actual)
    }

    void testInvokeInitMethod() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeInitMethod(beans, beanDefinitions)
        def actual = userService.getNumber()
        def expected = 3
        assertEquals(expected, actual)
    }

    void testInvokeAfterMethod() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeAfterMethod(beans)
        def actual = bean.getId()
        def expected = 'changedBean'
        assertEquals(expected, actual)
    }

    void testInvokeAfterMethodNeg() {
        def beanPostProcessBeans = new ArrayList()

        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeAfterMethod(beans)
        def actual = bean.getId()
        def expected = 'bean'
        assertEquals(expected, actual)

    }

    void testGetInitMethodFromBeanDefinition() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        def actual = invoker.getInitMethodFromBeanDefinition(bean, beanDefinition).toString()
        def expected = 'public void com.lysenkova.ioc.testentities.UserService.init()'
        assertEquals(expected, actual)

    }
}
