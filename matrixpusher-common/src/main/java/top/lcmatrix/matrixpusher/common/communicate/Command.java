package top.lcmatrix.matrixpusher.common.communicate;

import java.util.UUID;

public class Command{

	private String id = UUID.randomUUID().toString();
	private String cmd;
	private String[] args;
	
	public Command() {}
	
	public String getCmd() {
		return cmd;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public boolean validateArgs() {
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getCmd());
		if(this.getArgs() != null) {
			for(String arg : this.getArgs()) {
				sb.append(" " + arg);
			}
		}
		return sb.toString();
	}
}
