package com.bjoggis.dev.util;

import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MyDetailsQuartzJobBean extends QuartzJobBean {

    private String targetObject;
    private String targetMethod;

    private String arguments;
    private ApplicationContext ctx;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        try {
            Object bean = ctx.getBean(targetObject);

            List<String> args = null;
            Method m = null;
            if (arguments != null) {
                args = convertStringArgumentsToList(arguments);
                m = bean.getClass().getMethod(targetMethod, List.class);
                m.invoke(bean, args);

            } else {
                m = bean.getClass().getMethod(targetMethod);
                m.invoke(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> convertStringArgumentsToList(String arguments) {
        String stripped = arguments.replaceAll("\\s", "");
        return Arrays.asList(stripped.split(","));
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

}
