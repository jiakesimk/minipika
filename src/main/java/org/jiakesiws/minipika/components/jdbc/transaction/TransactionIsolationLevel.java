package org.jiakesiws.minipika.components.jdbc.transaction;

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
 * Creates on 2020/6/6.
 */

import java.sql.Connection;

/**
 * 事务隔离级别
 *
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public enum TransactionIsolationLevel
{

  /**
   * Jdbc驱动不支持事务
   */
  TRANSACTION_NONE(Connection.TRANSACTION_NONE),

  /**
   * 允许脏读、不可重复读和幻读
   */
  TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

  /**
   * 禁止脏读，但允许不可重复读和幻读
   */
  TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

  /**
   * 禁止脏读和不可重复读，单运行幻读
   */
  TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

  /**
   * 禁止脏读、不可重复读和幻读
   */
  TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),
  ;

  private final int value;

  TransactionIsolationLevel(int value)
  {
    this.value = value;
  }

  public int getLevel()
  {
    return this.value;
  }

}
