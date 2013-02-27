import scala.collection.mutable.HashMap

class Settings extends HashMap[java.lang.String, java.lang.String] {
  this += ("grammarMode" -> "nonstrict")
  def printConfig() {
    this.foreach(kv => println(kv._1+": "+kv._2))
  }
  def printHelp(){
    println("""
[options] [schemaname] [xmlname]
or
[options] [xmlname]

Options are
 -help
 -strict
""")
  }
}
object Settings {
  def parseArgs(args: List[java.lang.String]): Settings = {
    var setting = new Settings()
    parseArgsR(args, setting)
  }
  def parseArgsR(args: List[java.lang.String], setting: Settings): Settings = {
    args match {
      case "-help" :: Nil => {
	setting.printHelp()
	System.exit(0)
      }
      case "-strict" :: other => {
	setting += ("grammarMode" -> "strict")
	return parseArgsR(other, setting)
      }

      case schemaname :: filename :: Nil => {
	setting += ("schemaname" -> schemaname)
	setting += ("filename" -> filename)
      }

      case filename :: Nil => setting += ("filename" -> filename)

      case _ => {
        println("please specify XML filename to encode.")
	setting.printHelp()
	System.exit(1)
      }
    }

    setting
  }

  def main(args: Array[java.lang.String]) {
    println(">>> -strict schemafile xmlfile")
    Settings.parseArgs("-strict"::"schemafile"::"xmlfile"::Nil).printConfig()
    println(">>> -strict xmlfile")
    Settings.parseArgs("-strict"::"xmlfile"::Nil).printConfig()
    println(">>> xmlfile")
    Settings.parseArgs("xmlfile"::Nil).printConfig()
    println(">>> (empty)")
    if (Settings.parseArgs(Nil) != null){
      println("failed to exit.")
    }
  }
}
