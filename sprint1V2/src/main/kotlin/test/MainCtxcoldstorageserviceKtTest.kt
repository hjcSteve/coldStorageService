package test.it.unibo.ctxcoldstorageservice

import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import unibo.basicomm23.utils.CommUtils
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Socket
import unibo.basicomm23.tcp.TcpClientSupport

class MainCtxcoldstorageserviceKtTest {

    val port=8055;
    companion object {
        @JvmStatic
        @BeforeClass
        fun setUp(): Unit {
            println("set up");
        }
        val conn = TcpClientSupport.connect("localhost",8055,5)
        val robotconn = TcpClientSupport.connect("localhost",8090,5)
    }

    @Test
    fun ` test store request`(){
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "storerequest", "storerequest(35)", "coldstorageservice").toString()
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



}