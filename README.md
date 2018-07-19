# WeChat In

A basic WeChat message inbound endpoint using SpringBoot 2.

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

