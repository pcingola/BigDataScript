package org.bds.lang.nativeFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bds.lang.BdsNode;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;

/**
 * Log a message to STDERR, give some "debug" information (file name and line number)
 *
 * @author pcingola
 */
public class FunctionNativeLogd extends FunctionNative {

	private static final long serialVersionUID = 4328132832275759104L;

	SimpleDateFormat format;

	public FunctionNativeLogd() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "logd";
		returnType = Types.STRING;

		// Show time in GMT timezone
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));

		String argNames[] = { "str" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		// Get `log(msg)` argument
		String str = bdsThread.getString("str");

		// Get file name and line number
		String fnln = "";
		BdsNode bdsNode = bdsThread.getBdsNodeCurrent();
		if (bdsNode != null) {
			String filename = bdsNode.getFileName();
			if (filename != null) {
				filename = Gpr.baseName(filename);
				int lineNum = bdsNode.getLineNum();
				if (lineNum >= 0) {
					fnln = filename + ':' + lineNum;
				}
			}
		}

		// Show current time (GMT)
		String ymdhms = format.format(new Date());
		String out = ymdhms + '\t' + fnln + '\t' + str;

		// Show log message to stderr
		System.err.println(out);
		return out;
	}

}
