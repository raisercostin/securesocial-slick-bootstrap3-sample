package util.io

import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import scala.collection.Traversable
import java.io.File

trait FileVisitor {
  def apply(path: String) = fromNioPath(new File(path).toPath)
  def apply(path: Path) = fromNioPath(path)

  implicit def fromNioPath(path: Path): TraversePath = new TraversePath(path)
  implicit def fromIoFile(file: File): TraversePath = new TraversePath(file.toPath)

  // Make it extend Traversable
  class TraversePath(path: Path, withDir: Boolean = false) extends Traversable[(Path, BasicFileAttributes)] {
    // Make foreach receive a function from Tuple2 to Unit
    override def foreach[U](f: ((Path, BasicFileAttributes)) => U) {
      class Visitor extends SimpleFileVisitor[Path] {
        override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = try {
          // Pass a tuple to f
          f(file -> attrs)
          FileVisitResult.CONTINUE
          //} catch {
          //case _: Throwable => FileVisitResult.TERMINATE
        }
        override def preVisitDirectory(file: Path, attrs: BasicFileAttributes): FileVisitResult = try {
          if (withDir) {
            f(file -> attrs)
          }
          FileVisitResult.CONTINUE
        }
      }
      Files.walkFileTree(path, new Visitor)
    }
  }
  /*
ProjectHome foreach {
  // use case to seamlessly deconstruct the tuple
  case (file, _) => if (!file.toString.contains(".svn")) println(File)
}
*/
}
object FileVisitor extends FileVisitor