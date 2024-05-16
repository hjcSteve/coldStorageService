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
dispatch( dischargeTrolley, dischargeTrolley(TICKETID) ).
dispatch( chargeTaken, chargeTaken(TICKETID) ).
request( discharged_trolley, discharged_trolley(TICKETID) ).
reply( idle_trolley, idle_trolley(D) ).  %%for discharged_trolley
reply( serve_newtruck, serve_newtruck(TICKET) ).  %%for discharged_trolley
request( spaceCheck, spaceCheck(KG) ).
reply( space_insufficient, space_insufficient(D) ).  %%for spaceCheck
reply( space_reserved, space_reserved(D) ).  %%for spaceCheck
dispatch( stored_food, stored_food(KG) ).
event( alarm, alarm(X) ).
event( resume, resume(ARG) ).
event( coldstorage_state, coldstorage_state(X) ).
event( trolley_state, trolley_state(X) ).
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8055").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
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
