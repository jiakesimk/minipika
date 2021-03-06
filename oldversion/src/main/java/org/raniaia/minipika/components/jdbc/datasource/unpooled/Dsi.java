package org.jiakesiws.minipika.components.jdbc.datasource.unpooled;

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

import lombok.Getter;
import lombok.Setter;

import org.jiakesiws.minipika.framework.loader.NativeClassLoader;
import org.jiakesiws.minipika.framework.tools.Maps;
import org.jiakesiws.minipika.framework.tools.StringUtils;

import java.sql.Driver;
import java.util.Map;
import java.util.Properties;

/**
 * DataSource info
 *
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
@Getter
@Setter
public class Dsi {

  // 存放驱动实例
  static Map<String, Driver> registerDrivers = Maps.newConcurrentHashMap();

  protected Object id;
  protected String url;
  protected String driver;
  protected String username;
  protected String password;
  protected String sourceType;

  ClassLoader driverClassLoader;

  // 是否设置为自动提交
  Boolean autoCommit;

  public Dsi() {
  }

  public Dsi(String url, String driver, String username,
             String password) {
    this(url, driver, username, password, false, new NativeClassLoader());
  }

  public Dsi(String url, String driver, String username,
             String password, boolean autoCommit) {
    this(url, driver, username, password, autoCommit, new NativeClassLoader());
  }

  public Dsi(String url, String driver, String username,
             String password, Boolean autoCommit, ClassLoader classLoader) {
    this.url = url;
    this.driver = driver;
    this.username = username;
    this.password = password;
    this.autoCommit = autoCommit;
    this.driverClassLoader = classLoader;
  }

  static Properties buildDriverInfo(String username, String password) {
    final Properties info = new Properties();
    if (!StringUtils.isEmpty(username)) {
      info.setProperty("user", username);
    }
    if (!StringUtils.isEmpty(password)) {
      info.setProperty("password", password);
    }
    return info;
  }

}
