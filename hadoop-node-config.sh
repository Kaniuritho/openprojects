#!/bin/bash
#################################################################################
#################################################################################
#################################################################################
######   Hadoop Node Configuration File 			#################
######	 Author: Kaniu Ndungu					#################
######   Licencing/Agreement: Feel free to modify and exploit   #################
######   at your leisure but at your own risk.                  #################
#################################################################################
#################################################################################
 
#make sure only root can run
if [ $EUID -ne 0 ] 
then
	echo "This script must be run as root" 1>$2
	exit 1
fi

##look for arguments
if [ "$1" ]
then
	echo "prepping hostname $1 "
else
	echo "an argument stating the new hostname is required....exiting"
	exit 1
fi

if [ "$2" ]
then
        echo "prepping dnsname $2 "
else
        echo "an argument stating the configured DNS name is required....exiting"
        exit 1
fi

#check for java
#exit if none
javalink="/usr/lib/jvm/java-6-openjdk"
javadir="/usr/lib/jvm/java-6-openjdk-i386/"
if [ -d "$javadir" ]
then
echo "$javadir found... continuing"
	if [ -f "$javalink" ]
	then 
		echo "$javalink found"
	else
		echo "creating link"
		ln -s $javadir $javalink
	fi	
else
	echo "Java 6 not installed. Exiting"
	exit 1
fi

##modify hostname and hosts file
hostname=`hostname`
sed -i "s/$hostname/$2/g" /etc/hosts
echo $1 > /etc/hostname

if grep -Fxq "192.168.0.10 master" /etc/hosts > /dev/null
then
	echo "continue master found"
else 
	echo "192.168.0.10 master" >> /etc/hosts
fi

service hostname restart
service networking restart

##update ntpdate
echo '#!/bin/sh' | cat > /etc/cron.daily/ntpdate
echo 'ntpdate master' | cat >> /etc/cron.daily/ntpdate
echo '#!/bin/sh' | cat > /etc/ntp.conf
echo 'server master' | cat >> /etc/ntp.conf 

echo 'America/New_York' | cat > /etc/timezone
dpkg-reconfigure --frontend noninteractive tzdata

ntpdate -du master


##modify hadoop/hbase dirs - remove logs
hadoop_tar="/hadoop-hbase.tgz"

if [ -f "$hadoop_tar" ]
then
cd /
tar -xf $hadoop_tar
cd /usr/local/
ln -s hadoop* hadoop
ln -s hbase* hbase
cd /

chown -f -R hduser:hadoop /usr/local/hadoop*
chmod -f 0740 /usr/local/hadoop*
rm -r /usr/local/hadoop/logs/*

chown -f -R hduser:hadoop /usr/local/hbase*
chmod -f -R 0740 /usr/local/hbase*
rm -r /usr/local/hbase/logs/*
echo "Completed $hadoop_tar unpacking and configuration."
else
echo "ERROR!  $hadoop_tar not found"
fi

if grep -Fxq "$2" /usr/local/hadoop/conf/slaves > /dev/null
then
        echo "continue $2 configured properly in hadoop slaves file"
else
        echo "$2" >> /usr/local/hadoop/conf/slaves
fi


mkdir /app
chown -f -R hduser:hadoop /app
chmod -f 0740 /app
#alternate prepping JBOF 
#appdatadirs="/mnt/disk1/app/ /mnt/disk2/app/ /mnt/disk3/app/"
#mkdir $appdatadirs
#chown -f -R hduser:hadoop $appdatadirs
#chmod -f 0740 $appdatadirs

mkdir -p /Users/hduser/pichapps
chown -f -R hduser:hadoop /Users/hduser
chmod -f -R 0740 /Users/hduser

fusermount -u /Users/hduser/pichapps/
su hduser -c "sshfs hduser@master:/Users/hduser/pichapps/ /Users/hduser/pichapps/"
