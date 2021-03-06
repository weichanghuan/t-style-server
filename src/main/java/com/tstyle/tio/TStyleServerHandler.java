package com.tstyle.tio;

import com.tstyle.constants.Constants;
import com.tstyle.protocol.packet.MessagePacket;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

/**
 * 服务端处理
 * 
 * @author weichanghuan
 */
@Component
@Scope("prototype")
public class TStyleServerHandler implements ServerAioHandler {

    private static final Logger logger = LoggerFactory.getLogger(TStyleServerHandler.class);

    @Autowired
    TStyleServerDispatchHandler tStyleServerDispatchHandler;

    /**
     * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包 总的消息结构：消息头 + 消息体 消息头结构： 4个字节，存储消息体的长度
     * 消息体结构： 对象的json串的byte[]
     */
    @Override
    public MessagePacket decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
        int readableLength = buffer.limit() - buffer.position();
        // 收到的数据组不了业务包，则返回null以告诉框架数据不够
        if (readableLength < Constants.HEAD_DATA) {
            return null;
        }

        // 读取消息体的长度
        int bodyLength = buffer.getInt();

        // 数据不正确，则抛出AioDecodeException异常
        if (bodyLength < 0) {
            throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
        }

        // 计算本次需要的数据长度
        int neededLength = Constants.HEAD_DATA + bodyLength;
        // 收到的数据是否足够组包
        int isDataEnough = readableLength - neededLength;
        // 不够消息体长度(剩下的buffe组不了消息体)
        if (isDataEnough < 0) {
            return null;
        } else // 组包成功
        {
            MessagePacket imPacket = new MessagePacket();
            if (bodyLength > 0) {
                byte[] dst = new byte[bodyLength];
                buffer.get(dst);
                imPacket.setContent(dst);
            }
            return imPacket;
        }
    }

    /**
     * 编码：把业务消息包编码为可以发送的ByteBuffer 总的消息结构：消息头 + 消息体 消息头结构： 4个字节，存储消息体的长度 消息体结构：
     * 对象的json串的byte[]
     */
    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        MessagePacket helloPacket = (MessagePacket) packet;
        byte[] body = helloPacket.getContent();
        int bodyLen = 0;
        if (body != null) {
            bodyLen = body.length;
        }

        // bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
        int allLen = Constants.HEAD_DATA + bodyLen;
        // 创建一个新的bytebuffer
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        // 设置字节序
        buffer.order(groupContext.getByteOrder());

        // 写入消息头----消息头的内容就是消息体的长度
        buffer.putInt(bodyLen);

        // 写入消息体
        if (body != null) {
            buffer.put(body);
        }
        return buffer;
    }

    /**
     * 处理消息
     */
    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        MessagePacket messagePacket = (MessagePacket) packet;
        byte[] body = messagePacket.getContent();
        logger.debug("请求的消息为{}" + messagePacket.toString());
        if (body != null) {
            // String str = new String(body, MessagePacket.CHARSET);
            // 接收到前置的消息
            // System.out.println("收到消息：" + str);
            MessagePacket resp = tStyleServerDispatchHandler.execute(messagePacket);
            logger.debug("响应的消息{}" + messagePacket.toString());
            // MessagePacket resppacket = new MessagePacket();
            // resppacket.setContent(("收到了你的消息，你的消息是:" +
            // str).getBytes(MessagePacket.CHARSET));
            Aio.send(channelContext, resp);
        }
        return;
    }
}
