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
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( serviceaccessgui, ctxcoldstorageservice, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
  qactor( coldroom, ctxcoldstorageservice, "it.unibo.coldroom.Coldroom").
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( basicrobot, ctxbasicrobot, "external").
