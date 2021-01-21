@echo off
set RABBIT_ADDRESSES=localhost:5672
java -jar ./zipkin-server-2.21.7-exec.jar

#bash creado para levantar zipkin server con rabbit