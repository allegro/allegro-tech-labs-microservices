#!/bin/bash

sudo service toxiproxy restart
sudo /home/ubuntu/toxiproxy_setup_proxies.sh
sudo supervisorctl restart data-server
sudo supervisorctl restart main-service
