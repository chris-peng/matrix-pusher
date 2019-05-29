package top.lcmatrix.matrixpusher.common.communicate;

public class LogoutCmd extends Command{

	public static final String CMD = "LOGOUT";
	
	public LogoutCmd() {
		setCmd(CMD);
	}
	
	public LogoutCmd(String userId, String token) {
		this();
		setArgs(new String[] {userId, token});
	}

	@Override
	public boolean validateArgs() {
		if(getArgs() == null || getArgs().length < 2) {
			return false;
		}
		return true;
	}
}
