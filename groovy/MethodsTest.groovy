package framework.util

import MqlMapper;

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
 * Creates on 2020/6/23.
 */

import javassist.NotFoundException
import org.jiakesiws.minipika.framework.util.Arrays
import org.jiakesiws.minipika.framework.util.Methods
import org.junit.Test

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
class MethodsTest {

  @Test
  void test() throws NotFoundException {
    Arrays.toString(Methods.getParameterNames(MqlMapper.methods[0]));
  }

  @Test
  void test2() {

  }

}
