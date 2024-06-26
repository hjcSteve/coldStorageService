/* Generated by AN DISI Unibo */ 
package it.unibo.coldstorageservice

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023

//User imports JAN2024

class Coldstorageservice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		//val interruptedStateTransitions = mutableListOf<Transition>()
		
			
			var Trolley_is_working : Boolean = false;
			var	KgtoLoad : Int = 0;
			var	Expiration : Long = Configuration.conf.Expirationtime;
			var List = tickets.TicketList(Expiration);
			var servingTicket = tickets.Ticket();
			var queuedTicket = tickets.Ticket();
			var is_queued : Boolean = false;
			var Ticketnum : Int = 0;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name ) waiting for a new message...")
						discardMessages = false
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleStoreRequest",cond=whenRequest("storerequest"))
					transition(edgeName="t01",targetState="handleDischargeRequest",cond=whenRequest("dischargefood"))
					transition(edgeName="t02",targetState="handleTrolley_atColdroom",cond=whenRequest("discharged_trolley"))
					transition(edgeName="t03",targetState="clearIndoor",cond=whenDispatch("chargeTaken"))
				}	 
				state("handleStoreRequest") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(FW)"), Term.createTerm("storerequest(KG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
													KgtoLoad = payloadArg(0).toInt();
								CommUtils.outmagenta("$name ) asking to coldRoom")
								request("spaceCheck", "spaceCheck($KgtoLoad)" ,"coldroom" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="handleTicketGeneration",cond=whenReply("space_reserved"))
					transition(edgeName="t05",targetState="refuseStoreReq",cond=whenReply("space_insufficient"))
				}	 
				state("refuseStoreReq") { //this:State
					action { //it:State
						answer("storerequest", "replyTicketDenied", "ticketDenied(D)"   )  
						CommUtils.outmagenta("$name ) ticket denied, not enough space.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleTicketGeneration") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("space_reserved(D)"), Term.createTerm("space_reserved(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											var ticket=List.createTicket(KgtoLoad);
										    var TICKETCODE = ticket.getTicketNumber();
										    var TIMESTAMP = ticket.getTimestamp();
								
								answer("storerequest", "ticketAccepted", "ticketAccepted($TICKETCODE,$TIMESTAMP)"   )  
								CommUtils.outmagenta("$name ) ticket accepted! Ticket $TICKETCODE emitted. ")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleDischargeRequest") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("dischargefood(TICKETNUM)"), Term.createTerm("dischargefood(TICKETNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
									        Ticketnum = payloadArg(0).toInt();
									      	val ticket = List.getTicket(Ticketnum);
									      	var Expired : Boolean = true;
									      	if(ticket!==null){
										       Expired = List.isExpired(ticket);
									      	}
								
								if(  ticket !== null  
								 ){if(  !Expired  && !Trolley_is_working 
								 ){CommUtils.outmagenta("$name ) Sending food to the cold room, lazzaro alzati e cammina")
								
										      		Trolley_is_working=true;
										      		servingTicket= ticket;
										      		ticket.setStatus(1)
									
								forward("dischargeTrolley", "dischargeTrolley($Ticketnum)" ,"transporttrolley" ) 
								}
								else
								 {if(  !Expired  
								  ){CommUtils.outmagenta("$name ) Truck is already serving another truck, let's queue the ticket $Ticketnum")
								 
								 			      		queuedTicket=ticket;
								 			      		is_queued=true;
								 			      		ticket.setStatus(1)
								 }
								 else
								  {CommUtils.outmagenta("$name ) The ticket has expired... sending notification to SAGui")
								  
								  		      			ticket.setStatus(2)
								  answer("dischargefood", "replyTicketExpired", "replyTicketExpired($Ticketnum)"   )  
								  }
								 }
								}
								else
								 {CommUtils.outmagenta("$name ) Ticket not valid")
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleTrolley_atColdroom") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("discharged_trolley(TICKETID)"), Term.createTerm("discharged_trolley(TICKETNUM)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
										    		 Ticketnum = payloadArg(0).toInt();
										    		 val Kg : Int = servingTicket.getKgToStore();
									
								CommUtils.outmagenta("$name) received back discharged_trolley with id : $Ticketnum")
								if(  servingTicket.getTicketNumber() == Ticketnum  && is_queued  
								 ){
										    		servingTicket = queuedTicket;
										    		val ServingId : Int = servingTicket.getTicketNumber().toInt();
										    		is_queued=false;
									
								answer("discharged_trolley", "serve_newtruck", "serve_newtruck($ServingId)"   )  
								}
								else
								 {if(  servingTicket.getTicketNumber() == Ticketnum  
								  ){
								 			    		Trolley_is_working = false;
								 answer("discharged_trolley", "idle_trolley", "idle_trolley(D)"   )  
								 }
								 else
								  {CommUtils.outmagenta("$name) Error! Not corresponding serving ticket")
								  }
								 }
								forward("stored_food", "stored_food($Kg)" ,"coldroom" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("clearIndoor") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargeTaken(TICKETID)"), Term.createTerm("chargeTaken(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outmagenta("$name ) cleaning Indoor!!")
								 Ticketnum = payloadArg(0).toInt(); 
								answer("dischargefood", "replyChargeTaken", "replyChargeTaken($Ticketnum)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
} 
