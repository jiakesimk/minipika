package org.jiakesiws.minipika.components.config.components;

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
 * Creates on 2019/11/4.
 */

import org.jiakesiws.minipika.components.config.AbstractConfig;
import org.jiakesiws.minipika.framework.tools.*;

/**
 * 配置类
 *
 * Config object.
 *
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public final class PropertiesConfig extends AbstractConfig {

    // 配置文件地址
    private String configPath = "resources/minipika.properties";

    private static PropertiesConfig instance;

    public static PropertiesConfig getInstance() {
        if (instance == null) {
            instance = new PropertiesConfig("resources/minipika.properties");
        }
        return instance;
    }

    public PropertiesConfig(String configPath) {
        super(MinipikaIOUtils.getResourceAsProperties(configPath));
        instance = this;
    }


}
