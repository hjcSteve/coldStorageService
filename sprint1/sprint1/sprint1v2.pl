%====================================================================================
% sprint1v2 description   
%====================================================================================
request( storerequest, storerequest(FW) ).
reply( requestaccepted, requestaccepted(TICKETNUM,TIMESTAMP) ).  %%for storerequest
reply( requestdenied, requestdenied(D) ).  %%for storerequest
request( dischargefood, dischargefood(TICKETNUM) ).
reply( serviceterminated, serviceterminated(ACKNOWLEDGEMENT) ).  %%for dischargefood
request( kgAvailableRequest, kgAvailableRequest(D) ).
request( kgUpdateRequest, kgUpdateRequest(KG) ).
dispatch( gohome, gohome(ARG) ).
dispatch( dischargeTrolley, dischargeTrolley(WEIGHT) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
request( engage, engage(ARG) ).
request( robotposition, robotposition(D) ).
request( currentstatusrequest, currentstatusrequest(D) ).
request( currentloadrequest, currentloadrequest(ARG) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8056").
context(ctxbasicrobot, "localhost",  "TCP", "8059").
 qactor( servicestatusgui, ctxcoldstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
 static(servicestatusgui).
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
 static(warningdevice).
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
 static(alarmdevice).
  qactor( coldroom, ctxcoldstorageservice, "it.unibo.coldroom.Coldroom").
 static(coldroom).
  qactor( serviceaccessgui, ctxcoldstorageservice, "it.unibo.serviceaccessgui.Serviceaccessgui").
 static(serviceaccessgui).
  qactor( basicrobot, ctxbasicrobot, "external").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
 static(coldstorageservice).
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
 static(transporttrolley).
