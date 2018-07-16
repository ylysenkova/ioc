package com.lysenkova.ioc.applicationcontext.itests

import com.lysenkova.ioc.applicationcontext.ApplicationContext
import com.lysenkova.ioc.applicationcontext.BeanFactoryPostProcessorInvoker
import com.lysenkova.ioc.applicationcontext.ClassPathApplicationContext
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
