# 指定环境
profile=prod
# jar
SERVER_JAR="qy.jar"
# heap size
HeapSize=1024m
# permSize
PermSize=256m
# maxPermSize
MaxPermSize=512m
# ReservedCodeCacheSize
ReservedCodeCacheSize=240M
# MaxDirectMemorySize
MaxDirectMemorySize=256m

psid=0
 
checkpid() {
   if [ ! -f "server.pid" ]; then  
      psid=0
   else
      javaps=`cat server.pid`
      psid=`echo $javaps`
   fi
}

start(){
  echo "start project..."
  checkpid

  if [ $psid -ne 0 ]; then
      echo "===================================================="
      echo "warn: $SERVER_JAR already started! (pid=$psid)"
      echo "===================================================="
  else

    nohup java -verbose:gc -Xmx$HeapSize -Xms$HeapSize -XX:PermSize=$PermSize -XX:MaxPermSize=$MaxPermSize -XX:ReservedCodeCacheSize=$ReservedCodeCacheSize -XX:MaxDirectMemorySize=$MaxDirectMemorySize -XX:+TieredCompilation -XX:+OptimizeStringConcat -XX:AutoBoxCacheMax=20000 -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:MaxTenuringThreshold=6 -XX:+ExplicitGCInvokesConcurrent -XX:+ParallelRefProcEnabled -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:`pwd`/logs/jvm-gc.log -XX:-HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=`pwd`/logs/heap_dump.hprof -XX:+PrintGCApplicationStoppedTime -XX:-OmitStackTraceInFastThrow -XX:ErrorFile=`pwd`/logs/jvm_error.log -XX:-PrintConcurrentLocks -XX:+PrintCommandLineFlags -jar -Dspring.profiles.active=$profile $SERVER_JAR > stdout.log 2>&1 &
    echo $! > server.pid 
    checkpid

    echo "===================================================="
    if [ $psid -ne 0 ]; then
         echo "start $SERVER_JAR (pid=$psid) [OK]"
    else
         echo "start $SERVER_JAR [Failed]"
    fi
    echo "===================================================="
  fi
}

stop(){
    echo "stop project..."
    checkpid
    if [ $psid -ne 0 ]; then
        echo "kill pid $psid"
        kill -9 $psid
        rm -rf server.pid
        echo "===================================================="
        echo "stop $SERVER_JAR [OK]"
        echo "===================================================="
    else
        echo "===================================================="
        echo "warn: $SERVER_JAR not started!"
        echo "===================================================="
    fi
}


restart(){
    stop   
    sleep 1   
    start
}

case "$1" in
   'start')
     start
     ;;
   'stop')
     stop
     ;;
   'restart')
     stop
     start
     ;;
  *)
echo "Usage: $0 {start|stop|restart}"
esac
exit 1