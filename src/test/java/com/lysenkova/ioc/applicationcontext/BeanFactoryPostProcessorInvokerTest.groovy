package com.lysenkova.ioc.applicationcontext

import com.lysenkova.ioc.entity.BeanDefinition

class BeanFactoryPostProcessorInvokerTest extends GroovyTestCase {
    void testInvokePostProcessBeanFactoryMethod() {

        ApplicationContext context = new ClassPathApplicationContext("payment-context.xml")
        def factoryBeanDefinitions = new ArrayList()
        BeanDefinition factoryBeanDefinition = new BeanDefinition()
        factoryBeanDefinition.setId('factory')
        factoryBeanDefinition.setBeanClassName('com.lysenkova.ioc.testentities.BeanFactoryPostProcessorTestEntity')
        factoryBeanDefinitions.add(factoryBeanDefinition)
        def beanDefinitions = context.getBeanDefinitions()
        BeanFactoryPostProcessorInvoker invoker = new BeanFactoryPostProcessorInvoker(beanDefinitions, factoryBeanDefinitions)
        for(beanDefinition in beanDefinitions){
            invoker.invokePostProcessBeanFactoryMethod()
        }

        def expected = "1000"
        def actual = beanDefinitions.get(0).getDependencies().get('maxAmount')
        assertEquals(expected, actual)
    }
}
