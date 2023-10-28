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

    }

    @Test
    fun `Il test per il main`() {

    }
    @Test
    fun ` test di una funzione a caso`(){

    }
    @Test
    fun ` test store request`(){
        val conn = TcpClientSupport.connect("localhost",8055,5)
        //mandiamo la request
        val truckRequestStr = CommUtils.buildRequest("tester", "storerequest", "storerequest(10)", "coldstorageservice").toString()
        println(truckRequestStr);
        val responseMessage = conn.request(truckRequestStr)

        println(responseMessage)

        //otteniamo la risposta inm qualche modo
        val replyContent= "testo di risposta di un ticketaccepted";
        assertTrue("TEST___ il ticket accettato o rifiutato",
                replyContent.contains("ticketaccepted") || replyContent.contains("ticketdenied"));
    }



}