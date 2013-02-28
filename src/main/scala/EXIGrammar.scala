import java.io.FileInputStream
import java.io.FileOutputStream
import org.openexi.sax.Transmogrifier
import org.openexi.proc.common.GrammarOptions
import org.openexi.proc.common.AlignmentType
import org.openexi.proc.grammars.GrammarCache
import org.openexi.schema.EXISchema
import org.openexi.scomp.EXISchemaFactory
import org.xml.sax.InputSource

object EXIGrammar {
  def mkEXIOptions(setting: Settings) = {
    setting.printConfig()
    setting('grammarMode) match {
      case "strict" => GrammarOptions.STRICT_OPTIONS
      case _ => GrammarOptions.DEFAULT_OPTIONS
    }
  }
  def mkOutputStream(setting: Settings): FileOutputStream = {
    val outfn = setting('filename).replace(".xml", ".exi")
    return new java.io.FileOutputStream(outfn)
  }
  def mkSchema(settings: Settings): EXISchema = {
    if (settings.contains('schemaname)){
      val schema_source = new InputSource(settings('schemaname))
      val schema_factory = new EXISchemaFactory()
      return schema_factory.compile(schema_source)
    }
    null
  }
  def mkGrammarCache(settings: Settings) = {
    // FIXME: schema setting not implemented
    val options = mkEXIOptions(settings)
    val schema = mkSchema(settings)
    new GrammarCache(schema, options)
  }
  def mkTransmogrifier(settings: Settings): org.openexi.sax.Transmogrifier = {
    val transmogrifier = new org.openexi.sax.Transmogrifier()
    if (settings('encodeMode) == "byte"){
      transmogrifier.setAlignmentType(AlignmentType.byteAligned)
    }
    val f_out = mkOutputStream(settings)
    val gcache = mkGrammarCache(settings)
    transmogrifier.setGrammarCache(gcache)
    transmogrifier.setOutputStream(f_out)
    transmogrifier
  }

  def mkInputSource(settings: Settings): org.xml.sax.InputSource = {
    new InputSource(new FileInputStream(settings('filename)))
  }
}
