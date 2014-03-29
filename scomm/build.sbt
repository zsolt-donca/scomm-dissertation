name := "scomm"

version := "1.0"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.2")

checksums in update := Nil

resolvers += "Maven central" at "http://repo1.maven.org/maven2/"

resolvers += "milestone.repo.springsource.org" at "https://repo.springsource.org/libs-milestone"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.10.3"

libraryDependencies += "scala" % "scala-react_2.10" % "1.1-SNAPSHOT"

libraryDependencies += "junit" % "junit" % "4.11"

libraryDependencies += "org.testng" % "testng" % "6.8.8"

libraryDependencies += "org.easytesting" % "fest-swing" % "1.2.1"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test"

libraryDependencies += "org.springframework" % "spring-test" % "3.2.8.RELEASE"

libraryDependencies += "org.springframework.scala" % "spring-scala" % "1.0.0.M2"

libraryDependencies += "org.aspectj" % "aspectjweaver" % "1.7.4"

libraryDependencies += "org.aspectj" % "aspectjrt"     % "1.7.4"

libraryDependencies += "net.java.openjdk.cacio" % "cacio" % "1.3"

libraryDependencies += "net.java.openjdk.cacio" % "cacio-tta" % "1.3"

libraryDependencies += "com.github.nscala-time" % "nscala-time_2.10" % "0.8.0"

autoCompilerPlugins := true

addCompilerPlugin("org.scala-lang.plugins" % "continuations" % "2.10.2")

scalacOptions += "-P:continuations:enable"
