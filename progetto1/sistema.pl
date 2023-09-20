%====================================================================================
% sistema description   
%====================================================================================
context(ctxraspberry, "localhost",  "TCP", "8055").
context(ctxcoldstorageservice, "localhost",  "TCP", "8056").
context(ctxserviceaccessgui, "localhost",  "TCP", "8057").
context(ctxservicestatusgui, "localhost",  "TCP", "8058").
context(ctxbasicrobot, "localhost",  "TCP", "8059").
 qactor( serviceaccessgui, ctxserviceaccessgui, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( warningdevice, ctxraspberry, "it.unibo.warningdevice.Warningdevice").
  qactor( alarmdevice, ctxraspberry, "it.unibo.alarmdevice.Alarmdevice").
  qactor( coldstorageservice, ctxcoldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctxcoldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( servicestatusgui, ctxservicestatusgui, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( basicrobot, ctxbasicrobot, "external").
