package org.jiakesiws.minipika.components.configuration;

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
 * Creates on 2020/6/1.
 */

import org.jiakesiws.minipika.components.configuration.node.DataSourceNode;
import org.jiakesiws.minipika.components.configuration.node.ElementParser;
import org.jiakesiws.minipika.components.configuration.node.PropertiesNode;
import org.jiakesiws.minipika.components.configuration.wrapper.ElementWrapper;

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public class XMLConfig implements ElementParser {

  public static final String CHIlD_DATASOURCE         = "datasource";

  public static final String CHIlD_PROPERTIES         = "properties";

  public static final String CHILD_CONFIGURATION      = "configuration";

  /**
   * 所有配置的属性值
   */
  protected PropertiesNode propertiesNode;

  /**
   * 配置的数据源
   */
  protected DataSourceNode dataSourceNode;

  @Override
  public void parse(ElementWrapper element) {
    // 解析properties节点
    ElementWrapper properties = element.getChild(CHIlD_PROPERTIES);
    propertiesNode = new PropertiesNode();
    if(properties.getE() != null) {
      propertiesNode.parse(properties);
    }
    // 解析configuration节点
    ElementWrapper configuration = element.getChild(CHILD_CONFIGURATION);
    if(configuration.getE() != null) {
      propertiesNode.parse(configuration);
    }
    // 解析datasource节点
    ElementWrapper datasource = element.getChild(CHIlD_DATASOURCE);
    if(datasource.getE() != null) {
      dataSourceNode = new DataSourceNode();
      dataSourceNode.parse(datasource);
    }
  }

}
