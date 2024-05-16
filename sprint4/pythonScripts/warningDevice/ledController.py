import RPi.GPIO as GPIO
import sys
import time
import select
GPIO.setmode(GPIO.BCM)
GPIO.setup(25,GPIO.OUT,initial=GPIO.LOW)
while True:
    line = sys.stdin.readline()
    try:
        if 'on' in line :
            print('LedDevice on')
            sys.stdout.flush()	
            GPIO.output(25,GPIO.HIGH)
        elif 'off' in line :
            print('LedDevice off')
            sys.stdout.flush()	
            GPIO.output(25,GPIO.LOW)
        elif "blink" in line :
            print('LedDevice blink')
            sys.stdout.flush()	
            while True:
                GPIO.output(25,GPIO.HIGH)
                time.sleep(0.5)
                GPIO.output(25,GPIO.LOW)
                time.sleep(0.5)
                if(sys.stdin in select.select([sys.stdin],[],[],0)[0]):
                    break
        else :
            GPIO.output(25,GPIO.LOW)       
    except:
        print("LedDevice | An exception occurred")
    #sys.stdout.flush()	