package org.jiakesiws.minipika.timer;

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



import org.jiakesiws.minipika.framework.timer.Timer;
import org.jiakesiws.minipika.framework.tools.DateUtils;

/**
 * Copyright: Create by 2B键盘 on 2019/12/9 11:18
 */
public class Timer1 implements Timer {

    @Override
    public void run() {

        System.out.println("timer1");

    }

    @Override
    public long time() {
        return DateUtils.SECOND * 5;
    }
}
