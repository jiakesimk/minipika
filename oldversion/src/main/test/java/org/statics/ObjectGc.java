package org.statics;

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



/**
 * Copyright by 2B键盘 on 2020/3/3 15:54
 * @author 2B键盘
 * @version 1.0.0
 * @since 1.8
 */
public class ObjectGc {

    @Override
    protected void finalize() throws Throwable {
        System.out.println("对象被回收 - [" + this + "]");
    }
}
