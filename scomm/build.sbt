name := "scomm"

version := "1.0"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2")

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.3"

libraryDependencies += "scala" % "scala-react_2.10" % "1.1-SNAPSHOT"

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions += "-P:continuations:enable"
