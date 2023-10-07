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
with Diagram('sistemaArch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
     with Cluster('ctxColdStorageService', graph_attr=nodeattr):
          serviceaccessgui=Custom('serviceaccessgui','./qakicons/symActorSmall.png')
          servicestatusgui=Custom('servicestatusgui','./qakicons/symActorSmall.png')
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
          alarmdevice=Custom('alarmdevice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctxBasicRobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
diag