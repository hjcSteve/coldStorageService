# coldStorageService
tema finale ingegneria dei sistemi software 22/23

## Per attivare il robot
`cd unibo.basicrobot23`
`sudo docker-compose -f basicrobot23.yaml up`


## Attivare il broker mqttt
`sudo docker run -p 1833:1833 eclipse-mosquitto`
## attivare il coldstorage

`cd sprintxxx`
`./gradlew run`


## opzionale : mqtt sub locale
`mosquitto_sub -v -h "localhost" -t "trolley_state"`
