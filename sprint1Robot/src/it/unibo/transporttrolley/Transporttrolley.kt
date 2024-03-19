/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

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
class Transporttrolley ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var lastState: String = "" 
			var ticketID:String= ""
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name | init e engage basicrobot")
						request("engage", "engage(transporttrolley,300)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t08",targetState="engaged",cond=whenReply("engagedone"))
					transition(edgeName="t09",targetState="quit",cond=whenReply("engagerefused"))
				}	 
				state("engaged") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name | basicrobot engaged")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="atHome", cond=doswitch() )
				}	 
				state("atHome") { //this:State
					action { //it:State
						 lastState = "atHome"  
						CommUtils.outmagenta("$name | basicrobot at Home")
						forward("setdirection", "dir(down)" ,"basicrobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t010",targetState="goingIndoor",cond=whenDispatch("dischargeTrolley"))
				}	 
				state("goingIndoor") { //this:State
					action { //it:State
						 lastState = "goingIndoor"  
						CommUtils.outmagenta("$name | vado all'INDOOR")
						request("moverobot", "moverobot(0,4)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t011",targetState="atIndoor",cond=whenReply("moverobotdone"))
				}	 
				state("atIndoor") { //this:State
					action { //it:State
						 lastState = "atIndoor"  
						CommUtils.outmagenta("$name | sono in INDOOR")
						CommUtils.outmagenta("$name | carico il cibo")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_atIndoor", 
				 	 					  scope, context!!, "local_tout_transporttrolley_atIndoor", 3000.toLong() )
					}	 	 
					 transition(edgeName="t12",targetState="loadDone",cond=whenTimeout("local_tout_transporttrolley_atIndoor"))   
				}	 
				state("loadDone") { //this:State
					action { //it:State
						forward("chargeTaken", "chargetaken(CIAO)" ,"coldstorageservice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="goingColdroom", cond=doswitch() )
				}	 
				state("goingColdroom") { //this:State
					action { //it:State
						 lastState = "goingColdroom"  
						CommUtils.outmagenta("$name | vado verso la cold room")
						request("moverobot", "moverobot(4,3)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t013",targetState="atColdroom",cond=whenReply("moverobotdone"))
				}	 
				state("atColdroom") { //this:State
					action { //it:State
						 lastState = "atColdroom"  
						CommUtils.outmagenta("$name | sono in Cold Room")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_atColdroom", 
				 	 					  scope, context!!, "local_tout_transporttrolley_atColdroom", 3000.toLong() )
					}	 	 
					 transition(edgeName="t014",targetState="chargeStored",cond=whenTimeout("local_tout_transporttrolley_atColdroom"))   
				}	 
				state("chargeStored") { //this:State
					action { //it:State
						 lastState = "chargedStored"  
						CommUtils.outmagenta("$name | terminato deposito. Aspetto istruzioni")
						request("discharged_trolley", "discharged_trolley(TICKETID)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t015",targetState="goingIndoor",cond=whenReply("idle_trolley"))
					transition(edgeName="t016",targetState="goingHome",cond=whenReply("serve_newtruck"))
				}	 
				state("goingHome") { //this:State
					action { //it:State
						 lastState = "goingHome"  
						CommUtils.outmagenta("$name | vado alla posizione HOME")
						request("moverobot", "moverobot(0,0)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t017",targetState="atHome",cond=whenReply("moverobotdone"))
				}	 
				state("stopped") { //this:State
					action { //it:State
						discardMessages = true
						CommUtils.outmagenta("$name | Sono fermo per ostacolo sonar")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t018",targetState="atHome",cond=whenEventGuarded("resume",{ lastState == "atHome"  
					}))
					transition(edgeName="t019",targetState="goingIndoor",cond=whenEventGuarded("resume",{ lastState == "goingIndoor"  
					}))
					transition(edgeName="t020",targetState="atIndoor",cond=whenEventGuarded("resume",{ lastState == "atIndoor"  
					}))
					transition(edgeName="t021",targetState="goingColdroom",cond=whenEventGuarded("resume",{ lastState == "goingColdroom"  
					}))
					transition(edgeName="t022",targetState="atColdroom",cond=whenEventGuarded("resume",{ lastState == "atColdroom"  
					}))
					transition(edgeName="t023",targetState="chargeStored",cond=whenEventGuarded("resume",{ lastState == "chargeStored"  
					}))
					transition(edgeName="t024",targetState="goingHome",cond=whenEventGuarded("resume",{ lastState == "goingHome"  
					}))
				}	 
				state("quit") { //this:State
					action { //it:State
						forward("disengage", "disengage(transporttrolley)" ,"basicrobot" ) 
						 System.exit(0)  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
} 
