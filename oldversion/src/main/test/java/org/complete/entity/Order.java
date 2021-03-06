package org.complete.entity;

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
 * Creates on 2020/3/23 23:16
 */

import org.jiakesiws.minipika.framework.provide.entity.Column;
import org.jiakesiws.minipika.framework.provide.entity.Comment;
import org.jiakesiws.minipika.framework.provide.entity.Entity;
import org.jiakesiws.minipika.framework.provide.entity.PK;

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
@Entity("order")
public class Order {

    @PK
    @Column("int(11) not null")
    private Integer id;

    @Column("varchar(255) not null")
    @Comment("订单编号")
    private Integer orderNumber;

}
