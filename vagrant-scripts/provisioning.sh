#!/bin/bash

# Installed packages
# * mongo: 3.6
# * docker: for easy Graphite/Grafana deployment
# * toxiproxy: TCP proxy to inject faults & delays in network traffic
# * wrk2: traffic generator with stable rps count
# * supervisor: manage running apps
# * OpenJDK 9

MONGO_VER=3.6
MONGO_PKG_VER=3.6.2

TOXI_VER=2.1.3

### Repositories

# mongo 3.6
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2930ADAE8CAF5059EE73BB4B58712A2291FA4AD5
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/$MONGO_VER multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-$MONGO_VER.list

# docker
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu xenial stable"

# oracle java
add-apt-repository ppa:webupd8team/java

# update apt-get for all new deps
apt-get update

### python & httpie
apt-get install -y python-pip
pip -q install httpie

### mongo
apt-get install -y mongodb-org=$MONGO_PKG_VER mongodb-org-server=$MONGO_PKG_VER mongodb-org-shell=$MONGO_PKG_VER mongodb-org-mongos=$MONGO_PKG_VER mongodb-org-tools=$MONGO_PKG_VER

### wrk2
apt-get install -y build-essential libssl-dev

if [ ! -d /tmp/wrk2 ]; then
  (
    cd /tmp
    git clone https://github.com/giltene/wrk2.git
    cd wrk2
    make
    cp wrk /usr/local/bin
  )
fi

### graphite

apt-get install -y python-dev libcairo2-dev libffi-dev build-essential nginx virtualenv

# everything is installed in virtualenv at /opt/graphite
virtualenv /opt/graphite
source /opt/graphite/bin/activate

export PYTHONPATH="/opt/graphite/lib/:/opt/graphite/webapp/"
pip install --no-binary=:all: https://github.com/graphite-project/whisper/tarball/1.1.3
pip install --no-binary=:all: https://github.com/graphite-project/carbon/tarball/1.1.3
pip install --no-binary=:all: https://github.com/graphite-project/graphite-web/tarball/1.1.3
pip install gunicorn

PYTHONPATH=/opt/graphite/webapp /opt/graphite/bin/django-admin.py migrate --settings=graphite.settings --run-syncdb

# nginx
cat /tmp/vagrant-scripts/graphite-nginx.conf > /etc/nginx/sites-available/graphite
rm -f /etc/nginx/sites-enabled/default
ln -s /etc/nginx/sites-available/graphite /etc/nginx/sites-enabled

touch /var/log/nginx/graphite.access.log
touch /var/log/nginx/graphite.error.log
chmod 640 /var/log/nginx/graphite.*
chown www-data:www-data /var/log/nginx/graphite.*

# carbon cache
cp /opt/graphite/conf/carbon.conf.example /opt/graphite/conf/carbon.conf
cat /tmp/vagrant-scripts/graphite-retention.conf > /opt/graphite/conf/storage-schemas.conf

### grafana
wget -q -O /tmp/grafana.deb https://s3-us-west-2.amazonaws.com/grafana-releases/release/grafana_5.0.4_amd64.deb
dpkg -i /tmp/grafana.deb

### toxiproxy
# install toxiproxi
wget -q -O /tmp/toxiproxy.deb "https://github.com/Shopify/toxiproxy/releases/download/v${TOXI_VER}/toxiproxy_${TOXI_VER}_amd64.deb"
dpkg -i /tmp/toxiproxy.deb

# create toxiproxy user only if needed
id -u toxiproxy &>/dev/null || sudo useradd -s /bin/false -U -M toxiproxy

# startup scripts
cat /tmp/vagrant-scripts/toxiproxy.default > /etc/default/toxiproxy
cat /tmp/vagrant-scripts/toxiproxy.service > /etc/systemd/system/multi-user.target.wants/toxiproxy.service

### supervisor
apt-get install -y supervisor
cat /tmp/vagrant-scripts/supervisor-apps.conf > /etc/supervisor/conf.d/apps.conf
supervisorctl reload

### oraclejdk 8
echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections
apt-get install -y oracle-java8-installer oracle-java8-set-default

### jmxtrans
wget -q -O /tmp/jmxtrans.deb "http://central.maven.org/maven2/org/jmxtrans/jmxtrans/270/jmxtrans-270.deb"
echo "jmxtrans jmxtrans/jvm_heap_size string 128" | debconf-set-selections
dpkg -i /tmp/jmxtrans.deb
cat /tmp/vagrant-scripts/jmxtrans.yaml > /var/lib/jmxtrans/config.yaml

# set default polling period to 10seconds
sed -i.bak "s/60/10/g" /etc/default/jmxtrans


### gradle wrapper warmup
/vagrant/atl-data-server/gradlew tasks --no-daemon

### Copy reset env script
cp /tmp/vagrant-scripts/toxiproxy_setup_proxies.sh /home/vagrant
cp /tmp/vagrant-scripts/reset-env.sh /home/vagrant
cp /tmp/vagrant-scripts/random_offers.lua /home/vagrant
chmod +x /home/vagrant/toxiproxy_setup_proxies.sh
chmod +x /home/vagrant/reset-env.sh

### Services start
systemctl daemon-reload
systemctl enable grafana-server
service nginx restart
service grafana-server start
service toxiproxy start
service jmxtrans start
service mongod start
