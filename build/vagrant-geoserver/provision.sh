# Install JRE for GeoServer
echo ' '
echo --- Installing JRE ---
sudo add-apt-repository ppa:openjdk-r/ppa -y
sudo apt-get update
sudo apt-get install -y openjdk-8-jre -y
echo "Java installed?"
# sleep 20s

# Config JRE - still needs to be fixed
GEOSERVER_VERSION=2.10.1
export GEOSERVER_VERSION
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export JAVA_HOME

echo 'JAVA_HOME ' $JAVA_HOME

echo ' '
echo --- Installing unzip ---

# Install unzip
sudo apt-get install -y unzip
sudo apt-get install -y tomcat7

echo ' '
echo --- Setting Up for GeoServer ---
echo "export GEOSERVER_HOME=/usr/local/geoserver/" >> ~/.profile
. ~/.profile

sudo rm -rf /usr/local/geoserver/

mkdir /usr/local/geoserver/

sudo chown -R vagrant /usr/local/geoserver/

cd /usr/local

echo ' '
echo --- Downloading GeoServer package - please wait ---

wget -O tmp.zip http://sourceforge.net/projects/geoserver/files/GeoServer/$GEOSERVER_VERSION/geoserver-$GEOSERVER_VERSION-bin.zip && unzip tmp.zip -d /usr/local/ && rm tmp.zip

echo ' '
echo --- Package unzipped - configuring GeoServer directory ---
cp -r /usr/local/geoserver-$GEOSERVER_VERSION/* /usr/local/geoserver && sudo rm -rf /usr/local/geoserver-$GEOSERVER_VERSION/

echo --- Downloading GeoServer WPS package - please wait ---

wget -O tmp.zip http://sourceforge.net/projects/geoserver/files/GeoServer/$GEOSERVER_VERSION/extensions/geoserver-$GEOSERVER_VERSION-wps-plugin.zip && mkdir /usr/local/wps && unzip tmp.zip -d /usr/local/wps && rm tmp.zip

echo ' '
echo --- Package unzipped - configuring GeoServer directory ---
cp -r /usr/local/wps/* /usr/local/geoserver/webapps/geoserver/WEB-INF/lib && sudo rm -rf /usr/local/wps/

echo ' '
echo --- Package unzipped - configuring GeoServer directory ---

echo ' '
echo --- GeoServer Installed ---

echo ' '
echo --- Getting ready to run GeoServer ---

sudo chown -R vagrant /usr/local/geoserver/

cd /usr/local/geoserver/bin

# Geoserver configuration - use /etc/default/geoserver to override these vars
# user that shall run GeoServer
USER=geoserver
GEOSERVER_DATA_DIR=/usr/local/geoserver/data_dir
export GEOSERVER_DATA_DIR
GEOSERVER_HOME=/usr/local/geoserver/
export GEOSERVER_HOME

PATH=/usr/sbin:/usr/bin:/sbin:/bin
DESC="GeoServer daemon"
NAME=geoserver
JAVA_OPTS="-Xms128m -Xmx512m"
DAEMON="$JAVA_HOME/bin/java"
PIDFILE=/var/run/$NAME.pid
SCRIPTNAME=/etc/init.d/$NAME

# Read configuration variable file if it is present
[ -r /etc/default/$NAME ] && . /etc/default/$NAME

DAEMON_ARGS="$JAVA_OPTS $DEBUG_OPTS -DGEOSERVER_DATA_DIR=$GEOSERVER_DATA_DIR -Djava.awt.headless=true -jar start.jar"

# Load the VERBOSE setting and other rcS variables
[ -f /etc/default/rcS ] && . /etc/default/rcS

# Define LSB log_* functions.
# Depend on lsb-base (>= 3.0-6) to ensure that this file is present.
. /lib/lsb/init-functions

echo ' '
echo --- Launching GeoServer startup script ---
echo --- This will run in the background with nohup mode ---
echo --- To access the server, use vagrant ssh ---
echo --- To view the web client go to http://localhost:8081/geoserver ---
echo ' '

# run startup script and have it run in the background - output logged to nohup.out

echo "USER is" $USER
echo "GEOSERVER_DATA_DIR is" $GEOSERVER_DATA_DIR
echo "GEOSERVER_HOME is " $GEOSERVER_HOME

cd /usr/local/geoserver/bin/
echo " "
echo "Working directory:"
pwd
echo "--------"
echo "Starting up GeoServer"
sh /usr/local/geoserver/bin/startup.sh 0<&- &>/dev/null &
