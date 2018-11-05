package org.bds.vm;

/**
 * Tuple with information for a 'catch' block stored by VM's ExceptionHandler.
 *
 * @author pcingola
 */
public class CatchBlockInfo {

	public String handlerLabel; // Label: If there is an pendingException of type 'exceptionClass', jump to this label
	public String exceptionClassName; // Class handled by this 'catch' block
	public String variableName; // Variable name to use in the pendingException handler

	public CatchBlockInfo(String handlerLabel, String exceptionClass, String variableName) {
		this.handlerLabel = handlerLabel;
		exceptionClassName = exceptionClass;
		this.variableName = variableName;
	}

}
