package util.io

import java.nio.file.Paths
import java.io.File
import java.nio.file.Path
import org.apache.commons.io.FileUtils
import java.io.FileOutputStream
import java.io.OutputStream
import org.apache.commons.io.FilenameUtils
import java.io.InputStream
import scala.io.BufferedSource
import java.io.FileInputStream
import java.io.ByteArrayInputStream
import java.io.OutputStreamWriter
import java.io.Writer
import scala.io.Source
import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.PrintWriter
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import Constants._
import java.util.regex.Pattern

private object Constants{
  val FILE_SEPARATOR = File.separator
}

trait BaseLocation {
  def extension: String = FilenameUtils.getExtension(absolute)
  def name: String = FilenameUtils.getName(absolute)
  def baseName: String = FilenameUtils.getBaseName(absolute)
  def parentName: String = toFile.getParentFile.getAbsolutePath
  /**To read data you should read the inputstream*/
  def toFile: File
  def toPath: Path
  def toInputStream: InputStream
  def toPath(subFile: String): Path = toPath.resolve(subFile)
  def toSource: BufferedSource = scala.io.Source.fromInputStream(toInputStream)
  def absolute: String = toPath("").toAbsolutePath.toString
  def extractAncestor(ancestor: OutputLocation): Try[Seq[String]] = diff(absolute, ancestor.absolute).map { _.split(Pattern.quote(FILE_SEPARATOR)) }
  private def diff(text: String, prefix: String) = if (text.startsWith(prefix)) Success(text.substring(prefix.length)) else Failure(new RuntimeException(s"Text [$text] doesn't start with [$prefix]."))
  def mkdirIfNecessary = {
    FileUtils.forceMkdir(toFile)
  }
  def traverse = new FileVisitor.TraversePath(toPath)
  def traverseWithDir = new FileVisitor.TraversePath(toPath,true)
  protected def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try f(resource) finally resource.close()
    
  def hasDirs = RichPath.wrapPath(toPath).list.find(_.toFile.isDirectory).nonEmpty
}
trait InputLocation extends BaseLocation {
  def toInputStream: InputStream = new FileInputStream(absolute)
  def child(child: String): InputLocation
  def parent: InputLocation
  def bytes:Array[Byte] = org.apache.commons.io.FileUtils.readFileToByteArray(toFile)
  def usingInputStream(op: InputStream => Unit): Unit =
    using(toInputStream)(inputStream => op(inputStream))
}
trait OutputLocation extends BaseLocation {
  def toOutputStream: OutputStream = new FileOutputStream(absolute)
  def toWriter: Writer = new BufferedWriter(new OutputStreamWriter(toOutputStream, "UTF-8"))
  def toPrintWriter: PrintWriter = new PrintWriter(new OutputStreamWriter(toOutputStream, StandardCharsets.UTF_8), true)
  def child(child: String): OutputLocation
  //def descendant(childs: String*): OutputLocation = descendant(childs.toSeq)
  def descendant(childs: Seq[String]): OutputLocation = if (childs.isEmpty) this else child(childs.head).descendant(childs.tail)
  def parent: OutputLocation
  def rename(renamer: String => String) = {
    val newName = renamer(baseName)
    if (newName == baseName) {
      //println(s"ignore [${absolute}] to [${absolute}]")
    } else {
      val dest = parent.child(withExtension2(newName, extension))
      println(s"move [${absolute}] to [${dest.absolute}]")
      FileUtils.moveFile(toFile, dest.toFile)
    }
  }
  def moveTo(dest:OutputLocation) = {
      FileUtils.moveFile(toFile, dest.toFile)
  }
  def withName(baseNameSupplier: String=>String) = parent.child(withExtension2(baseNameSupplier(baseName),extension))
  def withExtension(extensionSupplier: String=>String) = parent.child(withExtension2(baseName,extensionSupplier(extension)))
  private def withExtension2(name: String, ext: String) =
    if (ext.length > 0)
      name + "." + ext
    else name
  def usingOutputStream(op: OutputStream => Unit): Unit =
    using(toOutputStream)(outputStream => op(outputStream))
  def usingPrintWriter(op: PrintWriter => Unit): Unit =
    using(toPrintWriter)(printWriter => op(printWriter))
}

case class FileLocation(val fileFullPath: String) extends InputLocation with OutputLocation {
  def toFile: File = new File(fileFullPath)
  def toPath: Path = Paths.get(fileFullPath)
  override def toInputStream: InputStream = new FileInputStream(toFile)
  def child(child: String): FileLocation = new FileLocation(fileFullPath + FILE_SEPARATOR + child)
  def parent: FileLocation = new FileLocation(parentName)
}
case class MemoryLocation(val memoryName: String) extends InputLocation with OutputLocation {
  //val buffer: Array[Byte] = Array()
  lazy val outStream = new ByteArrayOutputStream()
  def toFile: File = ???
  def toPath: Path = ???
  override def toOutputStream: OutputStream = outStream
  override def toInputStream: InputStream = new ByteArrayInputStream(outStream.toByteArray())
  def child(child: String): FileLocation = ???
  def parent: FileLocation = ???
}
case class ClassPathInputLocation(val resourcePath: String) extends InputLocation {
  def toFile: File = new File(getClass.getResource(resourcePath).getFile)
  def toPath: Path = toFile.toPath
  override def toInputStream: InputStream = getClass.getResourceAsStream(resourcePath)
  def child(child: String): ClassPathInputLocation = new ClassPathInputLocation(resourcePath + FILE_SEPARATOR + child)
  def parent: ClassPathInputLocation = new ClassPathInputLocation(parentName)
}

object Locations {
  def classpath(resourcePath: String): ClassPathInputLocation =
    new ClassPathInputLocation(resourcePath)
  def file(fileFullPath: String): FileLocation =
    new FileLocation(fileFullPath)
  def file(path: Path): FileLocation =
    file(path.toFile)
  def file(file: File): FileLocation =
    new FileLocation(file.getAbsolutePath())
  def file(file: File, subFile: String): FileLocation =
    new FileLocation(file.getAbsolutePath()).child(subFile)
  def memory(memoryName: String): MemoryLocation =
    new MemoryLocation(memoryName)
  //io.Source.fromInputStream(getClass.getResourceAsStream(source))
}