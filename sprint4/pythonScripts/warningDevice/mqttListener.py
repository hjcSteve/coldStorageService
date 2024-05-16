import time
import paho.mqtt.client as paho
import sys

brokerAddr="192.168.61.104"
topic = "trolley_state"


##############################################################
###  msg(sonarRobot,event,sonar,none,sonar(V),N)
##############################################################
def on_message(client, userdata, message) :   #define callback
    #print(f"{message.topic}: {message.payload.decode()}")
    msg=message.payload.decode()
    #print(msg)
    if "going" in msg:
        print("blink")
        sys.stdout.flush()
    elif "atHome" in msg:
        print("off")
        sys.stdout.flush()
    elif "stopped" in msg:
        print("on")
        sys.stdout.flush()
    return

    
client= paho.Client("receiver")      
client.on_message=on_message            # Bind function to callback
client.connect(brokerAddr,1883)              #connect
client.subscribe(topic)      #subscribe
client.loop_forever()