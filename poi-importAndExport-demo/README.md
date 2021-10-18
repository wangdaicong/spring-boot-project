# poi注解加反射实现通用excel导入导出

https://github.com/xyz0101/excelutils

java中元注解有四个：@Target、@Retention、@Document、@Inherited；

- Target:注解的作用目标
 1. @Target(ElementType.TYPE)：接口、类、枚举  
 2. @Target(ElementType.FIELD)：字段、枚举的常量
 3. @Target(ElementType.METHOD)：方法
 4. @Target(ElementType.PARAMETER)：方法参数
 5. @Target(ElementType.CONSTRUCTOR)：构造函数
 6. @Target(ElementType.LOCAL_VARIABLE)：局部变量
 7. @Target(ElementType.ANNOTATION_TYPE)：注解
 8. @Target(ElementType.PACKAGE)：包   
---
- Retention：注解的保留位置　　　　　　　　　
 1. @Retention(RetentionPolicy.SOURCE)：注解仅存在于源码中，在class字节码文件中不包含
 2. @Retention(RetentionPolicy.CLASS)：默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得，
 3. @Retention(RetentionPolicy.RUNTIME)：注解会在class字节码文件中存在，在运行时可以通过反射获取到
---
- @Document：说明该注解将被包含在javadoc中
- @Inherited：说明子类可以继承父类中的该注解