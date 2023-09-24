%====================================================================================
% sistema description   
%====================================================================================
context(ctxcoldstorageservice, "localhost",  "TCP", "8055").
context(ctxbasicrobot, "localhost",  "TCP", "8059").
 qactor( serviceaccessgui, ctxcoldstorageservice, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( warningdevice, ctxcoldstorageservice, "it.unibo.warningdevice.Warningdevice").
  qactor( alarmdevice, ctxcoldstorageservice, "it.unibo.alarmdevice.Alarmdevice").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( servicestatusgui, ctxcoldstorageservice, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( basicrobot, ctxbasicrobot, "external").
