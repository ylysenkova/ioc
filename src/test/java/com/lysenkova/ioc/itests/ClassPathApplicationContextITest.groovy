package com.lysenkova.ioc.itests

import com.lysenkova.ioc.context.ApplicationContext
import com.lysenkova.ioc.context.ClassPathApplicationContext
import com.lysenkova.ioc.exception.NotUniqueBeanExeption
import com.lysenkova.ioc.itests.providers.BeanDefinitionDataProvider
import com.lysenkova.ioc.beanparser.BeanDefinitionReader
import com.lysenkova.ioc.beanparser.xml.XMLBeanDefinitionReader
import com.lysenkova.ioc.exception.BeanInstantiationException
import com.lysenkova.ioc.testentities.AllDataTypesStorage
import com.lysenkova.ioc.testentities.MailService
import com.lysenkova.ioc.testentities.MailServiceImpl
import com.lysenkova.ioc.testentities.PaymentService
import com.lysenkova.ioc.testentities.UserServiceWithoutSetter
import org.testng.annotations.Test

import static org.testng.Assert.*


class ClassPathApplicationContextITest {

    @Test
    void getBeanByClassName() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("email-context.xml")
        MailService mailService = applicationContext.getBean(MailService.class)

        String expectedProtocol = "POP3"
        String actualProtocol = mailService.getProtocol()
        assertEquals(actualProtocol, expectedProtocol)

        int expectedPort = 3000
        int actualPort = mailService.getPort()
        assertEquals(actualPort, expectedPort)
    }

    @Test
    void getBeanByIdAndClassName() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("email-context.xml")
        MailService mailService = applicationContext.getBean("mailService", MailService.class)

        String expectedProtocol = "POP3"
        String actualProtocol = mailService.getProtocol()
        assertEquals(actualProtocol, expectedProtocol)

        int expectedPort = 3000
        int actualPort = mailService.getPort()
        assertEquals(actualPort, expectedPort)
    }

    @Test
    void getBeanById() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("email-context.xml")
        MailServiceImpl mailService = applicationContext.getBean("mailService")

        String expectedProtocol = "POP3"
        String actualProtocol = mailService.getProtocol()
        assertEquals(actualProtocol, expectedProtocol)

        int expectedPort = 3000
        int actualPort = mailService.getPort()
        assertEquals(actualPort, expectedPort)
    }

    @Test(dataProvider = "provideBeanNames", dataProviderClass = BeanDefinitionDataProvider.class)
    void getBeanNames(List<String> expectedBeanNames) {
        ApplicationContext applicationContext = new ClassPathApplicationContext("email-context.xml", "context.xml")
        def actualBeanNames = applicationContext.getBeanNames()
        assertTrue(actualBeanNames.containsAll(expectedBeanNames))
    }

    @Test(dataProvider = "provideBeanNames", dataProviderClass = BeanDefinitionDataProvider.class)
    void setBeanDefinitionReader(List<String> expectedBeanNames) {
        BeanDefinitionReader reader = new XMLBeanDefinitionReader("email-context.xml", "context.xml")
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.setBeanDefinitionReader(reader)
        def actualBeanNames = applicationContext.getBeanNames()
        assertEquals(actualBeanNames.size(), expectedBeanNames.size())
        assertTrue(actualBeanNames.containsAll(expectedBeanNames))
    }

    @Test(expectedExceptionsMessageRegExp = "Reference dependency mailService can not be inserted", expectedExceptions = BeanInstantiationException.class)
    void getBeanWithoutSetterTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("without-setter-context.xml")
        applicationContext.getBean(UserServiceWithoutSetter.class)
    }

    @Test(expectedExceptionsMessageRegExp = "Can not get setter for field: port", expectedExceptions = BeanInstantiationException.class)
    void getBeanWithIncorrectPropertyType() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("incorrect-type-context.xml")
        applicationContext.getBean("mailService")
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with id: paymentService has already been initialized.", expectedExceptions = BeanInstantiationException.class)
    void getBeanWithTheSameId() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("email-context.xml", "equals-id-context.xml")
        applicationContext.getBean(PaymentService.class)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean creation error.", expectedExceptions = BeanInstantiationException.class)
    void getBeanWithAbsentClass() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("absent-class-context.xml")
        applicationContext.getBean("userService")
    }

    @Test(expectedExceptionsMessageRegExp = "For class com.lysenkova.ioc.testentities.PaymentService more than 1 bean initialized.", expectedExceptions = NotUniqueBeanExeption.class)
    void getBeanForTheSameClass() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("context.xml", "email-context.xml")
        applicationContext.getBean(PaymentService.class)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with id: mail not found.", expectedExceptions = BeanInstantiationException.class)
    void getBeanForInvalidIdByNameAndClass() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("context.xml", "email-context.xml")
        applicationContext.getBean("mail", PaymentService.class)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with id: mail not found.", expectedExceptions = BeanInstantiationException.class)
    void getBeanForInvalidIdByName() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("context.xml", "email-context.xml")
        applicationContext.getBean("mail")
    }

    @Test
    void getDependencyTypeTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("data-type-context.xml")
        AllDataTypesStorage allDataTypesStorage = applicationContext.getBean(AllDataTypesStorage.class)
        assertEquals(allDataTypesStorage.getIntType(), 1)
        assertEquals(allDataTypesStorage.getIntegerType(), 1)
        assertEquals(allDataTypesStorage.getShortType(), 11)
        assertEquals(allDataTypesStorage.getShortBigType(), 11)
        assertEquals(allDataTypesStorage.getLongType(), 111L)
        assertEquals(allDataTypesStorage.getLongBigType(), 111L)
        assertEquals(allDataTypesStorage.getDoubleType(), 1.11d)
        assertEquals(allDataTypesStorage.getDoubleBigType(), 1.11d)
        assertEquals(allDataTypesStorage.getCharType(), 'c' as char)
        assertEquals(allDataTypesStorage.getCharacterType(), 'c' as char)
        assertEquals(allDataTypesStorage.getFloatType(), 2.22f)
        assertEquals(allDataTypesStorage.getFloatBigType(), 2.22f)
        assertEquals(allDataTypesStorage.getByteType(), -128)
        assertEquals(allDataTypesStorage.getByteBigType(), -128)
        assertTrue(allDataTypesStorage.getBooleanType())
        assertTrue(allDataTypesStorage.getBooleanBigType())
        assertEquals(allDataTypesStorage.getStringType(), 'string')
        assertEquals(allDataTypesStorage.getMailService(), applicationContext.getBean('mailService'))
    }
}


