package top.lcmatrix.matrixpusher.server.client;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import top.lcmatrix.matrixpusher.server.util.HeartbeatUtil;

public class ClientCache {

	private static final Map<String, ClientInfo> userIdClientInfoMap = new ConcurrentHashMap<String, ClientInfo>();
	private static final Map<Channel, ClientInfo> channelClientInfoMap = new ConcurrentHashMap<Channel, ClientInfo>();
	
	public static void add(String userId, ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		ClientInfo clientInfo = channelClientInfoMap.get(channel);
		if(clientInfo != null) {
			clientInfo.addUserId(userId);
			userIdClientInfoMap.put(userId, clientInfo);
		}else {
			clientInfo = new ClientInfo(userId, channel, HeartbeatUtil.startHeartbeatListen(ctx));
			userIdClientInfoMap.put(userId, clientInfo);
			channelClientInfoMap.put(channel, clientInfo);
		}
	}
	
	public static ClientInfo get(String userId) {
		return userIdClientInfoMap.get(userId);
	}
	
	public static ClientInfo get(Channel clientChannel) {
		return channelClientInfoMap.get(clientChannel);
	}
	
	public static void remove(String userId) {
		ClientInfo clientInfo = userIdClientInfoMap.get(userId);
		if(clientInfo != null) {
			clientInfo.removeUserId(userId);
			if(clientInfo.userIdCount() == 0) {
				channelClientInfoMap.remove(clientInfo.getChannel());
			}
			userIdClientInfoMap.remove(userId);
		}
	}
	
	public static void remove(Channel clientChannel) {
		ClientInfo clientInfo = channelClientInfoMap.get(clientChannel);
		if(clientInfo != null) {
			for(String userId : clientInfo.getUserIds()) {
				userIdClientInfoMap.remove(userId);
			}
		}
		channelClientInfoMap.remove(clientChannel);
	}
	
	public static boolean contains(String userId) {
		return userIdClientInfoMap.containsKey(userId);
	}
	
	public static boolean contains(Channel clientChannel) {
		return channelClientInfoMap.containsKey(clientChannel);
	}
	
	public static Collection<ClientInfo> allClients() {
		return userIdClientInfoMap.values();
	}
	
	public static Collection<String> allUserIds() {
		return userIdClientInfoMap.keySet();
	}
	
	public static int clientCount() {
		return channelClientInfoMap.size();
	}
	
	public static int userCount() {
		return userIdClientInfoMap.size();
	}
}
