package com.lysenkova.ioc.applicationcontext

import com.lysenkova.ioc.entity.Bean
import com.lysenkova.ioc.entity.BeanDefinition
import com.lysenkova.ioc.testentities.BeanPostProcessorTestEntity
import com.lysenkova.ioc.testentities.PaymentService
import com.lysenkova.ioc.testentities.UserService
import org.junit.Before
import org.junit.Test


class BeanPostProcessorInvokerTest {
    UserService userService
    PaymentService paymentService
    List<Bean> beans
    List<Bean> beanPostProcessBeans
    List<BeanDefinition> beanDefinitions
    Bean bean
    Bean bean1
    Bean postProcessBean
    BeanDefinition beanDefinition
    BeanDefinition beanDefinition1

    @Before
    void setUp() {
        userService = new UserService()

        beans = new ArrayList()
        bean = new Bean()
        bean.setId('bean')
        bean.setValue(userService)

        bean1 = new Bean()
        bean1.setId('bean1')
        bean1.setValue(paymentService)

        beans.add(bean1)
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

        beanDefinition1 = new BeanDefinition()
        beanDefinition1.setId('bean1')
        beanDefinition1.setBeanClassName('PaymentService')

        beanDefinitions = new ArrayList()
        beanDefinitions.add(beanDefinition1)
        beanDefinitions.add(beanDefinition)
    }

    @Test
    void "Invoke before method"() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        beanPostProcessBeans.add(postProcessBean)
        invoker.invokeBeforeMethod(beans)
        def actual = bean.getId()
        def expected = 'changedBean'
        assert expected == actual
        assert bean1.getId() == 'bean1'
    }

    @Test
    void testInvokeBeforeMethodNeg() {
        def beanPostProcessBeans = new ArrayList()
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeBeforeMethod(beans)
        def actual = bean.getId()
        def expected = 'bean'
        assert expected == actual
        assert bean1.getId() == 'bean1'
    }

    @Test
    void testInvokeInitMethod() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeInitMethod(beans, beanDefinitions)
        def actual = userService.getNumber()
        def expected = 3
        assert expected == actual
    }

    @Test
    void testInvokeAfterMethod() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeAfterMethod(beans)
        def actual = bean.getId()
        def expected = 'changedBean'
        assert expected == actual
        assert bean1.getId() == 'bean1'
    }

    @Test
    void testInvokeAfterMethodNeg() {
        def beanPostProcessBeans = new ArrayList()
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        invoker.invokeAfterMethod(beans)
        def actual = bean.getId()
        def expected = 'bean'
        assert expected == actual
        assert bean1.getId() == 'bean1'

    }

    @Test
    void testGetInitMethodFromBeanDefinition() {
        BeanPostProcessorInvoker invoker = new BeanPostProcessorInvoker(beanPostProcessBeans)
        def actual = invoker.getInitMethodFromBeanDefinition(bean, beanDefinition).toString()
        def expected = 'public void com.lysenkova.ioc.testentities.UserService.init()'
        assert expected == actual

    }
}
