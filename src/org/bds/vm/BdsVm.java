package org.bds.vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueReal;
import org.bds.lang.value.ValueString;
import org.bds.scope.Scope;

/**
 * Bds Virtual Machine
 *
 * @author pcingola
 */
public class BdsVm {

	public static final int CALL_STACK_SIZE = 1024; // Only this many nested stacks
	public static final int STACK_SIZE = 100 * 1024; // Initial stack size
	public static final String LABLE_MAIN = "main";

	boolean verbose, debug;
	int pc;
	int code[];
	int callStack[]; // Stack for function calls (Program Counter)
	int csp; // Call stack pointer
	Value[] stack; // Stack: main stack used for values
	int sp; // Stack pointer
	Scope scope; // Current scope (variables)
	Map<String, Integer> labels;
	Map<Object, Integer> constantsByObject;
	Map<String, VmFunction> functions;
	List<Object> constants;

	public BdsVm() {
		labels = new HashMap<>();
		constants = new ArrayList<>();
		constantsByObject = new HashMap<>();
		callStack = new int[CALL_STACK_SIZE];
		functions = new HashMap<>();
		stack = new Value[STACK_SIZE];
		scope = new Scope();
	}

	/**
	 * Add new constant
	 * @param constant
	 * @return Constant index
	 */
	public int addConstant(Object constant) {
		// Already in constants pool?
		if (constantsByObject.containsKey(constant)) return constantsByObject.get(constant);

		// Add new constant
		int idx = constants.size();
		constants.add(constant);
		constantsByObject.put(constant, idx);
		if (verbose) System.err.println("Added constant (idx: " + idx + "): " + constant);
		return idx;
	}

	/**
	 * Create and add a new function
	 */
	public void addFunction(String name, int pc) {
		VmFunction f = new VmFunction(name, pc);
		functions.put(name, f);
		if (verbose) System.err.println("Added function " + f);
	}

	/**
	 * Add new label
	 * @param label
	 * @param codeidx: instruction index where label occurs
	 */
	public void addLabel(String label, int codeidx) {
		labels.put(label, codeidx);
		if (verbose) System.err.println("Added label '" + label + "': " + codeidx);
	}

	boolean constantBool() {
		int idx = code[pc++];
		return (Boolean) constants.get(idx);
	}

	long constantInt() {
		int idx = code[pc++];
		return (Long) constants.get(idx);
	}

	double constantReal() {
		int idx = code[pc++];
		return (Double) constants.get(idx);
	}

	String constantString() {
		int idx = code[pc++];
		return (String) constants.get(idx);
	}

	VmFunction getFunction(String name) {
		return functions.get(name);
	}

	/**
	 * Get code index where label occurs, -1 if not found
	 */
	public int getLabel(String label) {
		return labels.containsKey(label) ? labels.get(label) : -1;
	}

	public Value getValue(String name) {
		return scope.getValue(name);
	}

	/**
	 * Create a new scope
	 */
	public void newScope() {
		scope = new Scope(scope);
	}

	public Value pop() {
		if (sp < 0) throw new RuntimeException("Pop from empty stack!");
		return stack[--sp];
	}

	/**
	 * Pop a bool from stack
	 */
	public boolean popBool() {
		return (Boolean) Types.BOOL.cast(pop()).get();
	}

	/**
	 * Pop an int from stack
	 */
	public long popInt() {
		return (Long) Types.INT.cast(pop()).get();
	}

	/**
	 * Pop value from call-stack
	 */
	int popPc() {
		return callStack[--csp];
	}

	/**
	 * Pop a real from stack
	 */
	public double popReal() {
		return (Double) Types.REAL.cast(pop()).get();
	}

	/**
	 * Restore old scope
	 */
	public void popScope() {
		scope = scope.getParent();
	}

	/**
	 * Pop a string from stack
	 */
	public String popString() {
		return (String) Types.STRING.cast(pop()).get();
	}

	public void push(boolean b) {
		push(new ValueBool(b));
	}

	public void push(double v) {
		push(new ValueReal(v));
	}

	public void push(long v) {
		push(new ValueInt(v));
	}

	public void push(String s) {
		push(new ValueString(s));
	}

	public void push(Value val) {
		if (sp >= stack.length) {
			// Resize stack
			stack = Arrays.copyOf(stack, 2 * stack.length);
		}
		stack[sp++] = val;
	}

	/**
	 * Push program counter to call-stack
	 */
	void pushPc() {
		callStack[csp++] = pc;
	}

	public void run() {
		OpCode opcodes[] = OpCode.values();

		// Initialize program counter
		pc = Math.max(0, getLabel(LABLE_MAIN));

		// First instruction
		int instruction;
		OpCode opcode = OpCode.NOOP;

		// Some variables used for opcodes
		long i1, i2;
		double r1, r2;
		String name, s1, s2;
		Value v1, v2;

		// Execute while not the end of the program
		while (pc < code.length && opcode != OpCode.HALT) {
			instruction = code[pc];
			opcode = opcodes[instruction];
			if (debug) System.err.println("PC: " + pc + "\tOpCode: " + opcode);
			pc++;

			switch (opcode) {
			case ADDI:
				i2 = popInt();
				i1 = popInt();
				push(i1 + i2);
				break;

			case ADDR:
				r2 = popReal();
				r1 = popReal();
				push(r1 + r2);
				break;

			case ADDS:
				s2 = popString();
				s1 = popString();
				push(s1 + s2);
				break;

			case CALL:
				name = constantString(); // Get function name
				VmFunction func = getFunction(name); // Find function meta-data
				newScope(); // Create a new scope
				for (String arg : func.getArgs()) // Add all arguments to the scope
					scope.add(arg, pop());
				pushPc(); // Push PC to call-stack
				pc = func.getPc(); // Jump to function
				break;

			case LOAD:
				name = constantString();
				push(scope.getValue(name));
				break;

			case PRINT:
				System.out.println(pop());
				break;

			case PUSHB:
				push(constantBool());
				break;

			case PUSHI:
				push(constantInt());
				break;

			case PUSHR:
				push(constantReal());
				break;

			case PUSHS:
				push(constantString());
				break;

			case RET:
				popScope(); // Restore scope
				pc = popPc(); // Pop PC from call-stack
				break;

			case SET:
				v1 = pop();
				v2 = pop();
				v1.setValue(v2);
				break;

			case STORE:
				name = constantString();
				scope.add(name, pop());
				break;

			default:
				throw new RuntimeException("Unimplemented opcode " + opcode);
			}
		}

		if (debug) System.err.println(this);
	}

	public void setCode(List<Integer> code) {
		this.code = new int[code.size()];
		for (int i = 0; i < code.size(); i++)
			this.code[i] = code.get(i);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PC         : " + pc + "\n");

		sb.append("Stack      : [");
		for (int i = 0; i < sp; i++)
			sb.append(" " + stack[i]);
		sb.append(" ]\n");

		sb.append("Call-Stack : [");
		for (int i = 0; i < csp; i++)
			sb.append(" " + callStack[i]);
		sb.append(" ]\n");

		sb.append("Scope:\n" + scope);
		return sb.toString();
	}

}
