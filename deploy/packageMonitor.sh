#!/bin/bash -e

if [ -z "$1" ]
  then
    echo "No arguments supplied"
    exit 1
fi

set -e

USERNAME=artread
PASSWD="\{DESede\}YNtyA/TMlbuQjz/BlYj9Pw=="

VERSIONSTR=$(head -n 1 ../version.sbt)
SNAPSHOTBEGIN=`echo $VERSIONSTR | grep -b -o '-' | awk 'BEGIN {FS=":"}{print $1}' | bc`
SNAPSHOTBEGIN=$((SNAPSHOTBEGIN - 25))
STRSIZE=${#VERSIONSTR}

FINALCHARPOS=$((STRSIZE - 25 - 1))

if [[ (( "$SNAPSHOTBEGIN" -gt 0 )) ]]; then
  ## If version has -SNAPSHOT in it, then the
  ## release version should have its micro 1 less
  ## than the snapshot
  ## Example: version in version.sbt = 1.0.2-SNAPSHOT
  ##          release version is 1.0.1
  VERSION=${VERSIONSTR:25:$SNAPSHOTBEGIN}
  major=$(echo $VERSION | cut -d. -f1)
  minor=$(echo $VERSION | cut -d. -f2)
  micro=$(echo $VERSION | cut -d. -f3)
  releasemicro=$(echo "$micro - 1" | bc)
  RELEASEVERSION="$major.$minor.$releasemicro"
  SNAPSHOTVERSION="$major.$minor.$micro-SNAPSHOT"
else
  ## If not -SNAPSHOT in version
  ## Then the release version is the version.
  VERSION=${VERSIONSTR:25:$FINALCHARPOS}
  RELEASEVERSION=$VERSION
fi

RELEASEURL=https://arm.mo.ca.am.ericsson.se/artifactory/simple/proj-jnkserv-staging-local/com/ericsson/jenkinsci/hajp/hajp-monitor_2.11/$RELEASEVERSION/hajp-monitor_2.11-$RELEASEVERSION.zip
SNAPSHOTURL=https://arm.mo.ca.am.ericsson.se/artifactory/simple/proj-jnkserv-dev-local/com/ericsson/jenkinsci/hajp/hajp-monitor_2.11/$SNAPSHOTVERSION/hajp-monitor_2.11-$SNAPSHOTVERSION.zip

rm -rf hajp-monitor-deploy

# Download monitor

EXTRACTVERSION=$RELEASEVERSION
if [ $1 == "release" ]
  then
  wget --no-proxy -O hajp-monitor.zip --user=$USERNAME --password=$PASSWD $RELEASEURL
fi

if [ $1 == "snapshot" ]
  then
  wget --no-proxy -O hajp-monitor.zip --user=$USERNAME --password=$PASSWD $SNAPSHOTURL
  EXTRACTVERSION=$SNAPSHOTVERSION
fi


unzip -q hajp-monitor.zip -d hajp-monitor-deploy/
mv -f hajp-monitor-deploy/hajp-monitor-$EXTRACTVERSION/* hajp-monitor-deploy/
rm -rf hajp-monitor-deploy/hajp-monitor-$EXTRACTVERSION
rm -f *.zip
