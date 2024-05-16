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
with Diagram('coldstorageserviceArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxcoldstorageservice', graph_attr=nodeattr):
          servicestatusgui=Custom('servicestatusgui','./qakicons/symActorSmall.png')
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          serviceaccessgui=Custom('serviceaccessgui','./qakicons/symActorSmall.png')
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
          coldroom=Custom('coldroom','./qakicons/symActorSmall.png')
          alarmdevice=Custom('alarmdevice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<discharged_trolley<font color="darkgreen"> idle_trolley serve_newtruck</font> &nbsp; >',  fontcolor='magenta') >> coldstorageservice
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<spaceCheck<font color="darkgreen"> space_insufficient space_reserved</font> &nbsp; >',  fontcolor='magenta') >> coldroom
     coldstorageservice >> Edge(color='blue', style='solid',  decorate='true', label='<dischargeTrolley &nbsp; >',  fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='blue', style='solid',  decorate='true', label='<setdirection &nbsp; disengage &nbsp; >',  fontcolor='blue') >> basicrobot
     transporttrolley >> Edge(color='blue', style='solid',  decorate='true', label='<chargeTaken &nbsp; >',  fontcolor='blue') >> coldstorageservice
     coldstorageservice >> Edge(color='blue', style='solid',  decorate='true', label='<stored_food &nbsp; >',  fontcolor='blue') >> coldroom
diag
