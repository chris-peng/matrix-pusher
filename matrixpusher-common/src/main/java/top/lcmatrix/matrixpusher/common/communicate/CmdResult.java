package top.lcmatrix.matrixpusher.common.communicate;

public class CmdResult{

	private Command cmd;
	private boolean success = false;
	private String message;
	
	public CmdResult() {}
	
	public CmdResult(Command cmd, boolean success, String message) {
		this.cmd = cmd;
		this.success = success;
		this.message = message;
	}
	
	public Command getCmd() {
		return cmd;
	}

	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
