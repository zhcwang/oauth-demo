package com.felix.oauth2resource.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * String context utils
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * injected application context
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtils.applicationContext == null) {
            SpringContextUtils.applicationContext = applicationContext;
        }
    }

    /**
     * get application context
     *
     * @return application context
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * get bean by name
     * 
     * @param name bean name
     * @return bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * get bean by class
     * 
     * @param clazz bean class
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * get bean by name and class
     * 
     * @param name  bean name
     * @param clazz bean class
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * get property value
     * 
     * @param key key
     * @return property value
     */
    public static String getProperty(String key) {
        return SpringContextUtils.getApplicationContext().getEnvironment().getRequiredProperty(key);
    }

    /**
     * get property value
     * 
     * @param key          key
     * @param defaultValue default value
     * @return property value
     */
    public static String getProperty(String key, String defaultValue) {
        return SpringContextUtils.getApplicationContext().getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * get property value
     * 
     * @param key
     * @param clazz target type
     * @return property value
     */
    public static <T> T getProperty(String key, Class<T> clazz) {
        return SpringContextUtils.getApplicationContext().getEnvironment().getProperty(key, clazz);
    }

    /**
     * get property value
     * 
     * @param key
     * @param clazz        target type
     * @param defaultValue default value
     * @return property value
     */
    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue) {
        return SpringContextUtils.getApplicationContext().getEnvironment().getProperty(key, clazz, defaultValue);
    }
}
