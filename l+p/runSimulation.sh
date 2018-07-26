#!/usr/bin/env bash

if [[ ! -f "$1" || -z "$1" || -z "$2" || -z "$3"  ]]; then
    echo "Usage: $0 <jar-file> <uri> <simulationClass>"
    if [ -f "$1" ] ; then
        echo "  Available simulationClasses:"
        unzip -l "$1" | grep Simulation.class$ | sed -e "s/.* /    /;s/.class$//" \
            | grep -v "io/gatling/core/scenario/Simulation" | tr "/" "."
    else
        echo "<jar-file> $1 doesn't exist"
    fi
    printf '\nExample: '$0' target/lp*jar http://localhost:15001/cgi-bin com.net128.test.gatling.wechatin.MessageOutboundSimulation\n'
    exit 1
fi

USER_ARGS="-DCONFIG_FILE=$1"
JAVA_OPTS="-server -XX:+UseThreadPriorities -XX:ThreadPriorityPolicy=42 -Xms512M
    -Xmx2048M -XX:+HeapDumpOnOutOfMemoryError -XX:+AggressiveOpts
    -XX:+OptimizeStringConcat -XX:+UseFastAccessorMethods
    -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled
    -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false ${JAVA_OPTS}"

java $JAVA_OPTS $USER_ARGS \
    -Dusers=1000 -DresultsFolder=target/results -Duri="$2" -DsimulationClass="$3" -jar "$1" -nr
