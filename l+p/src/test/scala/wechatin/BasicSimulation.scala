package wechatin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:15001/cgi-bin") // Here is the root for all relative URLs
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Java/1.8.0_131")
    .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)

//  val scn = scenario("Scenario Name")
//    .exec(http("token")
//      .get("/token)
//        .queryParam("appid", "wxf1569d816b304d28")
//        .queryParam("grant_type", "client_credential")
//        .queryParam("secret", "fz5c1035a4ef79f58d478512fda2b415")
//      )
//    )
//    .pause(7)
//    .pause(629 milliseconds)
//    .exec(http("send custom")
//      .post("/message/custom/send")
//      .queryParam("access_token", "dfgdfgsdgfsjztngsfdgsdfgggfsfsdfg")
//      .body(StringBody("""{"touser":"string","msgtype":"text","text":{"content":"LOGSESSION-54654564" }}"""))
//    )

  val scn = scenario("Scenario Name")
    .exec(http("token")
      .get("/token")
        .queryParam("appid", "wxf1569d816b304d28")
        .queryParam("grant_type", "client_credential")
        .queryParam("secret", "fz5c1035a4ef79f58d478512fda2b415")
        .check(
          jsonPath("$.access_token").saveAs("myaccesstoken")
        )
      .check(status.is(200))
    )
    .pause(1629 milliseconds)
    .pause(1)
    .exec(http("send custom")
      .post("/message/custom/send")
      .queryParam("access_token", "${myaccesstoken}")
      .body(StringBody(
        """{
          |"touser":"string","msgtype":"text",
          |"text":{"content":"LOGSESSION-54654564" }
          |}""".stripMargin)
      )
      .check(status.is(200))
    )
//    .exec(session => {
//      println(session.get("myaccesstoken"))
//      session
//    })

  setUp(
    scn.inject(
      atOnceUsers(1)
    ).protocols(httpConf))
}
