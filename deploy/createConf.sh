#!/bin/bash

IP=`awk 'NR==1 {print $1}' /etc/hosts`

# Remove file
rm /code/conf/prod.conf

echo "include \"application.conf\"


HajpCluster {
  akka {
    actor {
      provider = \"akka.cluster.ClusterActorRefProvider\"
    }

    remote {
      log-remote-lifecycle-events = off
      netty.tcp {
        hostname = \"$IP\"
        port = 2551
              maximum-frame-size = 100000 kilobytes
      }
    }

    cluster {

      min-nr-of-members = 2

      role {
        jenkins.min-nr-of-members = 1
        orchestrator.min-nr-of-members = 1
      }

      seed-nodes = [
        \"akka.tcp://HajpCluster@$ORCHESTRATOR_PORT_2551_TCP_ADDR:$ORCHESTRATOR_PORT_2551_TCP_PORT\"]
      roles = [frontend]
      auto-down-unreachable-after = 10s
    }
  }
}
" > /code/conf/prod.conf

sh bin/hajp-monitor -Dconfig.file=conf/prod.conf

# Uncomment to check produced conf file
cat /code/conf/prod.conf
