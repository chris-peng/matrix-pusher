package top.lcmatrix.matrixpusher.common.communicate;

/**
 * 测试命令，收到不做任何操作，仅回应“成功”的命令结果
 * @author pengh
 *
 */
public class TestCmd extends Command{

	public static final String CMD = "PING";
	
	public TestCmd() {
		setCmd(CMD);
	}
}
