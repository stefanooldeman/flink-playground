resolvers in ThisBuild ++= Seq("Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal)

name := "Flink Project"

version := "0.1-SNAPSHOT"

organization := "org.example"

scalaVersion in ThisBuild := "2.11.8"

val flinkVersion = "1.2.0"
val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided")

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )


/** test **/
resolvers += "Bintray" at "https://dl.bintray.com/ottogroup/maven"
libraryDependencies += "org.flinkspector" %% "flinkspector-datastream" % "0.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

mainClass in assembly := Some("com.kpn.datalab.flink.playground.Job")

// make run command include the provided dependencies
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
