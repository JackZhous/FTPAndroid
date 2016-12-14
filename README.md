# file transport in Android device with FTP protoal

the root directory: ftp_server.apk, you can install any android device, and the device will be a ftp server

## 使用FTP协议在Android设备上进行数据传输



根目录下面有一个ftp_server.apk，是ftp服务端，在任何手机上安装手机即可成功ftp服务器，其他设备运行我的测试代码可以进行连接数据传输，我的代码实现功能有：
+ 文件层级展示
+ 文件下载
+ 文件删除

项目运用底层ftp协议是ftp4j提供的实现，ftp4j工具包是一个专门适用于移动端的工具类，功能十分强大；
了解详情请参考：http://www.sauronsoftware.it/projects/ftp4j/
