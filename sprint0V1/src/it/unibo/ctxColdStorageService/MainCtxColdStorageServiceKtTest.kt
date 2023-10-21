package it.unibo.ctxColdStorageService

import org.junit.*
import org.junit.Assert.*
import org.junit.jupiter.api.DisplayName
import java.net.Socket

class MainCtxColdStorageServiceKtTest {
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
        //mandiamo la request

        //otteniamo la risposta inm qualche modo
        val replyContent= "testo di risposta di un ticketaccepted";
        assertTrue("TEST___ il ticket accettato o rifiutato",
                replyContent.contains("ticketaccepted") || replyContent.contains("ticketdenied"));
    }


}