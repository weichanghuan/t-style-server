package com.tstyle.tio;

import com.tstyle.constants.Constants;
import com.tstyle.listener.AioCommonListenerImpl;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

/**
 * t-io Server
 * 
 * @author weichanghuan
 *
 */
@Component
public class TioServer {

    private static final Logger logger = LoggerFactory.getLogger(TioServer.class);

    @Value("${tio.server.host}")
    private String host;

    @Value("${tio.server.port}")
    private int port;

    @Value("${tio.server.ioThreadNum}")
    private int ioThreadNum;

    @Value("${tio.server.backlog}")
    private int backlog;

    // handler, 包括编码、解码、消息处理
    public static ServerAioHandler aioHandler = new TStyleServerHandler();

    // 事件监听器，可以为null，
    public static ServerAioListener aioListener = new AioCommonListenerImpl();;

    // 一组连接共用的上下文对象
    public static ServerGroupContext serverGroupContext = new ServerGroupContext(aioHandler, aioListener);

    // 一组连接共用的上下文对象
    public static ChannelContext channelContext = new TStyleChannelContext(serverGroupContext, null);

    // aioServer对象
    public static AioServer aioServer = new AioServer(serverGroupContext);

    /**
     * 启动
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    @PostConstruct
    public void start() throws InterruptedException, IOException {
        logger.info("begin to start tio server");
        serverGroupContext.setHeartbeatTimeout(Constants.TIMEOUT);
        aioServer.start(host, port);
        logger.info("tio server listening on port " + port + " and ready for connections...");
    }

    @PreDestroy
    public void stop() {
        logger.info("destroy server resources");
        Aio.close(channelContext, "remark");
    }

}
