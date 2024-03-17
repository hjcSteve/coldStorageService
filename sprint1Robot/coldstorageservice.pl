%====================================================================================
% coldstorageservice description   
%====================================================================================
request( storerequest, storerequest(FW) ).
reply( ticketAccepted, ticketAccepted(TICKETNUMBER) ).  %%for storerequest
reply( replyTicketDenied, ticketDenied(ARG) ).  %%for storerequest
request( dischargefood, dischargefood(TICKETNUM) ).
reply( replyChargeTaken, replyChargeTaken(ARG) ).  %%for dischargefood
reply( replyTicketExpired, replyTicketExpired(ARG) ).  %%for dischargefood
request( engage, engage(OWNER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
dispatch( cmd, cmd(MOVE) ).
dispatch( end, end(ARG) ).
request( step, step(TIME) ).
reply( stepdone, stepdone(V) ).  %%for step
reply( stepfailed, stepfailed(DURATION,CAUSE) ).  %%for step
request( doplan, doplan(PATH,OWNER,STEPTIME) ).
reply( doplandone, doplandone(ARG) ).  %%for doplan
reply( doplanfailed, doplanfailed(ARG) ).  %%for doplan
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
dispatch( setrobotstate, setpos(X,Y,D) ).
dispatch( setdirection, dir(D) ).
request( getrobotstate, getrobotstate(ARG) ).
reply( robotstate, robotstate(POS,DIR) ).  %%for getrobotstate
dispatch( truckArrived, truckArrived(TICKETID) ).
dispatch( chargeTaken, chargeTaken(TICKETID) ).
request( chargeDeposited, foodStored(TICKETID) ).
reply( newTicket, newTicket(TICKETID) ).  %%for chargeDeposited
reply( noTIcket, noTicket(ARG) ).  %%for chargeDeposited
event( resume, resume(ARG) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8055").
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( servicestatusgui, ctxcoldstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( serviceaccessgui, ctxcoldstorageservice, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
  qactor( coldroom, ctxcoldstorageservice, "it.unibo.coldroom.Coldroom").
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
