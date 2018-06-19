package com.yulin.io.retrofit.invocation;

import com.yulin.io.retrofit.annotation.ServiceMethod;
import com.yulin.io.retrofit.annotation.ServiceType;
import com.yulin.io.retrofit.exception.NetworkException;
import com.yulin.io.retrofit.listener.OnApiListener;
import com.yulin.io.retrofit.observer.DefObserver;
import com.yulin.io.retrofit.okhttp.OkHttpUtils;
import com.yulin.io.retrofit.response.DefResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liu_lei on 2017/6/22.
 */

public class RetrofitServiceProxy implements InvocationHandler {

    private Map<String, Method> serviceMethodMap = new HashMap<>();
    private Map<String, Object> serviceClassMap = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> serviceType = getServiceType(method);
        if (serviceType != null && serviceType.isInterface()) {
            return invokeInterface(method, args);
        } else {
            return invokeClass(method, args);
        }
    }

    private Object invokeInterface(final Method method, final Object[] args) throws Throwable {
        final ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.method = method;
        serviceInfo.args = args;
        final Boolean isListener = serviceInfo.method.getReturnType() != Observable.class
                && serviceInfo.args.length > 0
                && serviceInfo.method.getParameterTypes()[serviceInfo.args.length - 1] == OnApiListener.class;

        Observable observable = Observable.just(serviceInfo)
                .subscribeOn(Schedulers.io())
                .map(new Function<ServiceInfo, ServiceInfo>() {
                    @Override
                    public ServiceInfo apply(ServiceInfo serviceInfo) throws Exception {
                        if (isListener) {
                            Object[] newArgs = new Object[serviceInfo.args.length - 1];
                            for (Integer i = 0; i < serviceInfo.args.length - 1; ++i) {
                                newArgs[i] = serviceInfo.args[i];
                            }
                            serviceInfo.args = newArgs;
                        }
                        serviceInfo.method = getServiceMethod(serviceInfo.method);
                        if (serviceInfo.method == null)
                            throw new NetworkException(-1, "serviceMethod null");

                        return serviceInfo;
                    }
                })
                .flatMap(new Function<ServiceInfo, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(ServiceInfo serviceInfo) throws Exception {
                        Class<?> cls = serviceInfo.method.getDeclaringClass();
                        Object service = serviceClassMap.get(cls.getName());
                        if (service == null) {
                            service = OkHttpUtils.getInstance().create(cls);
                            putService(service);
                        }
                        if (service == null)
                            throw new NetworkException(-1, "service null");
                        return  (Observable) serviceInfo.method.invoke(service, serviceInfo.args);
                    }
                });

        if (method.getReturnType() == Observable.class) {
            return observable.map(new Function<DefResponse<?>, Object>() {
                @Override
                public Object apply(final DefResponse<?> defResponse) throws Exception {
                    if (defResponse.getRetCode() == DefResponse.SUCCESS_CODE) {
                        return defResponse.getData();
                    }
                    throw new NetworkException(defResponse.getRetCode(), defResponse.getRetMsg());
                }
            });
        } else if (isListener) {
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new DefObserver((OnApiListener) args[args.length - 1]));
        } else {
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe();
        }
        return null;
    }

    private Object invokeClass(final Method method, final Object[] args) throws Throwable {
        Class<?> cls = getServiceType(method);

        if (cls == null) {
            throw new NetworkException(NetworkException.SERVER_ERROR, "service type is null");
        }

        Object service = serviceClassMap.get(cls.getName());
        if (service == null) {
            service = cls.newInstance();
            putService(service);
        }

        if (service == null)
            throw new NetworkException(NetworkException.SERVER_ERROR, "service null");

        Method serviceMethod = getServiceMethod(method);
        if (serviceMethod == null)
            throw new NetworkException(-1, "serviceMethod null");

        return serviceMethod.invoke(service, args);
    }

    private synchronized Method getServiceMethod(Method method) throws NetworkException {
        Class<?> serviceClass = getServiceType(method);
        if (serviceClass == null) {
            throw new NetworkException(-1, "serviceClass null");
        }

        // 获取注解中指定的方法名
        String methodName = method.getName();
        ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);
        if (serviceMethod != null) {
            methodName = serviceMethod.value();
        }

        String key = serviceClass.getName() + "_" + methodName;
        Method retrofitMethod = serviceMethodMap.get(key);
        if (retrofitMethod == null) {
            Method[] methods = serviceClass.getDeclaredMethods();
            for (Method method1 : methods) {
                String methodName1 = method1.getName();
                ServiceMethod serviceMethod1 = method1.getAnnotation(ServiceMethod.class);
                if (serviceMethod1 != null) {
                    methodName1 = serviceMethod1.value();
                }

                if (methodName1.equals(methodName)) {
                    retrofitMethod = method1;
                    serviceMethodMap.put(key, retrofitMethod);
                    break;
                }
            }
        }

        return retrofitMethod;
    }

    private Class<?> getServiceType(Method method) {
        ServiceType serviceType = method.getAnnotation(ServiceType.class);
        if (serviceType == null) {
            serviceType = method.getDeclaringClass().getAnnotation(ServiceType.class);
        }
        return serviceType != null ? serviceType.value() : null;
    }

    private void putService(Object service) {
        if (service != null) {
            serviceClassMap.put(service.getClass().getName(), service);
        }
    }

    private static class ServiceInfo {
        Method method;
        Object[] args;
    }

}
