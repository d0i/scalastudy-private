// EXI encoder using OpenEXI

import java.io.FileInputStream
import java.io.FileOutputStream
import org.openexi.sax.Transmogrifier
import org.openexi.proc.common.GrammarOptions
import org.openexi.proc.common.AlignmentType
import org.openexi.proc.grammars.GrammarCache
import org.xml.sax.InputSource

object Encode {

  def main(args: Array[java.lang.String]) {
    val settings = Settings.parseArgs(args.toList)
    val transmogrifier = EXIGrammar.mkTransmogrifier(settings) 
    val f_in = EXIGrammar.mkInputSource(settings)
    if (transmogrifier != null && f_in != null){
      transmogrifier.encode(f_in)
    } else {
      println("oops, no transmogilifier or input file.")
    }
  }
}
