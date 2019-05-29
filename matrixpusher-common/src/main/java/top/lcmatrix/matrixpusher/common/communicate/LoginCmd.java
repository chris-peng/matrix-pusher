package top.lcmatrix.matrixpusher.common.communicate;

/**
 * 登陆命令，参数：userId token
 * @author pengh
 *
 */
public class LoginCmd extends Command{

	public static final String CMD = "LOGIN";
	
	public LoginCmd() {
		setCmd(CMD);
	}
	
	public LoginCmd(String userId, String token) {
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
