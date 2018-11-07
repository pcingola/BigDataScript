package org.bds.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.lang.value.ValueClass;

/**
 * Exception handlers are added when try/catch/finally blocks are executed
 *
 * @author pcingola
 */
public class ExceptionHandler implements Serializable {

	private static final long serialVersionUID = 1193221593865738652L;

	boolean catchStart, finallyStart; // Exception handling started
	Map<String, CatchBlockInfo> catchBlocksByClass; // Catch blocks indexed by exception className
	String finallyLabel; // Finally label for this try/catch/block
	ValueClass pendingException; // Pending exception (to be re-thrown)

	public ExceptionHandler(String finallyLabel) {
		this.finallyLabel = finallyLabel;
		pendingException = null;
	}

	public void addHandler(String handlerLabel, String exceptionClassName, String variableName) {
		if (catchBlocksByClass == null) catchBlocksByClass = new HashMap<>();
		CatchBlockInfo cb = new CatchBlockInfo(handlerLabel, exceptionClassName, variableName);
		catchBlocksByClass.put(exceptionClassName, cb);
	}

	public void catchStart() {
		catchStart = true;
		finallyStart = false;
	}

	public void finallyStart() {
		catchStart = false;
		finallyStart = true;
	}

	/**
	 * Get catch block info if availble for 'exception'
	 */
	public CatchBlockInfo getCatchBlockInfo(ValueClass exception) {
		if (catchBlocksByClass == null) return null;
		return catchBlocksByClass.get(exception.getType().getCanonicalName());
	}

	public String getFinallyLabel() {
		return finallyLabel;
	}

	public ValueClass getPendingException() {
		return pendingException;
	}

	public boolean isCatchStart() {
		return catchStart;
	}

	public boolean isFinallyStart() {
		return finallyStart;
	}

	public void setPendingException(ValueClass ex) {
		pendingException = ex;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Finally block: " + finallyLabel);
		if (isCatchStart()) sb.append(", catchStart: true");
		if (isFinallyStart()) sb.append(", finalStart: true");
		sb.append("\n");
		sb.append("Pending exception: " + (pendingException != null ? pendingException.getType().getCanonicalName() : "null") + "\n");
		if (catchBlocksByClass != null) {
			List<String> keys = new ArrayList<>();
			keys.addAll(catchBlocksByClass.keySet());
			Collections.sort(keys);
			for (String key : keys)
				sb.append("\t" + catchBlocksByClass.get(key) + "\n");
		}
		return sb.toString();
	}

}
