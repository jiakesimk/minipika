package org.jiakesiws.minipika.components.wrapper;

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
 * Creates on 2019/11/13.
 */

/**
 * @author lts
 */
public interface IdenPool {

  String EMPTY          = "";
  String AND            = "and";
  String FALSE          = "false";
  String TRUE           = "true";
  String GE             = ">=";
  String GT             = ">";
  String LE             = "<=";
  String LT             = "<";
  String EQUALS         = "=";
  String NOT_EQUALS     = "!=";
  String LIKE           = "like";
  String R_LIKE         = LIKE.concat(" '{}%'");
  String L_LIKE         = LIKE.concat(" '%{}'");
  String PLUS           = "+";
  String SUB            = "-";
  String MUL            = "*";
  String DIV            = "/";


  String OR             = "or";
}
