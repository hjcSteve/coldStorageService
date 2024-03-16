%====================================================================================
% system description   
%====================================================================================
request( storerequest, storerequest(FW) ).
request( dischargefood, dischargefood(TICKETID) ).
request( spaceCheck, spaceCheck(KG) ).
dispatch( dischargeTrolley, dischargeTrolley(WEIGHT) ).
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
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
  qactor( coldroom, ctxcoldstorageservice, "it.unibo.coldroom.Coldroom").
  qactor( servicestatusgui, ctxcoldstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( basicrobot, ctxbasicrobot, "external").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
