package test.it.unibo.ctxcoldstorageservice

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.utils.CommUtils
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Socket
import unibo.basicomm23.tcp.TcpClientSupport
import unibo.basicomm23.mqtt.MqttConnection


import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class MainCtxcoldstorageserviceKtTest {
    val myMqttCallback : MessageListener = MessageListener();

    val port=8055;
    companion object {
        val topic="trolley_state"
        val mqSender="tester"
        val mqttBrokerAddr= "tcp://localhost"
        val conn = TcpClientSupport.connect("localhost",8055,5)
        val robotconn = TcpClientSupport.connect("localhost",8090,5)
        val  mqttConn : MqttConnection = MqttConnection.create();


        @JvmStatic
        @BeforeClass
        fun setUp(): Unit {
            println("set up");
//            try{
//                mqttConn.connect(mqSender,mqttBrokerAddr)
//            }catch (e:Exception){
//                println(e.toString())
//            }

        }
    }

    @Test
    fun ` test store request`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "storerequest", "storerequest(10)", "coldstorageservice").toString()
        println(truckRequestStr);
        val responseMessage = conn.request(truckRequestStr)
        println(responseMessage)
        assertTrue("TEST___ il ticket accettato ",
                responseMessage.contains("ticketAccepted"));
    }

    @Test
    fun ` test store request 1000`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "storerequest", "storerequest(1000)", "coldstorageservice").toString()
        println(truckRequestStr);
        val responseMessage = conn.request(truckRequestStr)
        println(responseMessage)
        assertTrue("TEST___ il ticket rifiutato",
                responseMessage.contains("ticketDenied"));
    }

    @Test
    fun ` test discarge request with ticketnumer1`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "dischargefood", "dischargefood(1)", "coldstorageservice").toString()
        println(truckRequestStr);
        val responseMessage = conn.request(truckRequestStr)
        println(responseMessage)
        assertTrue("TEST___ charge taken",
                responseMessage.contains("replyChargeTaken"));
    }
    @Test
    fun ` test discarge request with new ticket in queue`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "dischargefood", "dischargefood(1)", "coldstorageservice").toString()
        println(truckRequestStr);
        val responseMessage = conn.request(truckRequestStr)
        println(responseMessage)
        assertTrue("TEST___ charge taken",
                responseMessage.contains("replyChargeTaken"));
        val truckRequestStr2 = CommUtils.buildRequest("tester", "dischargefood", "dischargefood(2)", "coldstorageservice").toString()
        println(truckRequestStr2);
        val responseMessage2 = conn.request(truckRequestStr2)
        assertTrue("TEST___ charge taken",
                responseMessage2.contains("replyChargeTaken"));
    }
    @Test
    fun ` test discarge request with new ticket in queue con alarm`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "dischargefood", "dischargefood(3)", "coldstorageservice").toString()
        println(truckRequestStr);
        val responseMessage = conn.request(truckRequestStr)
        assertTrue("TEST___ charge taken",
                responseMessage.contains("replyChargeTaken"));
        Thread.sleep(1000)
        val alarm = CommUtils.buildEvent("tester", "alarm", "alarm(X)").toString();
        conn.forward(alarm)
        Thread.sleep(2000)
        val resume = CommUtils.buildEvent("tester", "resume", "resume(ARG)").toString();
        conn.forward(resume)
        println(responseMessage)


    }

    @Test
    fun ` test discarge request exipired`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "dischargefood", "dischargefood(0)", "coldstorageservice").toString()
        println(truckRequestStr);
        val responseMessage = conn.request(truckRequestStr)
        println(responseMessage)
        assertTrue("TEST___ ticket expired",
                responseMessage.contains("replyTicketExpired"));
    }
    @Test
    fun ` test virtualrobot`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "step", "step(TIME)", "basicrobot23").toString()
        println(truckRequestStr);
        val responseMessage = robotconn.request(truckRequestStr)
        println(responseMessage)
        assertTrue("TEST___ ticket expired",
                responseMessage.contains("replyTicketExpired"));
    }
    @Test
    fun `discarge request-ticket 1 OBSERV mqtt`() {
        try {
            println("connession mqtt:" + mqttConn.connect(mqSender, mqttBrokerAddr))
            println("set call back mqtt:" + mqttConn.setCallback(myMqttCallback))
            println("set subscribe to " + topic + " :" + mqttConn.subscribe(topic))
        } catch (e: Exception) {
            println(e)
        }
        //mandiamo la request per avere il ticket
        val ticketRequestStr = CommUtils.buildRequest("tester", "storerequest", "storerequest(35)", "coldstorageservice").toString()
        val ticketresponseMessage = conn.request(ticketRequestStr)
        assertTrue("TEST___ il ticket accettato ",
                ticketresponseMessage.contains("ticketAccepted"));
        //mando una richiesta di discarge food
        val truckRequestStr = CommUtils.buildRequest("tester", "dischargefood", "dischargefood(1)", "coldstorageservice").toString()
        val responseMessage = conn.request(truckRequestStr)
        assertTrue("TEST___ charge taken",
                responseMessage.contains("replyChargeTaken"));
        Thread.sleep(500)
        val alarm = CommUtils.buildDispatch("tester", "mocksonardata", "mocksonardata(1)","mockalarmdevice").toString();
        conn.forward(alarm)
        Thread.sleep(3000)
        val resume = CommUtils.buildDispatch("tester", "mocksonardata", "mocksonardata(100)","mockalarmdevice").toString();
        conn.forward(resume)
        Thread.sleep(500)
        val alarm2 = CommUtils.buildDispatch("tester", "mocksonardata", "mocksonardata(1)","mockalarmdevice").toString();
        conn.forward(alarm2)

        //verifico gli stati osservati
        val messagesMQTT = myMqttCallback.getMessagesMQTT();
        print( myMqttCallback.getMessagesMQTT().toString())
        val good="[trolley_state(goingIndoor), trolley_state(atIndoor), trolley_state(goingColdroom), trolley_state(stopped), trolley_state(goingColdroom)]"
        assertTrue("TEST___ transport_state",
                messagesMQTT.toString()== good
        )
    }
    class MessageListener :MqttCallback{
        private var messages : ArrayList<String> = ArrayList();
        override fun connectionLost(cause: Throwable?) {
            println("connection lost"+cause.toString())
        }
        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            TODO("Not yet implemented")
        }
        override fun messageArrived(topic: String?, message: MqttMessage?) {
            if (message != null) {
                messages.add(CommUtils.getContent(message.toString()))
            }
        }
        fun getMessagesMQTT(): ArrayList<String> {
            return messages;
        }

    }
}

