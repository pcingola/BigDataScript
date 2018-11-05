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

	Map<String, CatchBlockInfo> catchBlocksByClass;
	String finallyLabel;
	ValueClass pendingException; // Exception being handled

	public ExceptionHandler(String finallyLabel) {
		this.finallyLabel = finallyLabel;
		pendingException = null;
	}

	public void addHandler(String handlerLabel, String exceptionClassName, String variableName) {
		if (catchBlocksByClass == null) catchBlocksByClass = new HashMap<>();
		CatchBlockInfo cb = new CatchBlockInfo(handlerLabel, exceptionClassName, variableName);
		catchBlocksByClass.put(exceptionClassName, cb);
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

	public void resetHandlers() {
		catchBlocksByClass = null;
	}

	public void resetPendingException() {
		pendingException = null;
	}

	public void setPendingException(ValueClass ex) {
		pendingException = ex;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Finally block: " + finallyLabel + "\n");
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
