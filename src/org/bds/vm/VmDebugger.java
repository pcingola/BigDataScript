package org.bds.vm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.bds.lang.BdsNode;
import org.bds.lang.value.Value;
import org.bds.run.DebugMode;
import org.bds.util.Gpr;

/**
 * Data for running a VM in debugging mode
 *
 * @author pcingola
 */
public class VmDebugger implements Serializable {

	private static final long serialVersionUID = -3444495779847695585L;

	boolean debug;
	int fp; // Stop if fp matches (used for 'step over' command
	DebugMode debugMode;
	BdsVm vm;

	public VmDebugger(BdsVm vm) {
		this.vm = vm;
		debugMode = DebugMode.STEP;
	}

	/**
	 * Reached a 'breakpoint' (asm instruction)
	 */
	public void breakpoint(String label) {
		if (debugMode == DebugMode.RUN_DEBUG) return; // Ignore breakpoints
		if (label != null && !label.isEmpty()) System.err.println("Breakpoint: " + label);
		debugStep();
	}

	/**
	 * Running in debug mode: This method is invoked right before running 'node'
	 */
	public void debug() {
		debugMode = DebugMode.STEP;
		debugStep();
	}

	/**
	 * Show debug 'step' options
	 */
	void debugStep() {
		BdsNode node = vm.bdsThread.getBdsNodeCurrent();

		// Show current line
		String prg = node.toString();
		if (prg.indexOf("\n") > 0) prg = "\n" + Gpr.prependEachLine("\t", prg);
		else prg = prg + " ";

		String prompt = "DEBUG [" + debugMode + "]: " //
				+ Gpr.baseName(node.getFileName()) //
				+ ":" + node.getLineNum() //
				+ (debug ? " (" + node.getClass().getSimpleName() + ")" : "") //
				+ ": " + prg //
				+ "> " //
		;

		//---
		// Wait for options
		//---
		while (true) {
			System.err.print(prompt);
			String line = readConsole();

			if (line == null) return;
			line = line.trim();

			//---
			// Parse options
			//---

			// Empty line? => Continue using the same debug mode
			if (line.isEmpty()) {
				return;
			} else if (line.toLowerCase().startsWith("v ")) { // Show variable
				// Get variable's name
				String varName = line.substring(2).trim(); // Remove leading "s " string

				// Get and show variable
				Value val = vm.getScope().getValue(varName);
				if (val == null) System.err.println("Variable '" + varName + "' not found");
				else System.err.println(val.getType() + " : " + val);
			} else {
				// All other options are just one letter
				switch (line.toLowerCase()) {
				case "c":
					// Show current 'frame'
					System.err.println(vm.getScope().toStringLocal(false));
					break;

				case "k":
					System.err.println(vm.toStringStack());
					break;

				case "?":
				case "h":
					// Show help
					System.err.println("Help:");
					System.err.println("\t[RETURN]  : " + (debugMode == DebugMode.STEP_OVER ? "step over" : "step"));
					System.err.println("\tc         : show sCope: variables within current scope");
					System.err.println("\th         : Help");
					System.err.println("\tk         : show stacK");
					System.err.println("\to         : step Over");
					System.err.println("\tp         : show Program counter");
					System.err.println("\tq         : Quit debug mode, i.e. continue running until next 'debug' command");
					System.err.println("\tr         : Run program (until next breakpoint)");
					System.err.println("\ts         : Step");
					System.err.println("\tt         : show stack Trace");
					System.err.println("\tv varname : show Variable 'varname'");
					System.err.println("");
					break;

				case "p":
					// Show current 'pc'
					System.err.println(vm.getPc());
					break;

				case "o":
					// Switch to 'STEP_OVER' mode
					debugMode = DebugMode.STEP_OVER;
					return;

				case "q":
					// Quit debug mode
					debugMode = DebugMode.RUN_DEBUG;
					return;

				case "r":
					// Switch to 'RUN' mode
					debugMode = DebugMode.RUN;
					return;

				case "s":
					// Switch to 'STEP' mode
					debugMode = DebugMode.STEP;
					return;

				case "t":
					// Show stack trace
					System.err.println(vm.stackTrace());
					break;

				default:
					System.err.println("Unknown command '" + line + "'. Use 'h' or '?' for help.");
				}
			}
		}
	}

	/**
	 * Reached a 'node' (asm instruction)
	 */
	public void node() {
		switch (debugMode) {
		case RUN:
		case RUN_DEBUG:
			break;

		case STEP:
			debugStep();
			break;

		case STEP_OVER:
			// Keep running until we find a breakpoint
			if (fp >= 0 && vm.fp <= fp) debugStep();
			break;

		default:
			throw new RuntimeException("Unimplemented debug mode: " + debugMode);
		}
	}

	/**
	 * Read a line from STDIN
	 */
	String readConsole() {
		try {
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			String line = console.readLine();
			return line;
		} catch (Exception e) {
			return null;
		}
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
