package com.net128.test.gatling.wechatin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class MessageOutboundSimulation extends Simulation {

  val nUsers = Integer.getInteger("users", 100)
  var logSession:String = "LOGSESSION-" + System.currentTimeMillis()
  val uri = sys.props.getOrElse("uri", "http://localhost:15001/cgi-bin")
  val httpConf = http
    .disableWarmUp
    .baseURLs(uri)
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Java/1.8.0_131")
    .contentTypeHeader(HttpHeaderValues.ApplicationJson)

  val scn = scenario("Message Outbound Simulation")
  //  .feed(tsv("text.csv").circular)
    .exec(http("token")
      .get("/token")
        .queryParam("appid", "wxf1569d816b304d28")
        .queryParam("grant_type", "client_credential")
        .queryParam("secret", "fz5c1035a4ef79f58d478512fda2b415")
        .check(
          jsonPath("$.access_token").saveAs("currentAccessToken")
        )
        .silent
      .check(status.is(200))
    )
    .pause(1)
    .repeat(50) {
      pause(300 milliseconds)
      .exec(http("send custom message")
        .post("/message/custom/send")
        .queryParam("access_token", "${currentAccessToken}")
        .body(StringBody(
          s"""{
            |"touser":"string","msgtype":"text",
            |"text":{"content":"${logSession} 決口経算再市成誉給等 بعد اتفاق الشهيرة سنغافورة لم. על תנך התפתחות ליצירתה, קסאםFacete integre prodesset eum ex, mea"}
            |}""".stripMargin)
        )
        .check(status.is(200))
      )
    }

  setUp(
    scn.inject(
      atOnceUsers(nUsers)
    ).protocols(httpConf))
}
