package com.lysenkova.ioc.context

import org.junit.Test

class ClassPathApplicationContextTest  {

    @Test
    void testCreateBeansFromBeanDefinitions() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("email-context.xml")
        def expected = 1
        def actual = new ArrayList()
        actual.add(applicationContext.getBean('mailService'))

        assert expected == actual.size()

    }
}
