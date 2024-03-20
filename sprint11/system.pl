%====================================================================================
% system description   
%====================================================================================
request( storerequest, storerequest(FW) ).
reply( requestaccepted, requestaccepted(TICKETID,TIMESTAMP) ).  %%for storerequest
reply( requestdenied, requestdenied(D) ).  %%for storerequest
request( dischargefood, dischargefood(TICKETID) ).
reply( replyChargeTaken, replyChargeTaken(D) ).  %%for dischargefood
reply( replyTicketExpired, replyTicketExpired(D) ).  %%for dischargefood
request( spaceCheck, spaceCheck(KG) ).
reply( space_insufficient, space_insufficient(D) ).  %%for spaceCheck
reply( space_reserved, space_reserved(D) ).  %%for spaceCheck
dispatch( dischargeTrolley, dischargeTrolley(TICKETID) ).
request( discharged_trolley, discharged_trolley(TICKETID) ).
dispatch( gohome, gohome(ARG) ).
dispatch( trolley_isindoor, trolley_isindoor(D) ).
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
event( resume, resume(ARG) ).
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
