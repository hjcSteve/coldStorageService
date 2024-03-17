### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('systemArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxColdStorageService', graph_attr=nodeattr):
          serviceaccessgui=Custom('serviceaccessgui','./qakicons/symActorSmall.png')
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
          alarmdevice=Custom('alarmdevice','./qakicons/symActorSmall.png')
          coldroom=Custom('coldroom','./qakicons/symActorSmall.png')
          servicestatusgui=Custom('servicestatusgui','./qakicons/symActorSmall.png')
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctxBasicRobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
<<<<<<< HEAD
     servicestatusgui >> Edge(color='magenta', style='solid', decorate='true', label='<dischargefood<font color="darkgreen"> replyChargeTaken replyTicketExpired</font> &nbsp; storerequest<font color="darkgreen"> requestaccepted requestdenied</font> &nbsp; >',  fontcolor='magenta') >> coldstorageservice
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<discharged_trolley &nbsp; >',  fontcolor='magenta') >> coldstorageservice
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<spaceCheck<font color="darkgreen"> space_insufficient space_reserved</font> &nbsp; >',  fontcolor='magenta') >> coldroom
     coldstorageservice >> Edge(color='blue', style='solid',  label='<dischargeTrolley &nbsp; >',  fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='blue', style='solid',  label='<setdirection &nbsp; disengage &nbsp; >',  fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid',  label='<trolley_isindoor &nbsp; >',  fontcolor='blue') >> coldstorageservice
=======
     servicestatusgui >> Edge(color='magenta', style='solid', decorate='true', label='<dischargefood &nbsp; storerequest &nbsp; >',  fontcolor='magenta') >> coldstorageservice
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<engage &nbsp; moverobot &nbsp; >',  fontcolor='magenta') >> basicrobot
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<spaceCheck &nbsp; >',  fontcolor='magenta') >> coldroom
     coldstorageservice >> Edge(color='blue', style='solid',  decorate='true', label='<dischargeTrolley &nbsp; >',  fontcolor='blue') >> transporttrolley
>>>>>>> cf25c25baf186e6f1633adfa0eca9152449c7ab5
diag
