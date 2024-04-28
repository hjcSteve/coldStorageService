import java.io.File
import com.google.gson.Gson


object Configuration{

    var conf : Conf = Conf()
    init {
        val filePath = "AppConf.json" // Replace with your JSON file path
        val file = File(filePath)

        val jsonString = file.readText()
        val res = Gson().fromJson(jsonString, Conf::class.java)
        conf = res;
    }


}