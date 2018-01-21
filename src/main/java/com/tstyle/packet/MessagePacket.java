package com.tstyle.packet;

import com.tstyle.constants.Constants;
import java.util.Arrays;
import org.tio.core.intf.Packet;

/**
 * <pre>
 * 自己定义的协议 数据包格式 +——----——+——-----——+——----——+ |协议开始标志| 请求ID|消息类型| 长度 | 数据 |
 * +——----——+——-----——+——----——+ 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76
 * 2.请求ID,每次请求唯一ID 3.消息类型，消息类型定义 4.传输数据的长度contentLength，int类型 5.要传输的数据
 * 
 * @author weichanghuan
 *
 */
public class MessagePacket extends Packet {
    private static final long serialVersionUID = -172060606924066412L;
    /**
     * 消息的开头信息标记
     */
    private int headData = Constants.HEAD_DATA;
    /**
     * 会话Id
     */
    private String requestId;
    /**
     * 消息长度
     */
    private int contentLength;
    /**
     * 暂定： 消息类型 0:业务请求消息 1:业务响应消息 2:业务ONE WAY消息(既是请求又是响应消息) 3:握手请求消息 4:握手应答消息
     * 5:心跳请求消息 6:心跳应答消息
     */
    private byte type;

    public static final String CHARSET = Constants.CHARSET;
    private byte[] content;

    public MessagePacket() {

    }

    public MessagePacket(byte[] content) {
        if (content != null) {
            this.contentLength = content.length;
            this.content = content;
        }

    }

    public final int getHeadData() {
        return headData;
    }

    public final void setHeadData(int headData) {
        this.headData = headData;
    }

    public final String getRequestId() {
        return requestId;
    }

    public final void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public final byte getType() {
        return type;
    }

    public final void setType(byte type) {
        this.type = type;
    }

    public final int getContentLength() {
        return contentLength;
    }

    public final void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public final byte[] getContent() {
        return content;
    }

    public final void setContent(byte[] content) {
        if (content != null) {
            this.contentLength = content.length;
            this.content = content;
        }
    }

    @Override
    public String toString() {
        return "MessagePacket [headData=" + headData + ", sessionId=" + requestId + ", type=" + type + ", contentLength=" + contentLength + ", content="
                + Arrays.toString(content) + "]";
    }
}
