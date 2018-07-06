package com.lysenkova.ioc.testentities;

import com.lysenkova.ioc.applicationcontext.BeanFactoryPostProcessor;
import com.lysenkova.ioc.entity.BeanDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFactoryPostProcessorTestEntity implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if(beanDefinition.getId().equalsIgnoreCase("paymentWithMaxService")) {
                Map<String, String> dependency = new HashMap<>();
                dependency.put("maxAmount", "1000");
                beanDefinition.setDependencies(dependency);
            }
        }
    }
}
