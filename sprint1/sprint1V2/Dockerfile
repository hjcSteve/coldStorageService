FROM openjdk:12.0.2
EXPOSE 8055
EXPOSE 8020
ADD ./build /coldstorageservice/build
WORKDIR /coldstorageservice
COPY ./*.pl ./
COPY ./*.json ./
#COPY ./*.bin ./
COPY ./*.txt ./

CMD ["bash", "./build/bootScripts/unibo.coldstorageservice"]