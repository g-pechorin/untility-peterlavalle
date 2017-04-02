package peterlavalle.untility

class Feedback(prefix: String) {
  def out(line: String): Unit = System.out.println(s"$prefix > $line")

  def err(line: String): Unit = System.err.println(s"$prefix ! $line")
}
