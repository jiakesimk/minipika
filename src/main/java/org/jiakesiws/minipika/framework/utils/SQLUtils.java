package org.jiakesiws.minipika.framework.utils;

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
 * Creates on 2020/1/30.
 */

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public class SQLUtils
{

  /**
   * 获取sql中的表名
   *
   * @param sql
   * @return 表名集合
   */
  public static List<String> getSQLTables(String sql)
  {
    TablesNamesFinder finder = new TablesNamesFinder();
    Statement statement = null;
    try
    {
      statement = CCJSqlParserUtil.parse(sql);
    } catch (JSQLParserException e)
    {
      return new ArrayList<>();
    }
    List<String> tables = finder.getTableList(statement);
    for (int i = 0, len = tables.size(); i < len; i++)
    {
      tables.set(i, tables.get(i).replace("`", ""));
    }
    return tables;
  }

  /**
   * 构建带参数的sql
   */
  public static String buildPreSQL(String sql, Object[] args)
  {
    StringBuilder builder = new StringBuilder();
    char[] charArray = sql.toCharArray();
    int index = -1;
    for (int i = 0; i < charArray.length; i++)
    {
      char ch = charArray[i];
      if (ch == '?')
      {
        Object object = args[index = (index + 1)];
        if (object instanceof String)
        {
          builder.append("'");
          builder.append(object);
          builder.append("'");
        } else
        {
          builder.append(object);
        }
      } else
      {
        builder.append(ch);
      }
    }
    return builder.toString();
  }

}
