# 改造ScheduledTaskRegistrar实现动态增删启停定时任务功能
在spring boot项目中定时任务的开发方式：

一、可通过@EnableScheduling注解和@Scheduled注解实现

二、可通过SchedulingConfigurer接口来实现

三、集成Quartz框架实现

`注意：第一和第二方式不能动态添加、删除、启动、停止任务。`在满足项目需求的情况下，尽量少的依赖其它框架，避免项目过于臃肿和复杂是最基本的开发原则。

查看 spring-context 这个 jar 包中 org.springframework.scheduling.ScheduledTaskRegistrar 这个类的源代码，发现可以通过改造这个类（主要是基于TaskScheduler和CronTask两个类来实现）就能实现动态增删启停定时任务功能。