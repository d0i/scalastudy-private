// EXI decoder using OpenEXI

import java.io.FileInputStream
import java.io.FileOutputStream
import org.openexi.sax.Transmogrifier
import org.openexi.proc.common.GrammarOptions
import org.openexi.proc.common.AlignmentType
import org.openexi.proc.grammars.GrammarCache
import org.xml.sax.InputSource

import org.openexi.schema.EXISchema
import org.openexi.scomp.EXISchemaFactory
import org.openexi.sax.EXIReader
import org.openexi.proc.io._

import javax.xml.parsers.SAXParserFactory
import javax.xml.transform.sax.SAXTransformerFactory
import javax.xml.transform.sax.TransformerHandler
import javax.xml.transform.stream.StreamResult

object Decode {
  def mkTransformerHandler(): TransformerHandler = {
    val sTF = SAXTransformerFactory.newInstance()
    // FIXME I'm not sure it's effective or not... am I missing something?
    val sPF = SAXParserFactory.newInstance() 
    sPF.setNamespaceAware()
    sTF.newTransformerHandler()
  }
  def mkInputStream(setting: Settings): java.io.FileInputStream = {
    new FileInputStream(setting('filename))
  }
  def decode(setting: Settings): String = {
    val tHandler = mkTransformerHandler()
    val eReader = new EXIReader()
    if (setting('encodeMode) == "byte"){
      eReader.setAlignmentType(AlignmentType.byteAligned)
    } // by default it's bit-packed
    val gCache = EXIGrammar.mkGrammarCache(setting)
    eReader.setGrammarCache(gcache)
    val writer = new StringWriter()
    tHandler.setResult(new StreamResult(writer))
    eReader.setContentHandler(tHandler)
    
    eReader.parse(mkInputStream())
    writer.getBuffer().toString()
  }
  def main(args: Array[java.lang.String]){
    val settings = Settings.parseArgs(args.toList)
    val s = decode(settings)
    println(s)
  }
}
