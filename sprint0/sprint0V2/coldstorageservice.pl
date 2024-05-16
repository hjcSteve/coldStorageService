%====================================================================================
% coldstorageservice description   
%====================================================================================
request( storerequest, storerequest(FW) ).
reply( ticketAccepted, ticketAccepted(TICKETNUMBER) ).  %%for storerequest
reply( replyTicketDenied, ticketDenied(ARG) ).  %%for storerequest
request( dischargefood, dischargefood(TICKETNUM) ).
reply( replyChargeTaken, replyChargeTaken(ARG) ).  %%for dischargefood
reply( replyTicketExpired, replyTicketExpired(ARG) ).  %%for dischargefood
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8055").
context(ctxbasicrobot, "localhost",  "TCP", "8059").
 qactor( servicestatusgui, ctxcoldstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
 static(servicestatusgui).
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
 static(coldstorageservice).
  qactor( serviceaccessgui, ctxcoldstorageservice, "it.unibo.serviceaccessgui.Serviceaccessgui").
 static(serviceaccessgui).
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
 static(warningdevice).
  qactor( coldroom, ctxcoldstorageservice, "it.unibo.coldroom.Coldroom").
 static(coldroom).
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
 static(alarmdevice).
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
 static(transporttrolley).
  qactor( basicrobot, ctxbasicrobot, "external").
