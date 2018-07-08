# WeChat In

A basic WeChat message inbound endpoint using SpringBoot 2.

```
curl -s -X POST http://localhost:15001/wechat -H 'Content-Type: text/xml' -d '<xml>
	<ToUserName><![CDATA[toUser]]></ToUserName>
	<FromUserName><![CDATA[oWyt_1v_Knw6D0hQwGen1fgd4634]]></FromUserName>
	<CreateTime>1348831860</CreateTime>
	<MsgType><![CDATA[text]]></MsgType>
	<Content><![CDATA[I like WeChat!!!]]></Content>
	<MsgId>1234567890123456</MsgId></xml>' \
	| xmlstarlet fo
```

