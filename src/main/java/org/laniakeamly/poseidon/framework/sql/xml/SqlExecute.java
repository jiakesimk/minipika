package org.laniakeamly.poseidon.framework.sql.xml;

import org.laniakeamly.poseidon.framework.db.JdbcSupport;

import java.util.List;

/**
 * sql执行器
 * Copyright: Create by TianSheng on 2019/12/26 17:48
 */
@SuppressWarnings({"unchecked"})
public class SqlExecute {

    private String sql;
    private Object[] args;
    private Class<?> result;
    private JdbcSupport jdbc;

    public SqlExecute(){

    }

    public SqlExecute(String sql, Object[] args, Class<?> result,JdbcSupport jdbc) {
        this.sql = sql;
        this.jdbc = jdbc;
        this.args = args;
        this.result = result;
    }

    public <T> List<T> queryForList(){
        return (List<T>) jdbc.queryForList(sql,result,args);
    }

    public <T> T queryForObject(){
        return (T) jdbc.queryForObject(sql,result,args);
    }

    public int update(){
        return jdbc.update(sql,args);
    }

    public int insert(){
        return jdbc.insert(sql,args);
    }

    public int[] executeBatch(){
        return jdbc.executeBatch(sql,args);
    }

    public boolean execute() {
        return jdbc.execute(sql,args);
    }
}
