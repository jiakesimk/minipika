package org.jiakesiws.minipika.components.jdbc.datasource.pooled;

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

import org.jiakesiws.minipika.components.configuration.node.SourceConfig;
import org.jiakesiws.minipika.components.jdbc.datasource.DataSourceManager;
import org.jiakesiws.minipika.components.jdbc.datasource.unpooled.UnpooledDataSource;
import org.jiakesiws.minipika.components.logging.Log;
import org.jiakesiws.minipika.components.logging.LogFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public class PooledDataSource implements DataSource
{

  protected String username;

  protected String password;

  protected final PooledState state;

  protected SourceConfig sourceConfig;

  protected UnpooledDataSource unpooledDataSource;

  private static final Log log = LogFactory.getLog(PooledConnection.class);

  public PooledDataSource(UnpooledDataSource unpooledDataSource)
  {
    {
      this.unpooledDataSource = unpooledDataSource;
      this.sourceConfig = unpooledDataSource.getSourceConfig();
      this.username = sourceConfig.getUsername();
      this.password = sourceConfig.getPassword();
      this.state = new PooledState(this);
    }
    {
      // 将自己注册到数据源管理器中
      DataSourceManager.registerDataSource(sourceConfig.getName(), this);
    }
    {
      try
      {
        for (int i = 0; i < state.minimumConnections; i++)
        {
          state.idleConnections.add(createConnection(username, password));
        }
      } catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }

  public PooledState getState()
  {
    return state;
  }

  public UnpooledDataSource getUnpooledDataSource()
  {
    return unpooledDataSource;
  }

  /**
   * 归还链接
   *
   * @param connection 归还的链接对象
   */
  public void pushConnection(PooledConnection connection) throws SQLException
  {
    synchronized (state)
    {
      state.activeConnections.remove(connection);
      if (connection.isValid())
      {
        // 判断总连接数是否大于或等于最大连接数
        if (state.currentConnectionsCount < state.maximumConnections)
        {
          state.idleConnections.add(connection);
          if (log.isDebugEnabled())
          {
            log.debug("Returned connection " + connection.getRealHasCode() + " to  pool.");
          }
          state.notifyAllStrategy();
        } else
        {
          connection.invalidate();
          state.badConnectionCount++;
        }
      } else
      {
        if (log.isDebugEnabled())
        {
          log.debug("A bad connection (" + connection.getRealHasCode() + ") close.");
        }
        connection.forceClose();
      }
    }
  }

  protected PooledConnection popConnection() throws SQLException
  {
    return popConnection(username, password);
  }

  /**
   * 弹出链接
   */
  protected PooledConnection popConnection(String username, String password) throws SQLException
  {
    PooledConnection connection = null;
    long startTime = System.currentTimeMillis();
    synchronized (state)
    {
      while (connection == null)
      {
        if (!state.idleConnections.isEmpty())
        {
          connection = state.idleConnections.remove(0);
          if (log.isDebugEnabled())
          {
            log.debug("pop out connection " + connection.getRealHasCode() + " from pool,");
          }
        } else
        {
          if (state.currentConnectionsCount < state.maximumConnections)
          {
            connection = createConnection(username, password);
            state.accumulateCreatesCount++;
          } else
          {
            try
            {
              long wt = System.currentTimeMillis();
              if (log.isDebugEnabled())
              {
                log.debug(Thread.currentThread().getName() + " thread is waiting for getting a connection.");
              }
              state.waitStrategy(state.maximumWaitTimeout);
              long wte = (System.currentTimeMillis() - wt);
              state.maximumWaitTimeoutCount += wte;
              if (log.isDebugEnabled())
              {
                log.debug(Thread.currentThread().getName() + " thread is woken up, waiting time: " + wte);
              }
            } catch (InterruptedException e)
            {
              break;
            }
          }
        }
        if (connection != null)
        {
          if (connection.isValid())
          {
            connection.setLastUsedTimestamp(System.currentTimeMillis());
            state.requestCount++;
            state.activeConnections.add(connection);
            state.requestAccumulateTime += (System.currentTimeMillis() - startTime);
          } else
          {
            if (log.isDebugEnabled())
            {
              log.debug("A bad connection (" + connection.getRealHasCode() + ") close. return to another connection.");
            }
            state.badConnectionCount++;
            connection.forceClose();
            connection = null;
          }
        }
      }
    }
    return connection;
  }

  protected synchronized PooledConnection createConnection(
          String username, String password) throws SQLException
  {
    return new PooledConnection(unpooledDataSource.getConnection(username, password), this);
  }

  @Override
  public Connection getConnection() throws SQLException
  {
    return popConnection().proxyConnection;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException
  {
    return unpooledDataSource.getConnection(username, password);
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException
  {
    return unpooledDataSource.getLogWriter();
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException
  {
    unpooledDataSource.setLogWriter(out);
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException
  {
    unpooledDataSource.setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException
  {
    return unpooledDataSource.getLoginTimeout();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return unpooledDataSource.unwrap(iface);
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException
  {
    return unpooledDataSource.isWrapperFor(iface);
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException
  {
    return unpooledDataSource.getParentLogger();
  }

}
