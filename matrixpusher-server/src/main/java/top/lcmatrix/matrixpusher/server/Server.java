package top.lcmatrix.matrixpusher.server;

import java.net.InetAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import top.lcmatrix.matrixpusher.codecs.JsonDecoder;
import top.lcmatrix.matrixpusher.codecs.JsonEncoder;
import top.lcmatrix.matrixpusher.codecs.LengthedStringDecoder;
import top.lcmatrix.matrixpusher.codecs.LengthedStringEncoder;
import top.lcmatrix.matrixpusher.server.handler.CmdHandler;
import top.lcmatrix.matrixpusher.server.handler.HeartbeatHandler;
import top.lcmatrix.matrixpusher.server.handler.P2PMsgHandler;

public class Server {
	
	private int port;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void start() throws InterruptedException {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline()
						.addLast(new LengthedStringDecoder())
						.addLast(new LengthedStringEncoder())
						.addLast(new JsonDecoder())
						.addLast(new JsonEncoder())
						.addLast(new HeartbeatHandler())
						.addLast(new CmdHandler())
						.addLast(new P2PMsgHandler());
				}
			})
			.option(ChannelOption.SO_BACKLOG, ServerConfigs.SO_BACKLOG)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
		serverBootstrap.bind(port).sync();
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
	
	public static void main( String[] args )
    {
    	try {
			new Server(ServerConfigs.PORT).start();
			System.out.println("已启动服务器，IP：" + getMyIp() + "，端口：" + ServerConfigs.PORT);
		} catch (Exception e) {
			System.out.println("启动服务器失败");
			e.printStackTrace();
		}
    }
}
