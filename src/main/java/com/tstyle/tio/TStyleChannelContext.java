package com.tstyle.tio;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Node;

public class TStyleChannelContext extends ChannelContext {

    public TStyleChannelContext(GroupContext groupContext, AsynchronousSocketChannel asynchronousSocketChannel) {
        super(groupContext, asynchronousSocketChannel);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Node createClientNode(AsynchronousSocketChannel asynchronousSocketChannel) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
