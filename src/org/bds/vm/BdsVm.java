package org.bds.vm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
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

	int pc;
	int code[];
	Deque<Integer> callStack; // Stack for function calls (Program Counter)
	Deque<Value> stack; // Stack: main stack used for values
	Scope scope; // Current scope (variables)
	Map<String, Integer> labels;
	Map<Object, Integer> constantsByObject;
	List<Object> constants;

	public BdsVm() {
		labels = new HashMap<>();
		constants = new ArrayList<>();
		constantsByObject = new HashMap<>();
		callStack = new LinkedList<>();
		stack = new LinkedList<>();
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
		return idx;
	}

	/**
	 * Add new label
	 * @param label
	 * @param codeidx: instruction index where label occurs
	 */
	public void addLabel(String label, int codeidx) {
		labels.put(label, codeidx);
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

	/**
	 * Get code index where label occurs, -1 if not found
	 */
	public int getLabel(String label) {
		return labels.containsKey(label) ? labels.get(label) : -1;
	}

	public Value getValue(String name) {
		return scope.getValue(name);
	}

	public Value pop() {
		return stack.removeFirst();
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
	 * Pop a real from stack
	 */
	public double popReal() {
		return (Double) Types.REAL.cast(pop()).get();
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
		stack.addFirst(val);
	}

	public void run() {
		OpCode opcodes[] = OpCode.values();

		// First instruction
		int instruction = code[pc];
		OpCode opcode = opcodes[instruction];

		// Some variables used for opcodes
		long i1, i2;
		double r1, r2;
		String name, s1, s2;

		// Execute while not the end of the program
		while (pc < code.length && opcode != OpCode.HALT) {
			instruction = code[pc];
			opcode = opcodes[instruction];
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

			case STORE:
				name = constantString();
				scope.add(name, pop());
				break;

			default:
				throw new RuntimeException("Unimplemented opcode " + opcode);
			}
		}
	}

	public void setCode(List<Integer> code) {
		this.code = new int[code.size()];
		for (int i = 0; i < code.size(); i++)
			this.code[i] = code.get(i);
	}
}
