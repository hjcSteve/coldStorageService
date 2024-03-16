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

class MainRobotTest {

    val port=8090;
    companion object {
        @JvmStatic
        @BeforeClass
        fun setUp(): Unit {
            println("set up");
        }
        val conn = TcpClientSupport.connect("localhost",8020,5)
    }

    @Test
    fun ` test virtualrobot`(){
        //mandiamo la request
        val step = CommUtils.buildRequest("tester", "engage", "engage(transporttrolley,300)", "basicrobot").toString()
        println(step);
        val responseMessage = conn.request(step)
        println(responseMessage)
    }
    @Test
    fun ` test move`(){
        //mandiamo la request
        val step = CommUtils.buildRequest("tester", "moverobot", " moverobot(0,4)", "basicrobot").toString()
        println(step);
        val responseMessage = conn.request(step)
        println(responseMessage)

    }
    @Test
    fun ` test move home`(){
        //mandiamo la request
        val step = CommUtils.buildRequest("tester", "moverobot", " moverobot(0,0)", "basicrobot").toString()
        println(step);
        val responseMessage = conn.request(step)
        println(responseMessage)

    }



}