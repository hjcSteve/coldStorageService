%====================================================================================
% system description   
%====================================================================================
request( storerequest, storerequest(FW) ).
request( dischargefood, dischargefood(TICKETID) ).
request( spaceCheck, spaceCheck(KG) ).
dispatch( dischargeTrolley, dischargeTrolley(TICKETID) ).
request( discharged_trolley, discharged_trolley(TICKETID) ).
dispatch( gohome, gohome(ARG) ).
dispatch( trolley_isindoor, trolley_isindoor(D) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
request( engage, engage(ARG) ).
request( robotposition, robotposition(D) ).
request( currentstatusrequest, currentstatusrequest(D) ).
request( currentloadrequest, currentloadrequest(ARG) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8056").
context(ctxbasicrobot, "localhost",  "TCP", "8059").
 qactor( serviceaccessgui, ctxcoldstorageservice, "it.unibo.serviceaccessgui.Serviceaccessgui").
 static(serviceaccessgui).
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
 static(warningdevice).
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
 static(alarmdevice).
  qactor( coldroom, ctxcoldstorageservice, "it.unibo.coldroom.Coldroom").
 static(coldroom).
  qactor( servicestatusgui, ctxcoldstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
 static(servicestatusgui).
  qactor( basicrobot, ctxbasicrobot, "external").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
 static(coldstorageservice).
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
 static(transporttrolley).
