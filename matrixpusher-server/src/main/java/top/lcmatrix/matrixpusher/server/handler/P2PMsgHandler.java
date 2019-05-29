package top.lcmatrix.matrixpusher.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.lcmatrix.matrixpusher.common.communicate.P2PMessage;
import top.lcmatrix.matrixpusher.common.communicate.SystemMsg;
import top.lcmatrix.matrixpusher.server.client.ClientCache;
import top.lcmatrix.matrixpusher.server.client.ClientInfo;

public class P2PMsgHandler extends SimpleChannelInboundHandler<P2PMessage>{

	private static Logger logger = LoggerFactory.getLogger(P2PMsgHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, P2PMessage msg) throws Exception {
		if(!ClientCache.contains(ctx.channel())) {
			ctx.channel().writeAndFlush(new SystemMsg(null, "请先登录"));
			return;
		}
		ClientInfo toUserInfo = ClientCache.get(msg.getToUserId());
		if(toUserInfo == null) {
			ctx.channel().writeAndFlush(new SystemMsg(msg.getFromUserId(), "对方未上线"));
			return;
		}
		toUserInfo.getChannel().writeAndFlush(msg);
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
