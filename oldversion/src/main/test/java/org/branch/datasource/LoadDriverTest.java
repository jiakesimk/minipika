package org.branch.datasource;

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
 * Creates on 2020/3/25.
 */

import org.junit.Test;
import org.jiakesiws.minipika.framework.loader.NativeClassLoader;

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public class LoadDriverTest {

    @Test
    public void loadDriver() throws ClassNotFoundException {
        Class<?> driverClass = Class.forName("com.mysql.cj.jdbc.Driver",
                true, new NativeClassLoader());
        System.out.println(driverClass.toGenericString());
    }

}
