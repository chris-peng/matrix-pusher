package top.lcmatrix.matrixpusher.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import top.lcmatrix.matrixpusher.codecs.*;
import top.lcmatrix.matrixpusher.server.handler.CmdHandler;
import top.lcmatrix.matrixpusher.server.handler.HeartbeatHandler;
import top.lcmatrix.matrixpusher.server.handler.P2PMsgHandler;

import java.net.InetAddress;

public class Server {
	
	private ServerConfigs serverConfigs;
	
	public Server(ServerConfigs serverConfigs) {
		this.serverConfigs = serverConfigs;
	}
	
	public void start() throws InterruptedException {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						if(serverConfigs instanceof WebSocketServerConfigs){
							WebSocketServerConfigs webSocketServerConfigs = (WebSocketServerConfigs) serverConfigs;
							pipeline.addLast(new HttpServerCodec())
									.addLast(new ChunkedWriteHandler())
									.addLast(new HttpObjectAggregator(webSocketServerConfigs.getMaxAggregateContentLength()))
									.addLast(new TextWebSocketFrameDecoder())
									.addLast(new TextWebSocketFrameEncoder())
									.addLast(new WebSocketServerProtocolHandler(webSocketServerConfigs.getContextPath(),
											null, true, webSocketServerConfigs.getMaxFrameSize()));
						} else {
							pipeline.addLast(new LengthedStringDecoder())
									.addLast(new LengthedStringEncoder());
						}
						pipeline.addLast(new JsonDecoder())
							.addLast(new JsonEncoder())
							.addLast(new HeartbeatHandler())
							.addLast(new CmdHandler())
							.addLast(new P2PMsgHandler());
						;
					}
				})
				.option(ChannelOption.SO_BACKLOG, serverConfigs.getSoBackLog())
				.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture channelFuture = serverBootstrap.bind(serverConfigs.getPort()).sync();
			if(serverConfigs instanceof WebSocketServerConfigs){
				WebSocketServerConfigs webSocketServerConfigs = (WebSocketServerConfigs) serverConfigs;
				System.out.println("已启动WebSocket服务器：ws://" + getMyIp() + ":" + serverConfigs.getPort() + webSocketServerConfigs.getContextPath());
			} else {
				System.out.println("已启动Socket服务器，IP：" + getMyIp() + ":" + serverConfigs.getPort());
			}
			channelFuture.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	private static String getMyIp() {
		InetAddress ia = null;
		try {
            ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        } catch (Exception e) {
            return "";
        }
	}
	
	public static void main( String[] args ) {
		//WebSocketServerConfigs serverConfigs = new WebSocketServerConfigs();
		ServerConfigs serverConfigs = new ServerConfigs();
    	try {
			new Server(serverConfigs).start();
		} catch (Exception e) {
			System.out.println("启动服务器失败");
			e.printStackTrace();
		}
    }
}
