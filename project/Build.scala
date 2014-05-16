import sbt._
import Keys._
import play.Project._
 
object ApplicationBuild extends Build {
	val appName         = "securesocial-slick-bootstrap3-sample"
	val appVersion      = "0.1-SNAPSHOT"
	val appDependencies = Seq(
		jdbc
		,"com.typesafe.play" %% "play" % "2.2.1" withSources
		,"org.webjars" %% "webjars-play" % "2.2.0"
		,"org.webjars" % "bootstrap" % "3.1.1"
		,"org.scalaj" %% "scalaj-http" % "0.3.14"
		,"net.sf.jtidy" % "jtidy" % "r938"
		,"com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2"
		,"com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"
		,"org.jdom" % "jdom2" % "2.0.5"
		,"jaxen" % "jaxen" % "1.1.6" 
		,"org.fluentlenium" % "fluentlenium-core" % "0.9.2" 
		,"ws.securesocial" %% "securesocial" % "2.1.3"
		,"org.squeryl" %% "squeryl" % "0.9.6-RC2"
		,"com.netflix.rxjava" % "rxjava-scala" % "0.15.0"
		,"org.scalatest" %% "scalatest" % "2.0" //% "test"
		,"junit" % "junit" % "4.10" % "test"
		,"org.json4s" % "json4s-native_2.10" % "3.2.5"
		,"net.databinder.dispatch" % "dispatch-core_2.10" % "0.11.0"
		,"com.squareup.retrofit" % "retrofit" % "1.0.0"
		,"org.scala-lang" % "scala-swing" % "2.10.3"
		,"org.scala-lang" % "scala-reflect" % "2.10.3"
		,"org.scala-lang.modules" %% "scala-async" % "0.9.0-M2"
		//slick for play
		,"com.typesafe.play" %% "play-slick" % "0.6.0.1" 
		//slick joda time
		//,"com.typesafe.slick" %% "slick" % "2.0.0"
		,"joda-time" % "joda-time" % "2.3"
		,"org.joda" % "joda-convert" % "1.5"
		,"com.github.tototoshi" %% "slick-joda-mapper" % "1.0.1"
		
		//selenium
		,"org.seleniumhq.selenium" % "selenium-java" % "2.35.0"
	)
	
	val main = play.Project(appName, appVersion, appDependencies)
		.settings(play.Project.playScalaSettings:_*)
		.settings(
			resolvers ++= Seq(
			    Resolver.url("maven central",url("http://central.maven.org/maven2"))
				,Resolver.url("Objectify Play Repository (release)", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns)
				,Resolver.url("Objectify Play Repository (snapshot)", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
				,Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns)
				,Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns)
				,Resolver.url("play-authenticate (release)", url("http://joscha.github.com/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns)
				,Resolver.url("play-authenticate (snapshot)", url("http://joscha.github.com/play-authenticate/repo/snapshots/"))(Resolver.ivyStylePatterns)
				,Resolver.sonatypeRepo("releases")
				,Resolver.sonatypeRepo("snapshots")
			)
		)
}
