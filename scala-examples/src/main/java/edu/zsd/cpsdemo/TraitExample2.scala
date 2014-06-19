package edu.zsd.cpsdemo

object TraitExample2 extends App {

  class Animal {
    def description: String = "animal"
  }

  trait FourLegged extends Animal {
    override def description: String = "four-legged " + super.description
  }

  trait Furry extends Animal {
    override def description: String = "furry " + super.description
  }

  trait Carnivorous extends Animal {
    override def description: String = "carnivorous " + super.description
  }

  trait Feline extends FourLegged with Furry with Carnivorous {
    override def description: String = "feline, that is, a " + super.description
  }

  trait Domesticated {
    self: Animal =>

    override def description: String = "domesticated " + self.description
  }

  class Cat extends Feline with Domesticated {
    override def description: String = "I am a cat, a " + super.description
  }

  class Plant {
  }

  //  class PottedPlant extends Plant with Feline {
  //  }
  //
  println(new Cat().description)
  // prints: I am a cat, a domesticated carnivorous furry four-legged animal
}
