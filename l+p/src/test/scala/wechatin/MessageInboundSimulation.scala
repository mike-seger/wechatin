package wechatin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class MessageInboundSimulation extends Simulation {

  val uri = System.getProperty("uri")
  val httpConf = http
    .disableWarmUp
    .baseURLs(uri)
    .acceptHeader("application/json;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Java/1.8.0_131")
    .contentTypeHeader(HttpHeaderValues.TextXml)

  val scn = scenario("Message Inbound Simulation")
    .repeat(50) {
      pause(300 milliseconds)
      .exec(http("receive custom message")
        .post("/wechat")
        .body(StringBody(
          s"""<xml>
             |    <ToUserName>toUser</ToUserName>
             |    <FromUserName>oJ_mT0idqDSmsWctBfhJ4gLSUX1w</FromUserName>
             |    <CreateTime>1348831860</CreateTime>
             |    <MsgType>text</MsgType>
             |    <Content>I like WeChat!!!</Content>
             |    <MsgId>1234567890123456</MsgId>
             |</xml>""".stripMargin)
        )
        .check(status.is(200))
      )
    }

  setUp(
    scn.inject(
      atOnceUsers(3000)
    ).protocols(httpConf))
}
