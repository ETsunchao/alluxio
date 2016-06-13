---
layout: global
title: 在JSS上配置Alluxio
nickname: Alluxio使用JSS
group: Under Store
priority: 4
---

该指南介绍如何配置Alluxio以使用[Jingdong JSS](http://www.jcloud.com/products/cloudstorage.html)作为底层文件系统。
京东云存储（JSS）是京东云提供的一个大容量、安全、高可靠性的云存储服务。

## 初始步骤

要在许多机器上运行Alluxio集群，需要在这些机器上部署二进制包。你可以自己[编译Alluxio](Building-Alluxio-Master-Branch.html)，或者[下载二进制包](Running-Alluxio-Locally.html)

然后，如果你还没有配置文件，可以由template文件创建配置文件：

{% include Common-Commands/copy-alluxio-env.md %}

另外，为了在JSS上使用Alluxio，需要创建一个bucket（或者使用一个已有的bucket）。还要注意在该bucket里使用的目录，可以在该bucket中新建一个目录，或者使用一个存在的目录。
在该指南中，JSS bucket的名称为JSS_BUCKET，在该bucket里的目录名称为JSS_DIRECTORY。
另外，要使用JSS服务，还需提供一个jss 端点，该端点指定了你的bucket在哪个范围，本向导中的端点名为JSS_ENDPOINT。
要了解更多指定范围的端点的具体内容，可以参考[这里](http://jcloud.com/help/store_qa.html#qa2)。
要了解更多JSS Bucket的信息，请参考[这里](http://jcloud.com/help/store_sum.html#jbgn)。

## 配置Alluxio

若要在Alluxio中使用JSS作为底层文件系统，一定要修改`conf/alluxio-env.sh`配置文件。首先要指定一个已有的JSS bucket和其中的目录作为底层文件系统，可以在`conf/alluxio-env.sh`中添加如下语句指定它：

{% include Configuring-Alluxio-with-JSS/underfs-address.md %}

接着，需要指定jcloud证书以便访问JSS，在`conf/alluxio-env.sh`中的`ALLUXIO_JAVA_OPTS`部分添加：

{% include Configuring-Alluxio-with-JSS/jss-access.md %}

其中，`<JSS_ACCESS_KEY>`和`<JSS_SECRET_KEY>`是你实际的[Jcloud keys](http://www.jcloud.com/help/store_qa.html#qa1)，或者其他包含证书的环境变量，你可以从[这里](http://www.jcloud.com/help/store_qa.html#qa2)获取你的`<JSS_ENDPOINT>`。

如果你不太确定如何更改`conf/alluxio-env.sh`，有另外一个方法提供这些配置。可以在`conf/`目录下创建一个`alluxio-site.properties`文件，并在其中添加：

{% include Configuring-Alluxio-with-JSS/properties.md %}

更改完成后，Alluxio应该能够将JSS作为底层文件系统运行，你可以尝试[使用JSS在本地运行Alluxio](#running-alluxio-locally-with-s3)

## 配置分布式应用

如果你使用的Alluxio Client并非运行在Alluxio Master或者Worker上（在其他JVM上），那需要确保为该JVM提供了Jcloud证书，最简单的方法是在启动client JVM时添加如下选项：

{% include Configuring-Alluxio-with-JSS/java-bash.md %}

## 使用jSS在本地运行Alluxio

配置完成后，你可以在本地启动Alluxio，观察一切是否正常运行：

{% include Common-Commands/start-alluxio.md %}

该命令应当会启动一个Alluxio master和一个Alluxio worker，可以在浏览器中访问[http://localhost:19999](http://localhost:19999)查看master Web UI。

接着，你可以运行一个简单的示例程序：

{% include Common-Commands/runTests.md %}

运行成功后，访问你的JSS目录JSS_BUCKET/JSS_DIRECTORY，确认其中包含了由Alluxio创建的文件和目录。在该测试中，创建的文件名称应像下面这样：

{% include Configuring-Alluxio-with-JSS/jss-file.md %}

运行以下命令停止Alluxio：

{% include Common-Commands/stop-alluxio.md %}
