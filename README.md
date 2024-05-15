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

## attivare accessgui

`cd serviceAccessGUI_sprintxxx`
` ./gradlew bootRun`
` http://localhost:8086/`

## attivare statusgui

`cd sprint3/sprint3_statusGUI`
` ./gradlew bootRun`
` http://localhost:8087/`

## opzionale : mqtt sub locale

`mosquitto_sub -v -h "localhost" -t "trolley_state"`

## come eseguire

`docker-compose -f docker-compose.yaml up`
