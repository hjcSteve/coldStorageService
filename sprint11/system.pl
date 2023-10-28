%====================================================================================
% system description   
%====================================================================================
request( storerequest, storerequest(FW) ).
request( dischargefood, dischargefood(TICKETNUM) ).
request( kgAvailableRequest, kgAvailableRequest(D) ).
request( kgUpdateRequest, kgUpdateRequest(KG) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
request( engage, engage(ARG) ).
request( robotposition, robotposition(D) ).
request( currentstatusrequest, currentstatusrequest(D) ).
request( currentloadrequest, currentloadrequest(R) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8056").
context(ctxbasicrobot, "localhost",  "TCP", "8059").
 qactor( serviceaccessgui, ctxcoldstorageservice, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( coldroom, ctxcoldstorageservice, "it.unibo.coldroom.Coldroom").
  qactor( servicestatusgui, ctxcoldstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( basicrobot, ctxbasicrobot, "external").
