package org.laniakeamly.poseidon.javassist;

import javassist.*;
import org.laniakeamly.poseidon.framework.sql.xml.build.DynamicMethod;

import java.lang.reflect.Method;

/**
 * Create by 2BKeyboard on 2019/12/19 10:24
 */
public class DynamicUpdate {

    public static void main(String[] args) throws Exception {

        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("org.laniakeamly.poseidon.javassist.DynamicUpdate");
        CtMethod newMethod = CtNewMethod.make("public static void fuck(){ System.out.println(\"hello world by dynamic create!\"); }", ctClass);
        ctClass.addMethod(newMethod);

        Class<?> target = DynamicUpdate.class;
        Object obj = target.newInstance();

        Method method = target.getDeclaredMethod("fuck");
        method.invoke(obj);

        System.out.println();

    }

}
