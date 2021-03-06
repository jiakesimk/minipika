# 使用文档

> API手册

# 配置文件搭建

MinipikaFramework会自动扫描**resources**目录下的**minipika.properties**,如果没有这个文件那么会报一个**ReadException**

如果配置文件的文件名是自定义的，那么需要调用一个手动加载配置的方法**ManualConfig.load()**，示例如下:

```java
// 假设配置文件的名字叫做"newminipika.properties"
ManualConfig.load("newminipika.properties");
```

这个需要在最开始就调用，比如springboot的启动类在启动前调用，或者tomcat的init等，需要在使用前调用。

**配置项**

```properties
#####################################
### 数据库属性
#####################################
minipika.jdbc.url = jdbc:mysql://127.0.0.1:3306/groovy?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT
minipika.jdbc.driver = com.mysql.cj.jdbc.Driver
minipika.jdbc.username = root
minipika.jdbc.password = root

#####################################
### 是否开启事物，默认为false
#####################################
minipika.jdbc.transaction = true

#####################################
### 是否开启缓存，默认为false
#####################################
minipika.jdbc.cache = false

#####################################
### 连接池大小
### 默认最小连接为2个连接
### 默认最大连接为6个连接
#####################################
minipika.connectionPool.minSize = 2
minipika.connectionPool.maxSize = 90

#####################################
### 表名前缀，可为空
#####################################
minipika.entity.prefix = kkb

# 模型所在的包
minipika.entity.package = com.minipika.entity.experiment

```

----

# 模型映射

> Minipika提供了Table和Entity之间的映射，创建表和新增字段的时候只需要在Entity中新增即可。由TractoFramework来创建表，以及更新字段，不需要开发人员建立完表之后又去建立Entity（索引需要手动建立）。

**提供的注解**

- @Entity

    **@Entity**注解的范围在**TYPE**内，将注解放在类上即代表这是一个模型类。Entity注解有两个参数分别为：**value**和**engine**，engine默认值为**InnoDB**

- @Ignore

    **@Ignore**注解范围在**FIELD**内，将注解放在在字段上代表该字段不和数据库进行交互动作。

- @Column

    **@Column**注解范围在**FIELD**内，代表字段的一些属性。参数为 **String value();** 假设我当前有个**private String name**字段，然后注解上传入参数 **@Column("varchar(255) not null")**。其实这就相当于省下了字段名。

- @Increase

    **@Increase**注解范围在**FIELD**内，代表被注解的字段会进行自增。

- @Comment

    **@Comment**字段注释

- @Pk

    **@Pk**主键

具体Entity的实现可以参考一下本项目下的[UserEntity](https://github.com/PageNotFoundx/minipika/blob/master/src/main/java/com/minipika/entity/experiment/UserEntity.java)。

当Entity配置好了之后在启动时会自动创建表和字段。

----

# Jdbc操作

### **获取JdbcSupport**

如果你是**service**类的话推荐使用方式是继承**JdbcSupport**，示例：

```java
public class UserServiceImpl 
extends JdbcSupport implements UserService{
    // 这里调用JdbcSupport中提供的API
}
```

如果是测试类的话我推荐使用 **getTemplate()** 的方式来进行Jdbc操作，示例：

```java
public class Main{

    // 获取JdbcSupport对象
    static JdbcSupport jdbc = JdbcSupport.getTemplate();

    public static void main(String[] args){
      // 省略操作...
    }
    
}
```

----

### **查询单个对象并返回**

```java
/**
 * 查询并返回对象
 * @param sql sql语句
 * @param obj 需要返回的对象class
 * @param args 参数
 * @param <T>
 * @return
 */
<T> T queryForObject(String sql, Class<T> obj, Object... args);
```

**示例：**

*假设我们当前有一张user_entity表，我们要查询单条数据*

```java
public class Main{

    // 获取JdbcSupport对象
    static JdbcSupport jdbc = JdbcSupport.getTemplate();

    public static void main(String[] args){
        String sql = "queryOf * from user_entity where user_name = ?";
        UserEntity user = jdbc.queryForObject(sql,UserEntity.class,"张三");
        // 这里的话queryForList也是一样的，没什么太大的区别。
        List<UserEntity> user = jdbc.queryForList(sql,UserEntity.class,"张三");
    }
    
}
```

----

### **分页查询**

```java
/**
 * 分页查询,SQL不用加limit
 * @param sql
 * @param args
 * @return
 */
NativePageHelper queryForPage(String sql, NativePageHelper pageVo, Object... args);
```

分页查询需要传入一个**NativePageHelper**对象的分页插件，**NativePageHelper**是一个抽象类，我们不会使用它。我提供了一个叫做**PageHelper**的对象，它继承自**NativePageHelper**。所以我们分页都用它来传参。

**示例代码：**

*假设我们要查询**user_entity**这张表的**user_name**字段，查询10到20条之间的数据。*

```java
//
// 第一步需要new一个PageHelper插件
//
PageHelper pageVo = new PageHelper(UserEntity.class); // 构造函数需要返回结果的类型
// 设置页码和页面大小，这两个可以通过构造函数传入。
pageVo.setPageNum(2);     
pageVo.setPageSize(10);

//
// 第二步编写SQL
//
String sql = "queryOf `user_name` from user_entity";

//
// 第三步执行查询
//
jdbc.queryForPage(sql,pageVo);

//
// 获取查询到的数据
// pageVo查询完后还包含总页数、总记录数，当前第几页、以及每页显示多少条数据等这些基本信息
//
System.out.println(JSON.toJSONString(pageVo.getData()));
```

值得注意的是我们并不需要在sql上添加limit 2,10。

NativePageHelper是为了扩展而将它写成了抽象类，大家可以去继承它然后实现自己的东西。

---

### **更新和新增**

**update**

update其实非常简单，提供了一下三种方法：
```java

    /**
     * 更新所有实体类中的所有数据，但不包括为空的数据。
     * @param obj 实体类
     * @return 更新条数
     */
    int update(Object obj);

    /**
     * 通过SQL语句来更新数据。
     * @param sql sql语句
     * @param args 参数列表
     * @return 更新条数
     */
    int update(String sql, Object... args);

    /**
     * 传入一个实体类，将实体类中为空的数据也进行更新。
     * @param obj 实体类
     * @return 更新条数
     */
    int updateDoNULL(Object obj);

```

第一个是更新传入实体对象更新，但是不会更新实体中为空的数据。

第二个是通过sql来更新。

第三个也是更新传入的实体对象，但是它会更新为null的数据。

除了第二个，其他两个都是根据主键进行更新的。

**insert**

insert大致也一样，我这就不细讲了。

---

# 结尾

    至于其他的大家看看JdbcSupportService接口就明白了。
