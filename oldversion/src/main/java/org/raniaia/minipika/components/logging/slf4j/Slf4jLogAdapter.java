package org.jiakesiws.minipika.components.logging.slf4j;

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
 * Creates on 2020/3/26.
 */

import org.jiakesiws.minipika.components.logging.Log;
import org.jiakesiws.minipika.components.logging.LogAdapter;
import org.slf4j.LoggerFactory;

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public class Slf4jLogAdapter implements LogAdapter {

    public Log getLog(String key){
        return new Slf4jLog(LoggerFactory.getLogger(key));
    }

    public Log getLog(Class<?> key){
        return new Slf4jLog(LoggerFactory.getLogger(key));
    }

}
