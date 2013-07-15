// EXI encoder using OpenEXI

import java.io.FileInputStream
import java.io.FileOutputStream
import org.openexi.sax.Transmogrifier
import org.openexi.proc.common.GrammarOptions
import org.openexi.proc.common.AlignmentType
import org.openexi.proc.grammars.GrammarCache
import org.xml.sax.InputSource

object Encode {

  def mkOutputStream(setting: Settings): FileOutputStream = {
    val outfn = setting('filename).replace(".xml", ".exi")
    return new java.io.FileOutputStream(outfn)
  }
  def mkInputSource(settings: Settings): org.xml.sax.InputSource = {
    new InputSource(new FileInputStream(settings('filename)))
  }

  def mkTransmogrifier(settings: Settings): org.openexi.sax.Transmogrifier = {
    val transmogrifier = new org.openexi.sax.Transmogrifier()
    if (settings('encodeMode) == "byte"){
      transmogrifier.setAlignmentType(AlignmentType.byteAligned)
    }
    val f_out = mkOutputStream(settings)
    val gcache = EXIGrammar.mkGrammarCache(settings)
    transmogrifier.setGrammarCache(gcache)
    transmogrifier.setOutputStream(f_out)
    transmogrifier
  }
  
  def main(args: Array[java.lang.String]) {
    val settings = Settings.parseArgs(args.toList)
    val transmogrifier = mkTransmogrifier(settings) 
    val f_in = mkInputSource(settings)
    if (transmogrifier != null && f_in != null){
      transmogrifier.encode(f_in)
    } else {
      println("oops, no transmogilifier or input file.")
    }
  }
}
