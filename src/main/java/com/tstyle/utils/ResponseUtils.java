/**
 *Copyright (c) 2017, ShangHai HOWBUY INVESTMENT MANAGEMENT Co., Ltd.
 *All right reserved.
 *
 *THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF HOWBUY INVESTMENT
 *MANAGEMENT CO., LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED
 *TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *WITHOUT THE PRIOR WRITTEN PERMISSION OF HOWBUY INVESTMENT MANAGEMENT
 * CO., LTD.
*/

package com.tstyle.utils;

import com.tstyle.constants.Constants;
import com.tstyle.handler.common.BaseRequest;
import com.tstyle.handler.common.BaseResponse;
import com.tstyle.protocol.MessageTypeEnum;
import com.tstyle.protocol.packet.MessagePacket;
import java.io.UnsupportedEncodingException;

/**
 * @description:响应工具类
 * @author weichanghuan
 */
public class ResponseUtils {
    /**
     * 
     * buildResponse:基于请求参数对象构建响应参数对象
     * 
     * @param request
     * @return
     */
    public static BaseResponse buildResponse(BaseRequest request) {
        BaseResponse resp = new BaseResponse();
        resp.setRequestId(request.getRequestId());
        resp.setServiceCode(request.getServiceCode());
        return resp;
    }

    /**
     * 
     * buildRespMessageProtocol:构建响应协议对象
     * 
     * @param reqMessageProtocol
     * @param returnCode
     * @param returnDesc
     * @return
     * @throws UnsupportedEncodingException
     */
    public static MessagePacket buildRespMessageProtocol(MessagePacket reqMessage, String returnCode, String returnDesc) throws UnsupportedEncodingException {
        BaseResponse resp = new BaseResponse();
        resp.setReturnCode(returnCode);
        resp.setReturnDesc(returnDesc);

        MessagePacket respProtocol = new MessagePacket(JSONUtil.toJSonString(resp).getBytes(Constants.CHARSET));
        respProtocol.setRequestId(reqMessage.getRequestId());
        respProtocol.setType(MessageTypeEnum.BUSSINESS_RESP.getType());

        return respProtocol;
    }

}
