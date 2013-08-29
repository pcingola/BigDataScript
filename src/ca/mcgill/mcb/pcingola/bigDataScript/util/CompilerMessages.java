package ca.mcgill.mcb.pcingola.bigDataScript.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;

/**
 * A list of compilation messages
 * 
 * @author pcingola
 */
public class CompilerMessages implements Iterable<CompilerMessage> {

	// Store error messages by thread (we may want to paralelize compilation at some point)
	private static HashMap<Thread, CompilerMessages> compilerMessagesByThread = new HashMap<Thread, CompilerMessages>();

	String fileName;
	HashMap<String, CompilerMessage> messages;

	/**
	 * Get CompilerMessages (for this thread)
	 * @return
	 */
	public static synchronized CompilerMessages get() {
		CompilerMessages cms = compilerMessagesByThread.get(Thread.currentThread());
		if (cms == null) {
			cms = new CompilerMessages();
			compilerMessagesByThread.put(Thread.currentThread(), cms);
		}
		return cms;
	}

	/**
	 * Reset all errors
	 */
	public static synchronized void reset() {
		compilerMessagesByThread.put(Thread.currentThread(), null); // Remove entry for this thread
	}

	public static void setFileName(String fileName) {
		get().fileName = fileName;
	}

	private CompilerMessages() {
		messages = new HashMap<String, CompilerMessage>();
	}

	public void add(BigDataScriptNode node, String message, MessageType type) {
		CompilerMessage cm = new CompilerMessage(node, message, type);
		String key = cm.toString();
		if (!messages.containsKey(key)) messages.put(key, cm);
	}

	public void add(CompilerMessage cm) {
		String key = cm.toString();
		if (!messages.containsKey(key)) messages.put(key, cm);
	}
	
	/** only for special cases, returns false */
	public boolean addError(String msg) {
		add(null,msg,MessageType.ERROR);
		return false;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean hasErrors() {
		for (CompilerMessage cm : this)
			if (cm.getType() == MessageType.ERROR) return true;
		return false;
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}

	@Override
	public Iterator<CompilerMessage> iterator() {
		return messagesSorted().iterator();
	}

	/**
	 * Get all messages (sort them first)
	 * @return
	 */
	public List<CompilerMessage> messagesSorted() {
		ArrayList<CompilerMessage> list = new ArrayList<CompilerMessage>();
		for (CompilerMessage m : messages.values())
			list.add(m);
		Collections.sort(list);
		return list;
	}

	public int size() {
		return messages.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (CompilerMessage cm : CompilerMessages.get())
			sb.append(cm + "\n");

		return sb.toString();
	}

}
