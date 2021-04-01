package top.lcmatrix.matrixpusher.codecs;

import com.alibaba.fastjson.JSONException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TextWebSocketFrameDecoder extends MessageToMessageDecoder<TextWebSocketFrame>{
	
	private static Logger logger = LoggerFactory.getLogger(TextWebSocketFrameDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
		try {
			out.add(msg.text());
		} catch (JSONException e) {
			logger.warn("json解码失败", e);
		}
	}
}
