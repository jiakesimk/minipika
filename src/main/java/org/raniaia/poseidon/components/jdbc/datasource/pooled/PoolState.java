package org.raniaia.poseidon.components.jdbc.datasource.pooled;

/*
 * Copyright (C) 2020 tiansheng All rights reserved.
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
 */

/*
 * Creates on 2020/3/25.
 */

import org.raniaia.available.list.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tiansheng
 */
public class PoolState {

    private PooledDataSource dataSource;

    final List<PooledConnection> idleConnection = Lists.newArrayList();
    final List<PooledConnection> activeConnections = Lists.newArrayList();

    public PoolState() {
    }

    public PoolState(PooledDataSource dataSource){
        this.dataSource = dataSource;
    }

}