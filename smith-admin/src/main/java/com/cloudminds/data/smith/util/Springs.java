package com.cloudminds.data.smith.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Spring上下文处理
 *
 * @author james
 * @date 2020/1/8 15:41
 * @des
 */
@Component
public class Springs implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Springs.applicationContext = applicationContext;
    }

    /**
     * 根据类获取Bean
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    /**
     * 根据名称获取bean
     *
     * @param name
     * @return
     */
    public static Object getBean(final String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获取指定类型所有的Bean集合
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(final Class<T> tClass) {
        return applicationContext.getBeansOfType(tClass);
    }


    /**
     * 发布事件
     *
     * @param event
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    /**
     * 获取上下文
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取环境
     *
     * @return
     */
    public static Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

}
