package org.bds.vm;

import java.io.Serializable;

/**
 * Tuple with information for a 'catch' block stored by VM's ExceptionHandler.
 *
 * @author pcingola
 */
public class CatchBlockInfo implements Serializable {

	private static final long serialVersionUID = -937734211425821934L;

	public String handlerLabel; // Label: If there is an pendingException of type 'exceptionClass', jump to this label
	public String exceptionClassName; // Class handled by this 'catch' block
	public String variableName; // Variable name to use in the pendingException handler

	public CatchBlockInfo(String handlerLabel, String exceptionClass, String variableName) {
		this.handlerLabel = handlerLabel;
		exceptionClassName = exceptionClass;
		this.variableName = variableName;
	}

	@Override
	public String toString() {
		return "catch(" + exceptionClassName + " " + variableName + "): " + handlerLabel;
	}

}
