package com.lysenkova.ioc.testentities.providers

import com.lysenkova.ioc.entity.BeanDefinition
import com.lysenkova.ioc.testentities.MailService
import com.lysenkova.ioc.testentities.UserService
import com.lysenkova.ioc.testentities.PaymentService

import org.testng.annotations.DataProvider

class BeanDefinitionDataProvider {
    @DataProvider(name="provideBeanDefinitions")
    static Object[][] provideBeanDefinitions() {
        def beanDefinitions = new ArrayList<>()

        def beanPropertiesOne = new HashMap<>()
        beanPropertiesOne.put('protocol', 'SMTP')
        beanPropertiesOne.put('port', '8080')
        def beanRefDependenciesOne = new HashMap<>()
        BeanDefinition beanDefinitionOne = new BeanDefinition(
                id: 'mailService', beanClassName: 'com.lysenkova.ioc.testentities.MailService', dependencies: beanPropertiesOne, refDependencies: beanRefDependenciesOne)
        beanDefinitions.add(beanDefinitionOne)

        def beanPropertiesTwo = new HashMap<>()
        def beanRefDependenciesTwo = new HashMap<>()
        beanRefDependenciesTwo.put('mailService', 'mailService')
        BeanDefinition beanDefinitionTwo = new BeanDefinition(
                id: 'userService', beanClassName: 'com.lysenkova.ioc.testentities.UserService', dependencies: beanPropertiesTwo, refDependencies: beanRefDependenciesTwo)
        beanDefinitions.add(beanDefinitionTwo)

        def beanPropertiesThree = new HashMap<>()
        beanPropertiesThree.put('maxAmount', '5000')
        def beanRefDependenciesThree = new HashMap<>()
        beanRefDependenciesThree.put('mailService', 'mailService')
        BeanDefinition beanDefinitionThree = new BeanDefinition(
                id: 'paymentWithMaxService', beanClassName: 'com.lysenkova.ioc.testentities.PaymentService', dependencies: beanPropertiesThree, refDependencies: beanRefDependenciesThree)
        beanDefinitions.add(beanDefinitionThree)

        def beanPropertiesFour = new HashMap<>()
        def beanRefPropertiesFour = new HashMap<>()
        beanRefPropertiesFour.put('mailService', 'mailService')
        BeanDefinition beanDefinitionFour = new BeanDefinition(
                id: 'paymentService', beanClassName: 'com.lysenkova.ioc.testentities.PaymentService', dependencies: beanPropertiesFour, refDependencies: beanRefPropertiesFour)
        beanDefinitions.add(beanDefinitionFour)

        def array = new Object[1][]
        array[0] = [beanDefinitions] as Object[]
        return array
    }

    @DataProvider(name="provideBeanDefinitionPaths")
    static Object[][] provideBeanDefinitionPaths() {
        def beanDefinitions = new ArrayList<>()

        def beanPropertiesOne = new HashMap<>()
        beanPropertiesOne.put("protocol", "SMTP")
        beanPropertiesOne.put("port", "8080")
        def beanRefDependenciesOne = new HashMap<>()
        BeanDefinition beanDefinitionOne = new BeanDefinition(
                id: 'mailService', beanClassName: 'com.lysenkova.ioc.testentities.MailService', dependencies: beanPropertiesOne, refDependencies: beanRefDependenciesOne)
        beanDefinitions.add(beanDefinitionOne)

        def beanPropertiesTwo = new HashMap<>()
        def beanRefDependenciesTwo = new HashMap<>()
        beanRefDependenciesTwo.put("mailService", "mailService")
        BeanDefinition beanDefinitionTwo = new BeanDefinition(
                id: 'userService', beanClassName: 'com.lysenkova.ioc.testentities.UserService', dependencies: beanPropertiesTwo, refDependencies: beanRefDependenciesTwo)
        beanDefinitions.add(beanDefinitionTwo)

        def beanPropertiesThree = new HashMap<>()
        def beanRefDependenciesThree = new HashMap<>()
        beanRefDependenciesThree.put("mailService", "mailService")
        BeanDefinition beanDefinitionThree = new BeanDefinition(
                id: 'paymentWithMaxService', beanClassName: 'com.lysenkova.ioc.testentities.PaymentService', dependencies: beanPropertiesThree, refDependencies: beanRefDependenciesThree)
        beanDefinitions.add(beanDefinitionThree)

        def beanPropertiesFour = new HashMap<>()
        beanPropertiesFour.put("maxAmount", "5000")
        def beanRefPropertiesFour = new HashMap<>()
        beanRefPropertiesFour.put("mailService", "mailService")
        BeanDefinition beanDefinitionFour = new BeanDefinition(
                id: 'paymentService', beanClassName: 'com.lysenkova.ioc.testentities.PaymentService', dependencies: beanPropertiesFour, refDependencies: beanRefPropertiesFour)
        beanDefinitions.add(beanDefinitionFour)

        def beanPropertiesFive = new HashMap<>()
        beanPropertiesFive.put("protocol", "POP3")
        beanPropertiesFive.put("port", "3000")
        def beanRefDependenciesFive = new HashMap<>()
        BeanDefinition beanDefinitionFive = new BeanDefinition(
                id: 'mailService', beanClassName: 'com.lysenkova.ioc.testentities.MailService', dependencies: beanPropertiesFive, refDependencies: beanRefDependenciesFive)
        beanDefinitions.add(beanDefinitionFive)

        def array = new Object[1][]
        array[0] = [beanDefinitions] as Object[]
        return array
    }

    @DataProvider(name="provideBeanNames")
    static Object[][] provideBeanNames() {
        def beanNames = ['mailService','userService', 'paymentWithMaxService', 'paymentService']
        def beanNamesArray = new Object[1][]
        beanNamesArray[0] = [beanNames] as Object[]
        return beanNamesArray
    }
}
