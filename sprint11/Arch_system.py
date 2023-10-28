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
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          coldroom=Custom('coldroom','./qakicons/symActorSmall.png')
          servicestatusgui=Custom('servicestatusgui','./qakicons/symActorSmall.png')
     with Cluster('ctxBasicRobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<kgUpdateRequest &nbsp; >',  fontcolor='magenta') >> transporttrolley
     servicestatusgui >> Edge(color='magenta', style='solid', decorate='true', label='<dischargefood &nbsp; storerequest &nbsp; >',  fontcolor='magenta') >> coldstorageservice
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<engage &nbsp; >',  fontcolor='magenta') >> basicrobot
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<currentloadrequest &nbsp; >',  fontcolor='magenta') >> coldroom
diag
