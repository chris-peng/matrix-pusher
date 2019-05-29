package top.lcmatrix.matrixpusher.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.lcmatrix.matrixpusher.common.communicate.CmdResult;
import top.lcmatrix.matrixpusher.common.communicate.LoginCmd;
import top.lcmatrix.matrixpusher.common.communicate.LogoutCmd;
import top.lcmatrix.matrixpusher.common.communicate.TestCmd;

public class CmdResultHandler extends SimpleChannelInboundHandler<CmdResult>{

	private static Logger logger = LoggerFactory.getLogger(CmdResultHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CmdResult cmdResult) throws Exception {
		switch (cmdResult.getCmd().getCmd()) {
		case LoginCmd.CMD:
			System.out.println("登录" + (cmdResult.isSuccess() ? "成功" : "失败") + "！");
			break;

		case LogoutCmd.CMD:
			System.out.println("登出" + (cmdResult.isSuccess() ? "成功" : "失败") + "！");
			break;
			
		case TestCmd.CMD:
			System.out.println("测试连接" + (cmdResult.isSuccess() ? "成功" : "失败") + "！");
			break;
			
		default:
			System.out.println("执行命令\"" + cmdResult.getCmd() + "\"" + (cmdResult.isSuccess() ? "成功" : "失败") + "！");
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
