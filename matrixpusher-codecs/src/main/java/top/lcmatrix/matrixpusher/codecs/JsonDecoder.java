package top.lcmatrix.matrixpusher.codecs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.ParserConfig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class JsonDecoder extends MessageToMessageDecoder<String>{
	
	private static Logger logger = LoggerFactory.getLogger(JsonDecoder.class);
	static {
		//打开fastjson的autotype功能
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		try {
			out.add(JSON.parse(msg));
		} catch (JSONException e) {
			logger.warn("json解码失败", e);
		}
	}
}
