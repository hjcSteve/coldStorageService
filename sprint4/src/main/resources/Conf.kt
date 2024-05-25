data class Conf(
        val MAX_STG: Int = 100,
        val Expirationtime: Long = 100000,
        val MINT:Long=5000,
        val DLIMT:Long= 30,
        val broker_address: String= "tcp://localhost:1883",
        val trolley_state_topic : String= "trolley_state",
        val sonardata_topic : String= "sonardata")
