#!/bin/bash

#Change OS time zone
mv -f /etc/localtime /etc/localtime.bak
ln -s /usr/share/zoneinfo/Africa/Lagos /etc/localtime

#download keystore from repo
set -e
cd /opt/ticketapp/config/


cd /opt/ticketapp/

java -jar -Dspring.profiles.active=docker ticketapp-0.0.1.jar
