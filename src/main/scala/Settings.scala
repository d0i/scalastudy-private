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

  def parseArgsR(args: List[java.lang.String]): Settings = {
    args match {
      case "-help" :: Nil => {
	this.printHelp()
	System.exit(0)
      }
      case "-strict" :: other => {
	this += ("grammarMode" -> "strict")
	return this.parseArgsR(other)
      }

      case schemaname :: filename :: Nil => {
	this += ("schemaname" -> schemaname)
	this += ("filename" -> filename)
      }

      case filename :: Nil => this += ("filename" -> filename)

      case _ => {
        println("please specify XML filename to encode.")
	this.printHelp()
	System.exit(1)
      }
    }

    this
  }
}

object Settings {
  def parseArgs(args: List[java.lang.String]): Settings = {
    var setting = new Settings()
    setting.parseArgsR(args)
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
