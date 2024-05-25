#! /bin/bash
#mi sposto nella cartella del progetto
# sudo chmod +x ./deploy.sh
#faccio il build del progetto
./gradlew clean build
#faccio il build dell'immagine docker e la taggo con il nome hjcsteve/coldstorageservice:1.2
docker build -t hjcsteve/statusgui:1.0 .
#faccio il push dell'immagine su dockerhub di steve :) 
#se non avete le credenziali non funziona
docker push hjcsteve/statusgui:1.0
