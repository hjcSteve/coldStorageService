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
class Coldstorageservice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
			var KgWaitingDischarge : Long = 0;
			var	KgtoLoad : Long = 0;
			var List = tickets.TicketList(Expiration); 	
			
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("$name ) waiting for a new message...")
						discardMessages = false
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handleStoreRequest",cond=whenRequest("storerequest"))
					transition(edgeName="t01",targetState="handleDischargeRequest",cond=whenRequest("dischargefood"))
					transition(edgeName="t02",targetState="handleLoadRequest",cond=whenRequest("currentloadrequest"))
					transition(edgeName="t03",targetState="handleStatusRequest",cond=whenRequest("currentstatusrequest"))
				}	 
				state("handleStoreRequest") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(FW)"), Term.createTerm("storerequest(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								    		KgtoLoad = payloadArg(0).toLong();
								request("currentloadrequest", "currentloadrequest(D)" ,"coldroom" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="handleTicketGeneration",cond=whenReply("replycurrentload"))
				}	 
				state("handleTicketGeneration") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("replycurrentload(WEIGHT)"), Term.createTerm("replycurrentload(CURRLOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								    		var KgAvailable = payloadArg(0).toLong();	 	
								if(  KgAvailable - KgWaitingDischarge >= KgtoLoad  
								 ){
								    			KgWaitingDischarge += KgtoLoad;
								    			var ticket=List.createTicket(KgtoLoad);
											    var TICKETCODE = ticket.getTicketNumber;
											    var TIMESTAMP = ticket.getTimestamp(); 	
								answer("storerequest", "requestaccepted", "requestaccepted($TICKETCODE,$TIMESTAMP)"   )  
								CommUtils.outblack("$name ) ticket accepted!")
								}
								else
								 {answer("storerequest", "requestdenied", "requestdenied(D)"   )  
								 CommUtils.outblack("$name ) ticket denied, not enough space.")
								 }
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
								
								      	var Ticketnum = payloadArg(0);
								      	Ticket ticket = List.getTicket(Ticketnum);
								      	boolean Expired = List.isExpired(ticket);
								if(  !Expired  
								 ){
								      		var KgToStore = ticket.getKgToStore()
											KgWaitingDischarge -= KgToStore;
								CommUtils.outblack("$name ) Sending food to the cold room, lazzaro alzati e cammina")
								request("kgUpdateRequest", "kgUpdateReply(KgToStore)" ,"transporttrolley" )  
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleLoadRequest") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(FW)"), Term.createTerm("storerequest(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								answer("storerequest", "requestaccepted", "requestaccepted($TICKET)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("handleStatusRequest") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(FW)"), Term.createTerm("storerequest(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								answer("storerequest", "requestaccepted", "requestaccepted($TICKET)"   )  
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
