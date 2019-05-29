package top.lcmatrix.matrixpusher.server.util;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import top.lcmatrix.matrixpusher.common.Configs;
import top.lcmatrix.matrixpusher.common.communicate.SystemMsg;
import top.lcmatrix.matrixpusher.server.client.ClientCache;
import top.lcmatrix.matrixpusher.server.client.ClientInfo;
import top.lcmatrix.matrixpusher.server.client.ClientInfo.HeartbeatTask;

public class HeartbeatUtil {
	
	/**
	 * 超过3个心跳时间未收到心跳，则关闭通道
	 */
	public static final long DELAY_CLOSE_TASK = Configs.HEARBEAT_INTERVAL_MILS * 3;

	public static ClientInfo.HeartbeatTask startHeartbeatListen(ChannelHandlerContext ctx) {
		Runnable heartbeatTask = new Runnable() {
			
			public void run() {
				Channel channel = ctx.channel();
				channel.writeAndFlush(new SystemMsg(null, "客户端太久没有活动，将关闭与服务器的连接"));
				channel.close();
			}
		};
		ScheduledFuture<?> scheduler = ctx.executor().schedule(heartbeatTask, DELAY_CLOSE_TASK, TimeUnit.MILLISECONDS);
		return new ClientInfo.HeartbeatTask(scheduler, heartbeatTask);
	}
	
	public static void updateHeartbeat(final ChannelHandlerContext ctx) {
		ClientInfo clientInfo = ClientCache.get(ctx.channel());
		if(clientInfo != null) {
			//重新调度心跳监听
			renewHeartbeatListen(ctx, clientInfo);
		}
	}
	
	private static void renewHeartbeatListen(ChannelHandlerContext ctx, ClientInfo clientInfo) {
		HeartbeatTask heartbeatTask = clientInfo.getHeartbeatTask();
		if(heartbeatTask != null) {
			heartbeatTask.getScheduler().cancel(true);
			heartbeatTask.setScheduler(ctx.executor().schedule(heartbeatTask.getTask(), DELAY_CLOSE_TASK, TimeUnit.MILLISECONDS));
		}else {
			clientInfo.setHeartbeatTask(startHeartbeatListen(ctx));
		}
	}
}
