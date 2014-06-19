package edu.zsd.cpsdemo

object DiamondInheritanceTest extends App {


  trait Kocsi {
    def brumbrum() = println("xxxx")
  }

  trait Vizikocsi extends Kocsi {
    override abstract def brumbrum() = println("pfafff");
    super.brumbrum()
  }

  trait Foldikocsi extends Kocsi {
    override abstract def brumbrum() = println("vrömm");
    super.brumbrum()
  }

  class Hibrid extends Foldikocsi {
    override def brumbrum(): Unit = super.brumbrum()
  }

  val h = new Hibrid with Vizikocsi
  h.brumbrum()

}
