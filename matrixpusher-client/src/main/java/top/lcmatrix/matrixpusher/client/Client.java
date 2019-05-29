package top.lcmatrix.matrixpusher.client;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import top.lcmatrix.matrixpusher.client.handler.CmdResultHandler;
import top.lcmatrix.matrixpusher.client.handler.P2PMsgHandler;
import top.lcmatrix.matrixpusher.codecs.JsonDecoder;
import top.lcmatrix.matrixpusher.codecs.JsonEncoder;
import top.lcmatrix.matrixpusher.codecs.LengthedStringDecoder;
import top.lcmatrix.matrixpusher.codecs.LengthedStringEncoder;
import top.lcmatrix.matrixpusher.common.Configs;
import top.lcmatrix.matrixpusher.common.communicate.HeartbeatCmd;
import top.lcmatrix.matrixpusher.common.communicate.LoginCmd;
import top.lcmatrix.matrixpusher.common.communicate.LogoutCmd;
import top.lcmatrix.matrixpusher.common.communicate.P2PMessage;
import top.lcmatrix.matrixpusher.common.communicate.TestCmd;

public class Client 
{
	private Channel clientChannel = null;
	private NioEventLoopGroup eventLoopGroup;
	private Timer heartbeatTaskTimer = new Timer();
	private volatile long lastActionTime = -1;	//上次活动时间（发送消息），用于减少不必要的心跳次数
	
	public void connect(String serverHost, int port) {
		Bootstrap b = new Bootstrap();
		eventLoopGroup = new NioEventLoopGroup();
		b.group(eventLoopGroup)
			.channel(NioSocketChannel.class)
			.remoteAddress(serverHost, port)
			.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					clientChannel = ch;
					ch.pipeline()
						.addLast(new LengthedStringDecoder())
						.addLast(new LengthedStringEncoder())
						.addLast(new JsonDecoder())
						.addLast(new JsonEncoder())
						.addLast(new CmdResultHandler())
						.addLast(new P2PMsgHandler());
					ch.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {

						@Override
						public void operationComplete(Future<? super Void> future) throws Exception {
							System.out.println("连接已关闭！");
							heartbeatTaskTimer.cancel();
						}
					});
					heartbeatTaskTimer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							long currentTimeMillis = System.currentTimeMillis();
							if(lastActionTime < 0 || currentTimeMillis - lastActionTime >= Configs.HEARBEAT_INTERVAL_MILS) {
								send(new HeartbeatCmd());
								lastActionTime = -1;
							}
						}
					}, 0, Configs.HEARBEAT_INTERVAL_MILS);
				}
			});
		try {
			b.connect().sync();
			System.out.println("已连接到服务器");
		} catch (InterruptedException e) {
			System.out.println("连接服务器失败");
			e.printStackTrace();
		}
	}
	
	public void send(Object msg) {
		clientChannel.writeAndFlush(msg);
		lastActionTime = System.currentTimeMillis();
	}

	public static void main(String[] args) {
		Client client = new Client();
		if(args.length >= 2) {
			client.connect(args[0], Integer.parseInt(args[1]));
		}else {
			Object[] serverInfo = serverInfo();
			client.connect((String)serverInfo[0], (Integer)serverInfo[1]);
		}
		String[] userIdAndToken = login(client);
		boolean exit = false;
		while(!exit) {
			int selectedOper = operations();
			switch (selectedOper) {
			case 1:
				userIdAndToken = login(client);
				break;
				
			case 2:
				client.send(new LogoutCmd(userIdAndToken[0], userIdAndToken[1]));
				break;

			case 3:
				talk(client, userIdAndToken[0]);
				break;
				
			case 4:
				client.send(new TestCmd());
				break;
			
			case 5:
				client.clientChannel.close();
				exit = true;
				break;
				
			default:
				System.out.println("您选择的操作不正确");
				break;
			}
		}
		client.eventLoopGroup.shutdownGracefully();
	}
	
	private static Object[] serverInfo() {
		Scanner s = new Scanner(System.in);
		System.out.print("服务器IP：");
		String ip = s.nextLine();
		System.out.print("服务器端口：");
		int port = -1;
		while(port <= 0) {
			String sPort = s.nextLine();
			try {
				port = Integer.parseInt(sPort);
			} catch (NumberFormatException e) {
				System.out.println("请输入正确的端口号");
			}
		}
		return new Object[] {ip, port};
	}

	private static void talk(Client client, String myId) {
		Scanner s = new Scanner(System.in);
		System.out.print("请输入对方的id：");
		String toUserId = s.nextLine();
		System.out.println("开始愉快的聊天吧！（输入\"bye\"退出聊天）");
		while(true) {
			String msg = s.nextLine();
			client.send(new P2PMessage(myId, toUserId, msg));
			System.out.println("我对" + toUserId + "说：" + msg);
			if("bye".equals(msg)) {
				break;
			}
		}
	}

	private static int operations() {
		Scanner s = new Scanner(System.in);
		System.out.println("请选择操作：");
		System.out.println("1.重新登录");
		System.out.println("2.登出");
		System.out.println("3.聊天");
		System.out.println("4.测试连接");
		System.out.println("5.退出");
		int oper = -1;
		while(oper <= 0) {
			String sOper = s.nextLine();
			try {
				oper = Integer.parseInt(sOper);
			} catch (NumberFormatException e) {
				System.out.println("请输入正确的数字序号");
			}
		}
		return oper;
	}

	private static String[] login(Client client) {
		Scanner s = new Scanner(System.in);
		System.out.println("请登录：");
		System.out.print("id：");
		String userId = s.nextLine();
		System.out.print("token(任意值)：");
		String token = s.nextLine();
		client.send(new LoginCmd(userId, token));
		return new String[] {userId, token};
	}
}
