/**
 * 
 */
package com.tstyle.tio;

import com.tstyle.enums.ReturnCodeEnum;
import com.tstyle.handler.common.BaseRequest;
import com.tstyle.handler.common.BaseResponse;
import com.tstyle.handler.common.HandlerService;
import com.tstyle.listener.HandlerServiceAnnotationListener;
import com.tstyle.packet.MessagePacket;
import com.tstyle.protocol.MessageTypeEnum;
import com.tstyle.utils.JSONUtil;
import com.tstyle.utils.ResponseUtils;
import com.xiaoleilu.hutool.util.CharsetUtil;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * t-style服务端分发处理类
 * 
 * @author weichanghuan
 *
 */
@Component
public class TStyleServerDispatchHandler {

    private static final Logger logger = LoggerFactory.getLogger(TStyleServerDispatchHandler.class);

    @SuppressWarnings("unchecked")
    MessagePacket execute(MessagePacket protocol) throws UnsupportedEncodingException {
        MessagePacket respProtocol = null;
        BaseResponse response = null;
        if (protocol != null && MessageTypeEnum.BUSSINESS_REQ.getType() == protocol.getType()) {
            if (protocol.getContent() != null && protocol.getContent().length > 0) {
                String content = new String(protocol.getContent(), CharsetUtil.UTF_8);
                BaseRequest request = JSONUtil.toObject(content, BaseRequest.class);
                Map<String, Object> requestMap = null;
                HandlerService service = HandlerServiceAnnotationListener.handlerServiceMap.get(request.getServiceCode());
                if (service != null) {
                    requestMap = JSONUtil.toObject(content, HashMap.class);
                    response = service.execute(requestMap);
                    respProtocol = new MessagePacket(JSONUtil.toJSonString(response).getBytes(CharsetUtil.UTF_8));
                    respProtocol.setRequestId(protocol.getRequestId());
                    respProtocol.setType(MessageTypeEnum.BUSSINESS_RESP.getType());
                    return respProtocol;
                } else {
                    respProtocol = ResponseUtils.buildRespMessageProtocol(protocol, ReturnCodeEnum.SERVICE_NOT_OPENED.getCode(),
                            ReturnCodeEnum.SERVICE_NOT_OPENED.getDesc());
                    logger.error("无效的请求服务,serviceCode{}", request.getServiceCode());
                }
            } else {
                respProtocol = ResponseUtils.buildRespMessageProtocol(protocol, ReturnCodeEnum.PARAMETER_ERROR.getCode(), "业务请求参数不能为空");
                logger.error("业务请求参数不能为空");
            }
        }
        return respProtocol;
    }
}
