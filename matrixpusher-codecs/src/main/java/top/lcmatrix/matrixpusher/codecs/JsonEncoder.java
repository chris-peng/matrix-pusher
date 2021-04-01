package top.lcmatrix.matrixpusher.codecs;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class JsonEncoder extends MessageToMessageEncoder<Object>{
	
	private static final Class<?>[] IGNORE_CLASSES = {
		String.class
	};
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		out.add(JSON.toJSONString(msg, SerializerFeature.WriteClassName));
	}

	@Override
	public boolean acceptOutboundMessage(Object msg) throws Exception {
		if(msg.getClass().getName().startsWith("io.netty.")){
			return false;
		}
		for(Class<?> c : IGNORE_CLASSES) {
			if(c.equals(msg.getClass())) {
				return false;
			}
		}
		return true;
	}
}
