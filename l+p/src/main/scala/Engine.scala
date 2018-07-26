package com.net128.test.gatling.wechatin

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {

	System.out.println(System.getProperty("simulationClass"));
	val props = new GatlingPropertiesBuilder()
		.dataDirectory("jar")
		.simulationClass(System.getProperty("simulationClass"))
	//	.simulationClass(System.getProperty("gatling.core.simulationClass"))
	//	.dataDirectory(System.getProperty("dataFolder"))
	//	.resultsDirectory(System.getProperty("resultsFolder"))
	//	.resultsDirectory(IDEPathHelper.resultsDirectory.toString)
	//	.bodiesDirectory(IDEPathHelper.bodiesDirectory.toString)
	//	.binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)

	Gatling.fromMap(props.build)
	sys.exit()
}
