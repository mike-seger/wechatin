# WeChat In

A WeChat development helper application based on SpringBoot 2.
- message inbound endpoint (check for URL and token)
- mock server with recording
- encryption checks

[Swagger Documentation](http://localhost:15001/swagger-ui.html)

## Examples
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
curl -s "http://localhost:15001/util/encrypt?aesKey=WO3Gcs2X5lcNtZagAcaRRjNaAwEmoekP1P2aOKR4W3D&appId=wxf1569d816b304d28&token=weixin" \
    -H "Content-Type: text/xml" -d \
    '<?xml version="1.0" encoding="UTF-8"?>
    <Message>
        <Content>string</Content>
        <CreateTime>0</CreateTime>
        <FromUserName>string</FromUserName>
        <MsgId>0</MsgId>
        <MsgType>text</MsgType>
        <ToUserName>string</ToUserName>
    </Message>' \
    | xmlstarlet fo
```

Decrypt a message:
```
curl -s "http://localhost:15001/util/decrypt?aesKey=WO3Gcs2X5lcNtZagAcaRRjNaAwEmoekP1P2aOKR4W3D&appId=wxf1569d816b304d28&token=weixin" \
    -H "Content-Type: text/xml" -d \
    '<xml>
       <Encrypt>JpJ2oXjm/J9BjR/PhE9jUtOM10ue4Fh55hZpHLQpWnfdrU//p1jSV91tXicXXveun8c9SdGR5iXeGYimUrh4SyHHaawTgX1PWxVOS/Jj0E63w1CQ4/chWWvb0xsl5WA0cUpyS/uX6lBgLXQmJJGNbMmHD4mdj9U23fp5P9DfMpZqlA/kAUYVjYiPRTL+yMcAdBzGTzxPuAWTlBgj0/KL9svLrDK6m66EoS6ISazvrS8t8VhsnyvNZ0ieVrIOxOWiBJEmtNsd8gODllMJ5TmMUrNCCWzZbeSqii3nTDB3xVo=</Encrypt>
       <MsgSignature>a5bf1358d6bf843b0e5621508c90777e74a73af7</MsgSignature>
       <TimeStamp>1532229955678</TimeStamp>
       <Nonce>850968506</Nonce>
     </xml>' \
    | xmlstarlet fo
```

Send a text message to mock server
```
curl -v "http://localhost:15001/cgi-bin/message/custom/send?access_token=dfgdfgsdgfs%20gsfdgsdfg%20sdfg" \
    -H "Content-Type: application/json" \
    -d '{"touser":"string","msgtype":"text",
    "text":{"content":"LOGSESSION-54654564"}}'
```
