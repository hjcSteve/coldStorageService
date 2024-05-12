FROM gradle:7.4.2
WORKDIR /usr/app/
COPY . . 
RUN mkdir -p /usr/unibolibs/
COPY  ./unibolibs/ /usr/unibolibs/
ENTRYPOINT gradle run
