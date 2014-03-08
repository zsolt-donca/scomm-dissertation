name := "scomm"

version := "1.0"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2")

checksums in update := Nil

resolvers += "Maven central" at "http://repo1.maven.org/maven2/"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.3"

libraryDependencies += "scala" % "scala-react_2.10" % "1.1-SNAPSHOT"

libraryDependencies += "junit" % "junit" % "4.11"

libraryDependencies += "org.easytesting" % "fest-swing" % "1.2.1"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions += "-P:continuations:enable"
