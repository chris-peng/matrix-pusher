package top.lcmatrix.matrixpusher.server;

public class ServerConfigs {
	
	private int port = 18888;

	/**
	 * 连接队列大小
	 */
	private int soBackLog = 128;

	private String contextPath = "/ws";
	private int maxAggregateContentLength = 8192;
	private int maxFrameSize = 65536 * 10;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getSoBackLog() {
		return soBackLog;
	}

	public void setSoBackLog(int soBackLog) {
		this.soBackLog = soBackLog;
	}

	public void validate(){
	}
}
