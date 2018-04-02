package org.bds.vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueMap;
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
	int exitCode = -1; // Default exit code is error (program did not start)
	Value[] stack; // Stack: main stack used for values
	int sp; // Stack pointer
	Scope scope; // Current scope (variables)
	Map<String, Integer> labels;
	Map<Integer, String> labelsByPc;
	Map<Object, Integer> constantsByObject;
	Map<String, VmFunction> functions;
	List<Object> constants;

	public BdsVm() {
		callStack = new int[CALL_STACK_SIZE];
		constants = new ArrayList<>();
		constantsByObject = new HashMap<>();
		functions = new HashMap<>();
		labels = new HashMap<>();
		labelsByPc = new HashMap<>();
		scope = new Scope();
		stack = new Value[STACK_SIZE];
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
	 * Create and add a new function (label + function description)
	 */
	public void addFunction(String name, int pc) {
		addLabel(name, pc);
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
		labelsByPc.put(codeidx, label);
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

	Type constantType() {
		int idx = code[pc++];
		return (Type) constants.get(idx);
	}

	/**
	 * Get exit code. If the stak is empty, then the exitcode is 0 (i.e. program finished OK)
	 */
	int exitCode() {
		exitCode = isEmptyStack() ? 0 : (int) popInt();
		return exitCode;
	}

	Object getConstant(int idx) {
		return constants.get(idx);
	}

	public int getExitCode() {
		return exitCode;
	}

	VmFunction getFunction(String name) {
		return functions.get(name);
	}

	/**
	 * Get label at code idx
	 */
	public String getLabel(int codeidx) {
		return labelsByPc.get(codeidx);
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

	public boolean isEmptyStack() {
		return sp <= 0;
	}

	/**
	 * Create a new scope
	 */
	public void newScope() {
		scope = new Scope(scope);
	}

	public Value pop() {
		if (isEmptyStack()) throw new RuntimeException("Pop from empty stack!");
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

	/**
	 * Run the program in 'code'
	 */
	public int run() {
		OpCode opcodes[] = OpCode.values();

		// Initialize program counter
		pc = Math.max(0, getLabel(LABLE_MAIN));

		// First instruction
		int instruction;
		OpCode opcode = OpCode.NOOP;

		// Some variables used for opcodes
		boolean b1, b2;
		long i1, i2, idx;
		double r1, r2;
		String name, s1, s2;
		Type type;
		Value v1, v2, val;
		ValueList vlist;
		ValueMap vmap;

		// Execute while not the end of the program
		while (pc < code.length) {
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

			case ANDB:
				b2 = popBool();
				b1 = popBool();
				push(b1 && b2);
				break;

			case ANDI:
				i2 = popInt();
				i1 = popInt();
				push(i1 & i2);
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

			case DIVI:
				i2 = popInt();
				i1 = popInt();
				push(i1 / i2);
				break;

			case DIVR:
				r2 = popReal();
				r1 = popReal();
				push(r1 / r2);
				break;

			case EQB:
				b2 = popBool();
				b1 = popBool();
				push(b1 == b2);
				break;

			case EQI:
				i2 = popInt();
				i1 = popInt();
				push(i1 == i2);
				break;

			case EQR:
				r2 = popReal();
				r1 = popReal();
				push(r1 == r2);
				break;

			case EQS:
				s2 = popString();
				s1 = popString();
				push(s1.equals(s2));
				break;

			case GEB:
				b2 = popBool();
				b1 = popBool();
				push(b1 || b1 == b2);
				break;

			case GEI:
				i2 = popInt();
				i1 = popInt();
				push(i1 >= i2);
				break;

			case GER:
				r2 = popReal();
				r1 = popReal();
				push(r1 >= r2);
				break;

			case GES:
				s2 = popString();
				s1 = popString();
				push(s1.compareTo(s2) >= 0);
				break;

			case GTB:
				b2 = popBool();
				b1 = popBool();
				push(b1 && !b2);
				break;

			case GTI:
				i2 = popInt();
				i1 = popInt();
				push(i1 > i2);
				break;

			case GTR:
				r2 = popReal();
				r1 = popReal();
				push(r1 > r2);
				break;

			case GTS:
				s2 = popString();
				s1 = popString();
				push(s1.compareTo(s2) > 0);
				break;

			case JMP:
				name = constantString(); // Get function name
				pc = getLabel(name);
				break;

			case HALT:
				return exitCode();

			case JMPT:
				if (popBool()) {
					name = constantString(); // Get label name
					pc = getLabel(name); // Jump to label
				}
				break;

			case JMPF:
				if (!popBool()) {
					name = constantString(); // Get label name
					pc = getLabel(name); // Jump to label
				}
				break;

			case LOAD:
				name = constantString();
				push(scope.getValue(name));
				break;

			case LEB:
				b2 = popBool();
				b1 = popBool();
				push(b2 || b1 == b2);
				break;

			case LEI:
				i2 = popInt();
				i1 = popInt();
				push(i1 <= i2);
				break;

			case LER:
				r2 = popReal();
				r1 = popReal();
				push(r1 <= r2);
				break;

			case LES:
				s2 = popString();
				s1 = popString();
				push(s1.compareTo(s2) <= 0);
				break;

			case LTB:
				b2 = popBool();
				b1 = popBool();
				push(b2 && !b1);
				break;

			case LTI:
				i2 = popInt();
				i1 = popInt();
				push(i1 < i2);
				break;

			case LTR:
				r2 = popReal();
				r1 = popReal();
				push(r1 < r2);
				break;

			case LTS:
				s2 = popString();
				s1 = popString();
				push(s1.compareTo(s2) < 0);
				break;

			case MODI:
				i2 = popInt();
				i1 = popInt();
				push(i1 % i2);
				break;

			case MULI:
				i2 = popInt();
				i1 = popInt();
				push(i1 * i2);
				break;

			case MULR:
				r2 = popReal();
				r1 = popReal();
				push(r1 * r2);
				break;

			case NEB:
				b2 = popBool();
				b1 = popBool();
				push(b1 != b2);
				break;

			case NEI:
				i2 = popInt();
				i1 = popInt();
				push(i1 != i2);
				break;

			case NER:
				r2 = popReal();
				r1 = popReal();
				push(r1 != r2);
				break;

			case NES:
				s2 = popString();
				s1 = popString();
				push(!s1.equals(s2));
				break;

			case NEW:
				type = constantType(); // Get type
				val = type.newValue();
				push(val);

			case NOOP:
				break;

			case NOTB:
				b1 = popBool();
				push(!b1);
				break;

			case NOTI:
				i1 = popInt();
				push(~i1);
				break;

			case ORB:
				b2 = popBool();
				b1 = popBool();
				push(b1 || b2);
				break;

			case ORI:
				i2 = popInt();
				i1 = popInt();
				push(i1 | i2);
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

			case REFLIST:
				vlist = (ValueList) pop();
				idx = popInt();
				val = vlist.getValue(idx);
				push(val);
				break;

			case REFMAP:
				vmap = (ValueMap) pop();
				v1 = pop();
				val = vmap.getValue(v1);
				push(val);
				break;

			case RET:
				popScope(); // Restore scope
				pc = popPc(); // Pop PC from call-stack
				break;

			case SCOPEPUSH:
				newScope();
				break;

			case SCOPEPOP:
				scope = scope.getParent();
				break;

			case SET:
				v1 = pop();
				v2 = pop();
				v1.setValue(v2);
				break;

			case SETLIST:
				vlist = (ValueList) pop();
				idx = popInt();
				val = pop();
				vlist.setValue(idx, val);
				break;

			case SETMAP:
				vmap = (ValueMap) pop();
				v1 = pop(); // Key
				v2 = pop(); // Value
				vmap.put(v1, v2);
				break;

			case STORE:
				name = constantString();
				scope.add(name, pop());
				break;

			case SUBI:
				i2 = popInt();
				i1 = popInt();
				push(i1 - i2);
				break;

			case SUBR:
				r2 = popReal();
				r1 = popReal();
				push(r1 - r2);
				break;

			case XORB:
				b2 = popBool();
				b1 = popBool();
				push(b1 ^ b2);
				break;

			case XORI:
				i2 = popInt();
				i1 = popInt();
				push(i1 ^ i2);
				break;

			default:
				throw new RuntimeException("Unimplemented opcode " + opcode);
			}
		}

		return exitCode();
	}

	/**
	 * Add code to the VM
	 */
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

	/**
	 * Output code as 'assembly' (disassembler)
	 */
	public String toAsm() {
		OpCode opcodes[] = OpCode.values();

		StringBuilder sb = new StringBuilder();
		for (int pc = 0; pc < code.length; pc++) {
			// Any label?
			String label = getLabel(pc);
			if (label != null) sb.append(label + ":\n");

			// Show opcode
			OpCode op = opcodes[code[pc]];
			String opstr = op.toString().toLowerCase();
			if (op.hasParam()) {
				int idx = code[++pc];
				Object param = getConstant(idx);
				sb.append("\t" + opstr + " " + param + "\n");
			} else {
				sb.append("\t" + opstr + "\n");
			}
		}

		return sb.toString();
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
