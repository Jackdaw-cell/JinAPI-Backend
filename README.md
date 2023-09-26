# JinOpenAPI开放平台

[toc]

## 环境

### 主流框架 & 特性

- Spring Boot 2.7.x

- MyBatis + MyBatis Plus 数据访问

- nacos 2.1.x

  

### 数据存储

- MySQL 数据库
- Redis 内存数据库

### 工具类

- Easy Excel 表格处理
- Hutool 工具库
- Gson 解析库
- Apache Commons Lang3 工具类
- Lombok 注解

### 业务特性

- Spring Session Redis 分布式登录
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决
- 多环境配置

### 单元测试

- JUnit5 单元测试
- 示例单元测试类


## 启动项目

### MySQL 数据库

1）修改 `application.yml` 的数据库配置为你自己的：

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/my_db
    username: 
    password: 
```

2）执行 `sql/create_table.sql` 中的数据库语句，自动创建库表

3）启动项目，

Redis 分布式登录

1）修改 `application.yml` 的 Redis 配置为你自己的：

```yml
spring:
  redis:
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
    password: 123456
```

2）修改 `application.yml` 中的 session 存储方式：

```yml
spring:
  session:
    store-type: redis
```

3）移除 `MainApplication` 类开头 `@SpringBootApplication` 注解内的 exclude 参数：

修改前：

```java
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
```

修改后：


```java
@SpringBootApplication
```

### Dubbo 远程调用

先引入依赖

```
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>3.0.9</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>2.1.0</version>
        </dependency>
```

1）修改服务提供方配置文件，注册中心地址改为自己的nacos启动地址

```java
dubbo:
  application:
    name: dubbo-springboot-demo-provider
  protocol:
    name: dubbo
    port: -1
  registry:
    id: nacos-registry
    address: nacos://127.0.0.1:8848
```

2）启动类添加注解@EnableDubbo开启Dubbo

```java
@EnableDubbo
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
```

3）编写接口DemoService

```java
public interface DemoService {

    String sayHello(String name);

    String sayHello2(String name);

    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }

}

```

4）编写DemoServiceImpl实现类，加上@DubboService注解

```java
@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name;
    }

    @Override
    public String sayHello2(String name) {
        return "yupi";
    }

}
```

5）消费方修改配置文件

```yml
dubbo:
  application:
    name: dubbo-springboot-demo-provider
  protocol:
    name: dubbo
    port: -1
  registry:
    id: nacos-registry
    address: nacos://127.0.0.1:8848
```

6）消费方启动类配置，开启Dubbo

```java
@EnableDubbo
public class JackdawApiGatewayApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JackdawApiGatewayApplication.class, args);
    }

}
```

7）消费方调用Dubbo提供方的方法

```java
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private DemoService DemoServiceImpl；
    
    public void  validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        DemoServiceImpl.sayHello()
    }
 }
```

### JinOpenAPI开放平台 SDK调用

1）引入依赖

```xml
        <!- 转发网关接口类->
		<dependency>
            <groupId>io.github.jackdaw-cell</groupId>
            <artifactId>jackdawapi-sdk</artifactId>
            <version>0.0.6</version>
        </dependency>
        <!- 公共类 ->
        <dependency>
            <groupId>io.github.jackdaw-cell</groupId>
            <artifactId>jackdawapi-common</artifactId>
            <version>0.0.1</version>
        </dependency>
```

2）修改配置文件

```yml
jackdaw:
  openai:
    api-key: #私人的OpenKey
    api-host: https://api.openai.com/
    proxy-host-name: #代理服务器地址
    proxy-port: #代理服务器地址端口
  #平台创建用户获取
  api:
    access-key: 你的ak
    secret-key: 你的sk

```

3）可以使用

```java
@Component
public class Test {
    @Resource
    private JackdawApiClient jackdawApiClient;
    
   public BaseResponse<Object> testSdk() {
        String userRequestParams="{\"number\":\"吾即是汝，汝即是吾\"}";
        String bodyJson = JSONUtil.toJsonStr(userRequestParams);
        String url = "/user/number";
        return jackdawApiClient.getRequest(bodyJson, url);
    }
}
```

### Sentinel 监控

1）安装并开启sentinel

```
java -Dserver.port=8070 -Dcsp.sentinel.dashboard.server=localhost:8070 -jar -Dnacos.addr=localhost:8848 sentinel-dashboard-1.8.1.jar
```

2）引入依赖

```
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel
            </artifactId>
        </dependency>
```

3）修改配置文件

```
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8070   #控制台地址
```

4）访问控制台http://localhost:8070即可访问Sentinel控制台
