package org.jiakesiws.minipika.components.jdbc;

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
 * Creates on 2020/03/24.
 */

import com.alibaba.fastjson.JSONObject;
import org.jiakesiws.minipika.framework.utils.ClassUtils;
import org.jiakesiws.minipika.framework.utils.Lists;
import org.jiakesiws.minipika.framework.utils.Maps;
import org.jiakesiws.minipika.framework.utils.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("unchecked")
public class ConstResultSet implements NativeResultSet
{

  private int nextOffset = 0;
  private int hasNextOffset = 0;
  private List<String> hasNext;

  private List<Map<String, String>> resultSet;

  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public ConstResultSet()
  {
  }

  public ConstResultSet(ResultSet input)
  {
    build(input);
  }

  @Override
  public NativeResultSet build(ResultSet rset)
  {
    try
    {
      resultSet = Lists.newArrayList();
      ResultSetMetaData mdata = rset.getMetaData();
      int len = mdata.getColumnCount();
      while (rset.next())
      {
        Map<String, String> resultMap = new LinkedHashMap<>(len);
        for (int j = 0; j < len; j++)
        {
          int jtemp = j + 1;

          String name = mdata.getColumnLabel(jtemp);
          String value = rset.getString(name);

          resultMap.put(name, value);
        }
        resultSet.add(resultMap);
      }

    } catch (Exception e)
    {
      e.printStackTrace();
    } finally
    {
      try
      {
        if (rset != null) rset.close();
      } catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return this;
  }

  @Override
  public <T> List<T> conversionJavaList(Class<T> target)
  {
    try
    {
      if (target.equals(String.class)) return conversionJavaStringList();
      if (target.equals(Integer.class)) return conversionJavaIntegerList();
      if (target.equals(BigDecimal.class)) return conversionJavaBigDecimalList();
      if (target.equals(Long.class)) return conversionJavaLongList();
      if (target.equals(Double.class)) return conversionJavaDoubleList();
      if (target.equals(Short.class)) return conversionJavaShortList();
      if (target.equals(Float.class)) return conversionJavaFloatList();
      if (target.equals(Boolean.class)) return conversionJavaBooleanList();
      if (target.equals(Byte.class)) return conversionJavaByteList();
      if (target.equals(BigInteger.class)) return conversionJavaBigIntegerList();
      if (target.equals(Date.class)) return conversionJavaDateList();
      return conversionEntityList(target);
    } catch (Throwable e)
    {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public <T> T conversionJavaBean(Class<T> target) throws SQLException
  {
    try
    {
      if (!resultSet.isEmpty())
      {
        return conversionJavaBean(target, resultSet.get(0));
      }
    } catch (Exception e)
    {
      throw new SQLException(e.getMessage(), e);
    }
    return null;
  }

  private <T> T conversionJavaBean(Class<T> target, Map<String, String> resultMap) throws Exception
  {
    if (resultSet.isEmpty()) return null;
    List<String> names = Lists.newArrayList();
    T entity;
    if (resultMap == null || resultMap.isEmpty()) return null;
    Object v1 = isBase(target, String.valueOf(Maps.getFirstValue(resultMap)));
    if (v1 instanceof Exception) return null;
    if (v1 != null) return (T) v1;
    entity = ClassUtils.newInstance(target);
    for (Field field : target.getDeclaredFields())
    {
      names.add(field.getName());
    }
    for (Map.Entry<String, String> v : resultMap.entrySet())
    {
      String hump = StringUtils.underlineToHump(v.getKey());
      if (!names.contains(hump)) continue;                                 // 判断Entity中是否含有hump字段
      Field field = target.getDeclaredField(hump);
      field.setAccessible(true);
      setValue(field, v.getValue(), entity);
    }
    return entity;
  }

  /**
   * 转换成Entity List
   *
   * @param target 实例类型
   * @return 实例化后的实体集合
   */
  private <T> List<T> conversionEntityList(Class<T> target) throws Exception
  {
    List<T> javaBeans = Lists.newCopyOnWriteArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      javaBeans.add(conversionJavaBean(target, resultMap));
    }
    return javaBeans;
  }

  /**
   * 将查询结果转换为String集合
   */
  private <T> List<T> conversionJavaStringList()
  {
    List<String> strings = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        strings.add(v.getValue());
      }
    }
    return (List<T>) strings;
  }

  /**
   * 将查询结果转换为Integer集合
   */
  private <T> List<T> conversionJavaIntegerList()
  {
    List<Integer> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(Integer.valueOf(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为Long集合
   */
  private <T> List<T> conversionJavaLongList()
  {
    List<Long> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(Long.valueOf(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为Short集合
   */
  private <T> List<T> conversionJavaShortList()
  {
    List<Short> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(Short.valueOf(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为Boolean集合
   */
  private <T> List<T> conversionJavaBooleanList()
  {
    List<Boolean> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(Boolean.valueOf(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为Double集合
   */
  private <T> List<T> conversionJavaDoubleList()
  {
    List<Double> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(Double.valueOf(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为Float集合
   */
  private <T> List<T> conversionJavaFloatList()
  {
    List<Float> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(Float.valueOf(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为Float集合
   */
  private <T> List<T> conversionJavaByteList()
  {
    List<Byte> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(Byte.valueOf(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为BigDecimal集合
   */
  private <T> List<T> conversionJavaBigDecimalList()
  {
    List<BigDecimal> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(new BigDecimal(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为BigInteger集合
   */
  private <T> List<T> conversionJavaBigIntegerList()
  {
    List<BigInteger> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(new BigInteger(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  /**
   * 将查询结果转换为BigInteger集合
   */
  private <T> List<T> conversionJavaDateList() throws ParseException
  {
    List<Date> list = Lists.newArrayList();
    for (Map<String, String> resultMap : resultSet)
    {
      for (Map.Entry<String, String> v : resultMap.entrySet())
      {
        list.add(formatter.parse(v.getValue()));
      }
    }
    return (List<T>) list;
  }

  @Override
  public String toJSONString()
  {
    // 为了区分数组和单个对象
    if (resultSet.size() == 1)
    {
      return JSONObject.toJSONString(resultSet.get(0));
    }
    return JSONObject.toJSONString(resultSet);
  }

  @Override
  public void hasNext()
  {
    if (!resultSet.isEmpty())
    {
      this.hasNext = new ArrayList<>(resultSet.get(this.hasNextOffset).values());
      this.hasNextOffset++;
      this.nextOffset = 0;
    }
  }

  @Override
  public String next()
  {
    if (hasNext == null) return null;
    String v = hasNext.get(this.nextOffset);
    this.nextOffset++;
    return v;
  }

  @Override
  public void reset()
  {
    String next = null;
    this.hasNext = null;
    this.nextOffset = 0;
    this.hasNextOffset = 0;
  }

  /**
   * 添加对应的值
   *
   * @param field  成员对象
   * @param value  要转换的值
   * @param entity 实体类型
   */
  private void setValue(Field field, String value, Object entity) throws Exception
  {
    if (field.getType().equals(String.class))
    {
      field.set(entity, value);
      return;
    }
    if (field.getType().equals(Date.class))
    {
      field.set(entity, formatter.parse(value));
      return;
    }
    if (field.getType().equals(Byte.class))
    {
      field.set(entity, Byte.valueOf(value));
      return;
    }
    if (field.getType().equals(Long.class))
    {
      field.set(entity, Long.valueOf(value));
      return;
    }
    if (field.getType().equals(Short.class))
    {
      field.set(entity, Short.valueOf(value));
      return;
    }
    if (field.getType().equals(Float.class))
    {
      field.set(entity, Float.valueOf(value));
      return;
    }
    if (field.getType().equals(Double.class))
    {
      field.set(entity, Double.valueOf(value));
      return;
    }
    if (field.getType().equals(Boolean.class))
    {
      field.set(entity, Boolean.valueOf(value));
      return;
    }
    if (field.getType().equals(Integer.class))
    {
      field.set(entity, Integer.valueOf(value));
      return;
    }
    if (field.getType().equals(BigDecimal.class))
    {
      field.set(entity, new BigDecimal(value));
      return;
    }
    if (field.getType().equals(BigInteger.class))
    {
      field.set(entity, new BigInteger(value));
    }
  }

  /**
   * 判断是不是基本数据类型或其他数据类型
   */
  public Object isBase(Class<?> target, String value)
  {
    if (StringUtils.isEmpty(value)) return null;
    try
    {
      if (target.equals(String.class))
      {
        return value;
      } else if (target.equals(Date.class))
      {
        return formatter.parse(value);
      } else if (target.equals(Byte.class))
      {
        return Byte.valueOf(value);
      } else if (target.equals(Long.class))
      {
        return Long.valueOf(value);
      } else if (target.equals(Short.class))
      {
        return Short.valueOf(value);
      } else if (target.equals(Float.class))
      {
        return Float.valueOf(value);
      } else if (target.equals(Double.class))
      {
        return Double.valueOf(value);
      } else if (target.equals(Boolean.class))
      {
        return Boolean.valueOf(value);
      } else if (target.equals(Integer.class))
      {
        return Integer.valueOf(value);
      } else if (target.equals(BigDecimal.class))
      {
        return new BigDecimal(value);
      } else if (target.equals(BigInteger.class))
      {
        return new BigInteger(value);
      }
    } catch (Exception e)
    {
      return e;
    }
    return null;
  }

}
