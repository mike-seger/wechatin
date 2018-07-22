package com.net128.app.wechatin.domain.message;

import com.net128.app.wechatin.domain.WeChatType;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InternalMessage implements WeChatType {
    @ApiModelProperty(hidden = true)
    public Long id;
    @ApiModelProperty(hidden = true)
    public LocalDateTime sent;
    @ApiModelProperty(hidden = true)
    public String remoteAddress;
    @ApiModelProperty(hidden = true)
    public Set<Map.Entry<String, List<String>>> headers;
}
