/* Generated by AN DISI Unibo */ 
package it.unibo.coldroom

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
class Coldroom ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
			val MAX_STG : Int =Configuration.conf.MAX_STG;
			var current_STG : Int =0;
			var reserved_STG : Int =0;
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("$name) I am started with a Maximum $MAX_STG, currently $current_STG !")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting_state", cond=doswitch() )
				}	 
				state("waiting_state") { //this:State
					action { //it:State
						CommUtils.outblack("$name) Okay then, the storage is $current_STG kg, with a reserved $reserved_STG kg")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t26",targetState="check_state",cond=whenRequest("spaceCheck"))
					transition(edgeName="t27",targetState="store_state",cond=whenDispatch("stored_food"))
				}	 
				state("check_state") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("spaceCheck(KG)"), Term.createTerm("spaceCheck(KG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											var Kg : Int = payloadArg(0);
								if(  MAX_STG >= current_STG + reserved_STG + kg 
								 ){
												reserved_STG+=kg;
												
								answer("spaceCheck", "space_reserved", "space_reserved($Kg)"   )  
								}
								else
								 {CommUtils.outblack("$name) not enough space for reservation...")
								 answer("spaceCheck", "space_insufficient", "space_insufficient(D)"   )  
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting_state", cond=doswitch() )
				}	 
				state("store_state") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("stored_food(KG)"), Term.createTerm("stored_food(KG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
											var Kg : Int = payloadArg(0);
											reserved_STG -= Kg;
											current_STG += Kg;
								CommUtils.outblack(" $name) Performed the load with success!")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting_state", cond=doswitch() )
				}	 
			}
		}
} 
