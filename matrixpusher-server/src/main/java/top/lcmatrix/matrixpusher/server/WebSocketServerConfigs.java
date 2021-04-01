package top.lcmatrix.matrixpusher.server;

public class WebSocketServerConfigs extends ServerConfigs{
	
	private String contextPath = "/ws";
	private int maxAggregateContentLength = 8192;
	private int maxFrameSize = 65536 * 10;

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public int getMaxAggregateContentLength() {
		return maxAggregateContentLength;
	}

	public void setMaxAggregateContentLength(int maxAggregateContentLength) {
		this.maxAggregateContentLength = maxAggregateContentLength;
	}

	public int getMaxFrameSize() {
		return maxFrameSize;
	}

	public void setMaxFrameSize(int maxFrameSize) {
		this.maxFrameSize = maxFrameSize;
	}
}
