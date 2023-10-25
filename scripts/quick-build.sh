#!/bin/bash
mvn_path=/Users/fang/Environment/apache-maven-3.8.8/bin
project_root_path=/Users/fang/Coding/Personal/qiniu-1024

server_ip="berry.akfang.cn"
server_path="/home/qiniu/runtime/"
username="root"

need_package="false"

if [ $need_package == "true" ]; then
  cd $project_root_path
  "$mvn_path/mvn" clean package -DskipTests
fi

# provider
file1=$project_root_path/berry-services/berry-misc-service/berry-misc-provider/target/berry-misc-provider-0.0.1-SNAPSHOT.jar
file2=$project_root_path/berry-services/berry-video-service/berry-video-provider/target/berry-video-provider-0.0.1-SNAPSHOT.jar
file4=$project_root_path/berry-services/berry-user-service/berry-user-provider/target/berry-user-provider-0.0.1-SNAPSHOT.jar
# consumer
file5=$project_root_path/berry-services/berry-video-service/berry-video-consumer/target/berry-misc-consumer-0.0.1-SNAPSHOT.jar
file5=$project_root_path/berry-services/berry-user-service/berry-user-consumer/target/berry-misc-consumer-0.0.1-SNAPSHOT.jar

# gateway
file3=$project_root_path/berry-gateway/target/berry-gateway-0.0.1-SNAPSHOT.jar

# 上传JAR文件到服务器
scp -C "${file1}" "${file2}" "${file3}" "${file4}" "${file5}" "${file6}" "${username}@${server_ip}:${server_path}"

# 连接到服务器并运行JAR文件
ssh "${username}@${server_ip}" "cd ${server_path} && for file in *.jar; do java -jar \$file; done"