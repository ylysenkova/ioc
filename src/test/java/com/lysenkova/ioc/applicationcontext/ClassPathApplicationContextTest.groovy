package com.lysenkova.ioc.applicationcontext

class ClassPathApplicationContextTest extends GroovyTestCase {
    void testCreateBeansFromBeanDefinitions() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("email-context.xml")
        def expected = 1
        def actual = new ArrayList()
        actual.add(applicationContext.getBean('mailService'))

        assertEquals(expected, actual.size())

    }
}
