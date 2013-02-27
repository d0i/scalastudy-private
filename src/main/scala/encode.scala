// EXI encoder using OpenEXI

import java.io.FileInputStream
import java.io.FileOutputStream
import org.openexi.sax.Transmogrifier
import org.openexi.proc.common.GrammarOptions
import org.xml.sax.InputSource


object Encode {
  def mkEXIOptions(setting: Settings) = {
    setting.printConfig()
    setting("grammarMode") match {
      case "strict" => GrammarOptions.STRICT_OPTIONS
      case _ => GrammarOptions.DEFAULT_OPTIONS
    }
  }
  def mkOutputStream(setting: Settings): FileOutputStream = {
    val outfn = setting("filename").replace(".xml", ".exi")
    return new java.io.FileOutputStream(outfn)
  }
  def mkTransmogrifier(settings: Settings): org.openexi.sax.Transmogrifier = {
    val options = mkEXIOptions(settings)
    // FIXME: it should set byte-align option if setting request it
    val transmogrifier = new org.openexi.sax.Transmogrifier()
    // transmogrifier.setAlignmentType?
    val f_out = mkOutputStream(settings)
    // FIXME: schema setting not implemented
    val schema = null
    val gcache = new org.openexi.proc.grammars.GrammarCache(schema, options)
    //transmogrifier.setEXISchema(gcache) // why it does not compile good?
    transmogrifier.setOutputStream(f_out)
    transmogrifier
  }

  def mkInputSource(settings: Settings): org.xml.sax.InputSource = {
    new InputSource(new FileInputStream(settings("filename")))
  }

  def main(args: Array[java.lang.String]) {
    val settings = Settings.parseArgs(args.toList)
    val transmogrifier = Encode.mkTransmogrifier(settings) 
    val f_in = Encode.mkInputSource(settings)
    if (transmogrifier != null && f_in != null){
      transmogrifier.encode(f_in)
    } else {
      println("oops, no transmogilifier or input file.")
    }
  }
}
