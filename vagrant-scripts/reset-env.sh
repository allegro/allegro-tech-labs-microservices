#!/bin/bash

sudo service toxiproxy restart
sudo ./toxiproxy_setup_proxies.sh
sudo supervisorctl restart data-server
sudo supervisorctl restart main-service