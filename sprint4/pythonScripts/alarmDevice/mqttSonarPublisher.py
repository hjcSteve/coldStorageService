import time
import paho.mqtt.client as paho
import sys
brokerAddr="192.168.61.104"
topic = "sonardata"
duration = 20

##############################################################
###  msg(sonarRobot,event,sonar,none,sonar(V),N)
##############################################################
    
client= paho.Client("publisher")      
client.connect(brokerAddr,1883)         
while True:
    line = sys.stdin.readline().rstrip('\n')
    client.publish(topic,line)
    sys.stdout.flush()  
