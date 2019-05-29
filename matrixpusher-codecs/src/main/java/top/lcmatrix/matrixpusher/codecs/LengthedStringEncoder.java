package top.lcmatrix.matrixpusher.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.lcmatrix.matrixpusher.common.Configs;

public class LengthedStringEncoder extends MessageToByteEncoder<String>{

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
		byte[] contentBytes = msg.getBytes(Configs.CHARSET);
		out.writeInt(contentBytes.length);
		out.writeBytes(contentBytes);
	}

}
