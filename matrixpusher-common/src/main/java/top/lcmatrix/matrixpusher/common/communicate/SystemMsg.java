package top.lcmatrix.matrixpusher.common.communicate;

import top.lcmatrix.matrixpusher.common.Configs;

public class SystemMsg extends P2PMessage{
	
	public SystemMsg() {
		setFromUserId(Configs.SYSTEM_USER_ID);
	}
	
	public SystemMsg(String toUserId, String msg) {
		super(toUserId, msg);
		setFromUserId(Configs.SYSTEM_USER_ID);
	}
}
