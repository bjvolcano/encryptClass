# encryptClass
可以加密核心代码，防止反编译。在运行时通过自定义类加载器加载，如果有集成spring，会自动注册到spring容器。
项目分为两大块，一个是maven插件（用于加密class、根据需要是否复制到目标目录）、另外是类加载器。

模块说明：
>encrypt-plugin 插件实现。
> 
>encrypted      需要加密的class模块，主要看pom文件。
> 
>test           测试模块。需要依赖loader组建。 需要注意，在启动的时候，不要扫描（被加密所在的包,如果是spring项目，程序会自动注入）。