package com.lysenkova.ioc.itests

import com.lysenkova.ioc.context.ApplicationContext
import com.lysenkova.ioc.context.BeanFactoryPostProcessorInvoker
import com.lysenkova.ioc.context.ClassPathApplicationContext
import com.lysenkova.ioc.entity.BeanDefinition
import org.junit.Test

class BeanFactoryPostProcessorInvokerITest {

    @Test
    void testInvokePostProcessBeanFactoryMethod() {

        ApplicationContext context = new ClassPathApplicationContext("payment-context.xml")
        def factoryBeanDefinitions = new ArrayList()
        BeanDefinition factoryBeanDefinition = new BeanDefinition()
        factoryBeanDefinition.setId('factory')
        factoryBeanDefinition.setBeanClassName('com.lysenkova.ioc.testentities.BeanFactoryPostProcessorTestEntity')
        factoryBeanDefinitions.add(factoryBeanDefinition)
        def beanDefinitions = context.getBeanDefinitions()
        BeanFactoryPostProcessorInvoker invoker = new BeanFactoryPostProcessorInvoker(beanDefinitions, factoryBeanDefinitions)
        for (beanDefinition in beanDefinitions) {
            invoker.invokePostProcessBeanFactoryMethod()
        }

        def expected = "1000"
        def actual = beanDefinitions.get(0).getDependencies().get('maxAmount')
        assert expected == actual
    }
}
