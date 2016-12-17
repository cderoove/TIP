name := "tip"

scalaVersion := "2.11.8"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies += "org.parboiled" %% "parboiled" % "2.1.3"
libraryDependencies += "com.regblanc" %% "scala-smtlib" % "0.2"
libraryDependencies += "org.scalatest"  %% "scalatest"   % "3.0.1"  % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck"  % "1.12.6" % "test"


scalaSource in Compile := baseDirectory.value / "src" 
scalaSource in Test := baseDirectory.value / "test"


// ~~~~ sbt-start-script (https://github.com/sbt/sbt-start-script)
// Creates an invocation wrapper script `target/start`.  We customize it here to create `target/tip` instead.
// Usage:
// $ sbt start-script
import com.typesafe.sbt.SbtStartScript
Seq(SbtStartScript.startScriptForClassesSettings: _*)
SbtStartScript.StartScriptKeys.startScriptName <<= target / "tip"
