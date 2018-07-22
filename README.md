# WeChat In

A WeChat development helper application based on SpringBoot 2.
- message inbound endpoint (check for URL and token)
- mock server with recording
- encryption checks

[Swagger Documentation](http://localhost:15001/swagger-ui.html)

Send a message to the message webhook:
```
curl -s -X POST http://localhost:15001/wechat -H 'Content-Type: text/xml' -d '<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[oJ_mT0idqDSmsWctBfhJ4gLSUX1w]]></FromUserName>
	<CreateTime>1348831860</CreateTime>
	<MsgType><![CDATA[text]]></MsgType>
	<Content><![CDATA[I like WeChat!!!]]></Content>
	<MsgId>1234567890123456</MsgId></xml>' \
	| xmlstarlet fo
```

Encrypt a message:
```
curl -s -X POST "http://localhost:15001/util/encrypt?token=1234&appId=wxf1569d816b304d28&aesKey=WO3Gcs2X5lcNtZagAcaRRjNaAwEmoekP1P2aOKR4W3D" \
    -H 'Content-Type: text/xml' -d '<xml>
    <ToUserName><![CDATA[toUser]]></ToUserName>
    <FromUserName><![CDATA[oJ_mT0idqDSmsWctBfhJ4gLSUX1w]]></FromUserName>
    <CreateTime>1348831860</CreateTime>
    <MsgType><![CDATA[text]]></MsgType>
    <Content><![CDATA[I like WeChat!!!]]></Content>
    <MsgId>1234567890123456</MsgId></xml>' \
    | xmlstarlet fo
```

Send a text message to mock server
```
curl -v "http://localhost:15001/cgi-bin/message/custom/send?access_token=dfgdfgsdgfs%20gsfdgsdfg%20sdfg" \
    -H "Content-Type: application/json" \
    -d '{"touser":"string","msgtype":"text","text":{"content":"LOGSESSION-54654564"}}'
```
