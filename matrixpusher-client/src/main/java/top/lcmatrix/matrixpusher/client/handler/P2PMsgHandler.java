package top.lcmatrix.matrixpusher.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.lcmatrix.matrixpusher.common.Configs;
import top.lcmatrix.matrixpusher.common.communicate.P2PMessage;

public class P2PMsgHandler extends SimpleChannelInboundHandler<P2PMessage>{

	private static Logger logger = LoggerFactory.getLogger(P2PMsgHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, P2PMessage msg) throws Exception {
		String fromUser = msg.getFromUserId();
		if(Configs.SYSTEM_USER_ID.equals(fromUser)) {
			fromUser = "系统";
		}
		System.out.println("收到来自" + fromUser + "的消息：" + msg.getMsg());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if(cause != null) {
			logger.warn("exception caught", cause);
		}
		if(ctx != null) {
			ctx.close();
		}
	}
}
