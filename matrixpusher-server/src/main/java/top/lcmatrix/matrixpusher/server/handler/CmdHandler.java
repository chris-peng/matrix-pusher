package top.lcmatrix.matrixpusher.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import top.lcmatrix.matrixpusher.common.communicate.CmdResult;
import top.lcmatrix.matrixpusher.common.communicate.Command;
import top.lcmatrix.matrixpusher.common.communicate.HeartbeatCmd;
import top.lcmatrix.matrixpusher.common.communicate.LoginCmd;
import top.lcmatrix.matrixpusher.common.communicate.LogoutCmd;
import top.lcmatrix.matrixpusher.common.communicate.TestCmd;
import top.lcmatrix.matrixpusher.server.client.ClientCache;

public class CmdHandler extends SimpleChannelInboundHandler<Command>{

	private static Logger logger = LoggerFactory.getLogger(CmdHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Command cmd) throws Exception {
		String[] args = cmd.getArgs();
		Channel channel = ctx.channel();
		switch (cmd.getCmd()) {
		case LoginCmd.CMD:
			if(!cmd.validateArgs()) {
				logger.warn("参数不正确");
				channel.writeAndFlush(new CmdResult(cmd, false, "参数不正确"));
				return;
			}
			if(!ClientCache.contains(channel)) {
				channel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {

					@Override
					public void operationComplete(Future<? super Void> future) throws Exception {
						ClientCache.remove(channel);
					}
				});
			}
			//暂不验证token
			ClientCache.add(args[0], ctx);
			channel.writeAndFlush(new CmdResult(cmd, true, "登录成功"));
			break;
			
		case LogoutCmd.CMD:
			if(!cmd.validateArgs()) {
				logger.warn("参数不正确");
				channel.writeAndFlush(new CmdResult(cmd, false, "参数不正确"));
				return;
			}
			//暂不验证token
			ClientCache.remove(args[0]);
			channel.writeAndFlush(new CmdResult(cmd, true, "登出成功"));
			break;
		
		case TestCmd.CMD:
			channel.writeAndFlush(new CmdResult(cmd, true, null));
			break;
			
		case HeartbeatCmd.CMD:
			break;
			
		default:
			channel.writeAndFlush(new CmdResult(cmd, false, "未知命令"));
			break;
		}
		
		
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
