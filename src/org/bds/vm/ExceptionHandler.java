package org.bds.vm;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception handlers are added when try/catch/finally blocks are executed
 *
 * @author pcingola
 */
public class ExceptionHandler {

	class CatchBlock {
		String handlerLabel; // Label: If there is an exception of type 'exceptionClass', jump to this label
		String exceptionClass; // Class handled by this 'catch' block
		String variableName; // Variable name to use in the exception handler

		public CatchBlock(String handlerLabel, String exceptionClass, String variableName) {
			this.handlerLabel = handlerLabel;
			this.exceptionClass = exceptionClass;
			this.variableName = variableName;
		}
	}

	Map<String, CatchBlock> catchBlocksByClass;
	String finallyLable;

	public ExceptionHandler() {
		catchBlocksByClass = new HashMap<>();
	}

	public void addHandler(String handlerLabel, String exceptionClass, String variableName) {
		CatchBlock cb = new CatchBlock(handlerLabel, exceptionClass, variableName);
		catchBlocksByClass.put(exceptionClass, cb);
	}
}
