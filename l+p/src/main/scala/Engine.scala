import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {
	val props = new GatlingPropertiesBuilder()
		.dataDirectory(System.getProperty("dataDirectory"))
		.resultsDirectory(System.getProperty("resultsDirectory"))
		.bodiesDirectory(System.getProperty("bodiesDirectory"))
		.binariesDirectory(System.getProperty("binariesDirectory"))
	Gatling.fromMap(props.build)
	sys.exit()
}
