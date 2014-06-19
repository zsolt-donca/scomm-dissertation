package edu.zsd.cpsdemo

object TraitExample {

  trait Named {
    def name: String
  }

  class Animal

  trait FourLegged

  trait Mammal

  class Cat(nickname: String) extends Animal with FourLegged with Mammal with Named {
    override def name: String = nickname
  }

  trait TwoNames extends Named {
    def firstName: String

    def lastName: String

    def fullName: String = {
      s"$firstName $lastName"
    }

    def lexicalFullName: String = {
      s"$lastName, $firstName"
    }

    override def name = fullName
  }

  trait Student {
    var year: Int
  }

  trait FacultyMember {
    def faculty: String
  }

  class Person(_name: String) extends Named {
    def name = _name
  }

  //  class Student extends Named with FacultyStudent {
  //    override def name: String = ???
  //    override def faculty: String = ???
  //  }

  def printName(o: Named) {
    println("name is: " + o.name)
  }

  val bobby: Named = new Cat("Bobby")
  printName(bobby)
}
