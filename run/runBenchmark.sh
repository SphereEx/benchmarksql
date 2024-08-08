#!/usr/bin/env bash

if [ $# -ne 1 ] ; then
    echo "usage: $(basename $0) PROPS_FILE" >&2
    exit 2
fi

SEQ_FILE="./.jTPCC_run_seq.dat"
if [ ! -f "${SEQ_FILE}" ] ; then
    echo "0" > "${SEQ_FILE}"
fi
SEQ=$(expr $(cat "${SEQ_FILE}") + 1) || exit 1
echo "${SEQ}" > "${SEQ_FILE}"

source funcs.sh $1

setCP || exit 1

# myOPTS="-Dprop=$1 -DrunID=${SEQ} -Xmx512m -Xms512m"
VERSION_OPTS=" -XX:+SegmentedCodeCache -XX:+AggressiveHeap -XX:ParallelGCThreads=16 "
JAVA_OPTS=" -Djava.awt.headless=true "
JAVA_MEM_OPTS=" -XX:AutoBoxCacheMax=4096 -XX:+UseNUMA -XX:+DisableExplicitGC ${VERSION_OPTS} -Dio.netty.leakDetection.level=DISABLED -Dio.netty.buffer.checkAccessible=false -Dio.netty.buffer.checkBounds=false "
myOPTS="-Dprop=$1 -DrunID=${SEQ} -server -Xmx32g -Xms32g -Xmn20g -Xss2m ${JAVA_OPTS} ${JAVA_MEM_OPTS} "

#numactl -C 0-28,32-60,64-92,96-124 java -cp "$myCP" $myOPTS jTPCC
numactl -C 16-127 java -cp "$myCP" $myOPTS jTPCC
