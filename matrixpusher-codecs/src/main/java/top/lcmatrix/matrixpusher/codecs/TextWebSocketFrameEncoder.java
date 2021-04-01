package top.lcmatrix.matrixpusher.codecs;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

public class TextWebSocketFrameEncoder extends MessageToMessageEncoder<String>{
	
	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		out.add(new TextWebSocketFrame(msg));
	}

	@Override
	public boolean acceptOutboundMessage(Object msg) throws Exception {
		if(msg.getClass().getName().startsWith("io.netty.")){
			return false;
		}
		return true;
	}
}
