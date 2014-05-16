package util

class PimpedSeq[A](s: Seq[A]) {

  /**
   * Group elements of the sequence that have consecutive keys that are equal.
   *
   * Use case:
   *     val lst = SQL("SELECT * FROM a LEFT JOIN b ORDER BY a.key")
   *     val grp = lst.groupConsecutiveKeys(a.getKey)
   */
  def groupConsecutiveKeys[K](f: (A) => K): Seq[(K, List[A])] = {
    this.s.foldRight(List[(K, List[A])]())((item: A, res: List[(K, List[A])]) =>
      res match {
        case Nil => List((f(item), List(item)))
        case (k, kLst) :: tail if k == f(item) => (k, item :: kLst) :: tail
        case _ => (f(item), List(item)) :: res
      })
  }
}

object PimpedSeq {
  implicit def seq2PimpedSeq[A](s: Seq[A]) = new PimpedSeq(s)
}