package top.lcmatrix.matrixpusher.server.handler;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import top.lcmatrix.matrixpusher.server.util.HeartbeatUtil;

/**
 * 收到任何消息都可视为收到心跳（可改进为收到除登陆登出等命令外的任何消息）
 * @author pengh
 *
 */
public class HeartbeatHandler extends MessageToMessageDecoder<Object>{

	@Override
	protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		HeartbeatUtil.updateHeartbeat(ctx);
		out.add(msg);
	}

}
