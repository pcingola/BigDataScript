package org.bds.vm;

import java.util.Deque;

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

	Deque<Value> stack; // Program stack
	int pc;
	int code[];
	Scope scope; // Base scope

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

		long i1, i2;
		double r1, r2;
		String s1, s2;

		// Execute while not the end of the program
		while (pc < code.length && opcode != OpCode.HALT) {
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

			default:
				throw new RuntimeException("Unimplemented opcode " + opcode);
			}

			instruction = code[pc];
			opcode = opcodes[instruction];
		}

	}
}
