package top.lcmatrix.matrixpusher.codecs;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import top.lcmatrix.matrixpusher.common.Configs;

public class LengthedStringDecoder extends ReplayingDecoder<LengthedStringDecoder.ReadStage>{
	
	enum ReadStage{
		LENGTH, CONTENT
	}
	
	private int length;
	
	public LengthedStringDecoder(){
		super(ReadStage.LENGTH);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch (state()) {
		case LENGTH:
			int length = in.readInt();
			if(length > 0) {
				this.length = length;
				checkpoint(ReadStage.CONTENT);
			}else {
				throw new CodecException("invalid length:" + length);
			}
			break;
		case CONTENT:
			byte[] bytes = new byte[this.length];
			in.readBytes(bytes);
			String content = new String(bytes, Charset.forName(Configs.CHARSET));
			out.add(content);
			checkpoint(ReadStage.LENGTH);
			break;
		default:
			throw new CodecException("invalid state:" + state());
		}
	}
}
