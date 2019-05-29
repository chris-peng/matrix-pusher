package top.lcmatrix.matrixpusher.common.communicate;

import java.util.Date;

public class P2PMessage{
	
	public static final String TYPE_PLAIN_TEXT = "PLAIN_TEXT";
	public static final String TYPE_RICH_TEXT = "RICH_TEXT";
	public static final String TYPE_TEMPLATE_MSG = "TEMPLATE_MSG";

	private String toUserId;
	private String fromUserId;
	private String type = TYPE_PLAIN_TEXT;
	private String msg;
	private Date createTime = new Date();
	
	public P2PMessage() {}
	
	public String getFromUserId() {
		return fromUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	
	public P2PMessage(String toUserId, String msg) {
		this.toUserId = toUserId;
		this.msg = msg;
	}
	
	public P2PMessage(String fromUserId, String toUserId, String msg) {
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.msg = msg;
	}
	
	public P2PMessage(String fromUserId, String toUserId, String type, String msg) {
		this.toUserId = toUserId;
		this.type = type;
		this.msg = msg;
	}
	
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
