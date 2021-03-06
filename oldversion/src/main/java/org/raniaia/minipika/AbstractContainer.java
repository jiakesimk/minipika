package org.jiakesiws.minipika;

/* ************************************************************************
 *
 * Copyright (C) 2020 2B键盘 All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ************************************************************************/

/*
 * Creates on 2020/3/21.
 */

import org.jiakesiws.minipika.components.config.GlobalConfig;
import org.jiakesiws.minipika.components.jdbc.datasource.unpooled.Dsi;
import org.jiakesiws.minipika.framework.mapper.MapperInvocation;
import org.jiakesiws.minipika.framework.provide.Minipika;
import org.jiakesiws.minipika.framework.provide.component.Component;
import org.jiakesiws.minipika.framework.provide.component.ComponentType;
import org.jiakesiws.minipika.framework.sql.SqlMapper;
import org.jiakesiws.minipika.framework.tools.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
@SuppressWarnings("unchecked")
public abstract class AbstractContainer extends RootContainer<String, Object> {

    protected AbstractContainer() {

    }

    public AbstractContainer(Map<String, Class<?>> components) {
        putComponents(components);
    }

    public <T> T newInstance(Class<?> clazz) {
        Object instance = newInstance(clazz, null);
        return (T) minipika(instance);
    }

    public <T> T newInstance(Class<?> clazz, Object... args) {
        Class<?>[] parametersTypes = null;
        if (args != null) {
            parametersTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parametersTypes[i] = args[i].getClass();
            }
        }
        //
        // If class is interface and class name equals mapper xml name.
        // Then think of class as a mapper interface.
        //
        if (clazz.isInterface()) {
            String classname = clazz.getSimpleName();
            if (SqlMapper.isMapper(classname)) {
                return (T) MapperInvocation.invoker(clazz);
            }
        }
        if (parametersTypes == null) {
            return (T) Classic.newInstance(clazz);
        }
        Object instance = Classic.newInstance(clazz, parametersTypes, args);
        return (T) minipika(instance);
    }

    /**
     * Get bean by {@code Class} from {@link RootContainer}.
     *
     * @see RootContainer#getRoots0
     */
    protected <T> T get0(Class<?> clazz) {
        return get0(getSimpleName(clazz));
    }

    /**
     * Get bean by {@code String} from {@link RootContainer}.
     *
     * @see RootContainer#getRoots0
     */
    protected <T> T get0(String name) {
        Object instance = getRoots0(name);
        if (instance == null) {
            Class<?> component = getComponents0(name);
            if (component == null) return null;
            instance = Classic.newInstance(component);
            submitBean(name, minipika(instance));
        }
        return (T) instance;
    }

    public void loadComponents(Class<?>... statements) {
        for (Class<?> statement : statements) {
            Method[] methods = Methods.getDeclaredMethods(statement, true);
            for (Method method : methods) {
                Component component = Annotations.isAnnotation(method, Component.class);
                if (component != null) {
                    Object object = Methods.invoke(method);
                    if (component.type() == ComponentType.DEFAULT) {
                        submitBean(getSimpleName(object), object);
                    } else {
                        String defaultName = component.name();
                        if(StringUtils.isEmpty(defaultName)){
                            defaultName = getSimpleName(object);
                        }
                        putParameter(defaultName, object);
                    }
                }
            }
        }
    }

    /**
     * minipika object.
     */
    protected Object minipika(Object instance) {
        Class<?> clazz = instance.getClass();
        Field[] fields = Fields.getDeclaredFields(clazz, true);
        for (Field field : fields) {
            Minipika valid = Annotations.isAnnotation(field, Minipika.class);
            if (valid != null) {
                String componentName = field.getType().getSimpleName();
                Object componentInstance = getRoots0(componentName);
                if (componentInstance == null) {
                    Class<?> componentClass = getComponents0(componentName);
                    if (componentClass == null) {
                        throw new NullPointerException("cannot found [" + componentName + "] component");
                    }
                    if ("DataSource".equals(componentName)) {
                        componentInstance = Classic.newInstance(componentClass, new Class<?>[]{Dsi.class},
                                GlobalConfig.getConfig().getDsi());
                    } else {
                        componentInstance = Classic.newInstance(componentClass);
                    }
                    minipika(componentInstance);
                }
                Fields.set(instance, componentInstance, field);
                submitBean(getComponentName(componentInstance.getClass()),
                        componentInstance);
            }
        }
        return instance;
    }

}
