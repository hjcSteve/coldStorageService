### **Navigation**
- [index]
- [previous] |
- [iss23 1.0 documentation](index.html) »
- TemaFinale23
# <a name="temafinale23"></a>**TemaFinale23[¶**](#temafinale23 "Permalink to this headline")**
A company intends to build a ColdStorageService, composed of a set of elements:

1. a service area (rectangular, flat) that includes:
   1. an INDOOR port, to enter food (fruits, vegetables, etc. )
   1. a ColdRoom container, devoted to store food, upto **MAXW** kg .

The ColdRoom is positioned within the service area, as shown in the following picture:

![_images/ColdStorageServiceRoomAnnoted.PNG](Aspose.Words.f3a645e3-91a9-4a66-8b23-625cac4ade57.001.png)[](_images/ColdStorageServiceRoomAnnoted.PNG)

1. a DDR robot working as a transport trolley, that is intially situated in its HOME location. The transport trolley has the form of a square of side length **RD**.

   The *transport trolley* is used to perform a deposit action that consists in the following phases:

   1. pick up a food-load from a Fridge truck located on the INDOOR
   1. go from the *INDOOR* to the PORT of the ColdRoom
   1. deposit the food-load in the ColdRoom
1. a ServiceAcessGUI that allows an human being to see the current current weigth of the material stored in the ColdRoom and to send to the ColdStorageService a request to store new **FW** kg of food. If the request is accepted, the services return a ticket that expires after a prefixed amount of time (**TICKETTIME** secs) and provides a field to enter the ticket number when a *Fridge truck* is at the INDOOR of the service.
1. a ServiceStatusGUI that allows a Service-manager (an human being) to supervises the state of the service.
## <a name="alarm-requirements"></a>**Alarm requirements[¶**](#alarm-requirements "Permalink to this headline")**
The system includes a a Sonar and a Led connected to a RaspnerryPi.

The Sonar is used as an ‘alarm device’: when it measures a distance less that a prefixed value **DLIMT**, the transport trolley must be stopped; it will be resumed when *Sonar* detects again a distance higher than **DLIMT**.

The *Led* is used as a *warning devices*, according to the following scheme:

- the *Led* is **off** when the *transport trolley* is at *HOME*
- the *Led* **blinks** while the *transport trolley* is moving
- the *Led* is **on** when *transport trolley* is stopped.
## <a name="service-users-story"></a>**Service users story[¶**](#service-users-story "Permalink to this headline")**
The story of the ColdStorageService can be summarized as follows:

1. A *Fridge truck* driver uses the *ServiceAcessGUI* to send a request to store its load of **FW** kg. If the request is accepted, the driver drives its truck to the INDOOR of the service, before the ticket exipration time **TICKETTIME**.
1. When the truck is at the INDOOR of the service, the driver uses the *ServiceAcessGUI* to enter the ticket number and waits until the message **charge taken** (sent by the *ColdStorageService*) appears on the *ServiceAcessGUI*. At this point, the truck should leave the INDOOR.
1. When the service accepts a ticket, the *transport trolley* reaches the INDOOR, picks up the food, sends the **charge taken** message and then goes to the ColdRoom to store the food.
1. When the deposit action is terminated, the transport trolley accepts another ticket (if any) or returns to HOME.
1. While the transport trolley is moving, the [Alarm requirements](#alarm-requirements) should be satisfied. However, the transport trolley should not be stopped if some prefixed amount of time (**MINT** msecs) is not passed from the previous stop.
1. A *Service-manager* migtht use the ServiceStatusGUI to see:
   1. the **current state** of the transport trolley and it **position** in the room;
   1. the **current weigth** of the material stored in the ColdRoom;
   1. the **number of store-requests rejected** since the start of the service.

About requirements

The development of the ServiceStatusGUI is optional. However, it is required if the working team is composed of 3 person.
### [**Table of Contents**](index.html)
- [TemaFinale23]()
  - [Alarm requirements](#alarm-requirements)
  - [Service users story](#service-users-story)
#### **Previous topic**
[DeliverableAppl1HTTP](DeliverableAppl1HTTP.html "previous chapter")
### **This Page**
- [Show Source](_sources/TemaFinale23.rst.txt)
###
### **Navigation**
- [index]
- [previous] |
- [iss23 1.0 documentation](index.html) »
- TemaFinale23

© Copyright 2022, Antonio Natali. Created using [Sphinx](https://www.sphinx-doc.org/) 4.4.0. 

[index]: genindex.html "General Index"
[previous]: DeliverableAppl1HTTP.html "DeliverableAppl1HTTP"
