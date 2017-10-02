#!/bin/sh
source /etc/profile
MAIN_CLASS=com.netto.demo.DemoServerLauncher
PIDPROC=`ps -ef | grep "${MAIN_CLASS}" | grep -v 'grep'| awk '{print $2}'`

if [ -z "$PIDPROC" ];then
 echo "$MAIN_CLASS is not running"
 exit 0
fi

echo "PIDPROC: "$PIDPROC
for PID in $PIDPROC
do
if kill -9 $PID
   then echo "process $MAIN_CLASS (Pid:$PID) was force stopped at " `date`
fi
done
echo stop finished.
