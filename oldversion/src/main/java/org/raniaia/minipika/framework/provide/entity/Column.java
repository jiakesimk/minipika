package org.jiakesiws.minipika.framework.provide.entity;

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
 * Creates on 2019/11/12.
 */

import java.lang.annotation.*;

/**
 *
 * This annotation only to have {@link Entity} annotation the class effective.
 *
 * Entity class the column annotation, database column sql just write
 * statement script,no need write column name.
 *
 * If we have a age column now, we just need write sql script 'int(3) not null default 18',
 * as for column name minipika will auto create.
 *
 * Entity class the column property annotation.
 *
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    /**
     * 字段声明代码，例如: 'int(1) not null'
     *
     * sql column statement code.
     */
    String value() default "";

}
