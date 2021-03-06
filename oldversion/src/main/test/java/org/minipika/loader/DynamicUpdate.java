package org.jiakesiws.minipika.loader;

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



import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import org.jiakesiws.minipika.framework.loader.NativeClassLoader;

/**
 * Copyright: Create by 2B键盘 on 2019/12/19 10:24
 */
public class DynamicUpdate {

    public static void main(String[] args) throws Exception {

        loader("public static void fuck(){ System.out.println(\"hello world by dynamic2 create!\"); }","fuck");
        loader("public static void fuck1(){ System.out.println(\"hello world by dynamic1 create!\"); }","fuck1");

    }

    public static void loader(String method,String name) throws Exception {
        DynamicUpdate du = new DynamicUpdate();
        String fullClassName = "org.raniaia.minipika.javassist.DynamicUpdate";
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(fullClassName);
        ctClass.defrost();
        CtMethod newMethod = CtNewMethod.make(method, ctClass);
        ctClass.addMethod(newMethod);
        NativeClassLoader classLoader = new NativeClassLoader();
        // todo exception：Exception in thread "main" java.lang.LinkageError: loader (instance of  org/raniaia/minipika/framework/loader/MinipikaClassLoader): attempted  duplicate class definition for name: "org/raniaia/minipika/javassist/DynamicUpdate"
        // todo 解决方案：https://blog.csdn.net/is_zhoufeng/article/details/26602689
        Class<?> target = classLoader.findClassByBytes(fullClassName,ctClass.toBytecode());
        Object copy = classLoader.getObject(target,du);

        copy.getClass().getDeclaredMethod(name).invoke(copy);
    }

}
