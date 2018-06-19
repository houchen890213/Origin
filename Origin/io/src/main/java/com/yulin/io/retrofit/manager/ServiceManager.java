package com.yulin.io.retrofit.manager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liu_lei on 2017/6/22.
 */

public class ServiceManager {

    private Map<String, Class<?>> serviceClassMap = new HashMap<>();
    private Map<String, Object> serviceMap = new HashMap<>();
    private InvocationHandler invocationHandler;

    protected ServiceManager(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    protected ServiceManager() {

    }

    public <T> T getService(String serviceName) {
        T service = (T) serviceMap.get(serviceName);
        if (service == null) {
            Class<?> classType = serviceClassMap.get(serviceName);
            if (classType != null) {
                try {
                    service = (T) classType.newInstance();
                    serviceMap.put(serviceName, service);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return service;
    }


    public <T> T getService(Class<T> tClass) {
        T service = (T) serviceMap.get(tClass.getName());
        if (service == null) {
            service = (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, invocationHandler);
            if( service != null ) {
                serviceMap.put(tClass.getName(),service);
            }
        }
        return service;
    }

    public void remove(String serviceName) {
        serviceMap.remove(serviceName);
    }

}
