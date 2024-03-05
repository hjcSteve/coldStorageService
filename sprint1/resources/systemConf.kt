import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileReader


object systemConf {

    private var Expirationtime : Int = 0;
    private var Maxweight : Int = 0;


    init {
        try {
            val parser = JSONParser()

                val obj = parser.parse(FileReader("systemConf.json"))
            val jsonObject = obj as JSONObject
            this.Expirationtime = jsonObject["expiration"] as Int
            this.Maxweight = jsonObject["maxweight"] as Int


        } catch (e : Exception) {
            println(" ${this.javaClass.name}  | ${e.localizedMessage}, activate simulation by default")
        }
    }

    fun getExpirationTime() : Int {
        return Expirationtime;
    }

    fun getMaxWeight() : Int {
        return Maxweight;
    }

}
