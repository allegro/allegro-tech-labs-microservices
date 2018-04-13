#!/bin/bash

toxiproxy-cli create mongo -l localhost:27117 -u localhost:27017
toxiproxy-cli create description_service -l localhost:9998 -u localhost:8888
toxiproxy-cli create gallery_service -l localhost:9999 -u localhost:8888
