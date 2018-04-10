package org.bds.vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueClass;
import org.bds.lang.value.ValueFunction;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueMap;
import org.bds.lang.value.ValueReal;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;
import org.bds.scope.GlobalScope;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * Bds Virtual Machine
 *
 * @author pcingola
 */
public class BdsVm {

	public static final int CALL_STACK_SIZE = 1024; // Only this many nested stacks
	public static final int STACK_SIZE = 100 * 1024; // Initial stack size
	public static final String LABLE_MAIN = "main";
	private static final OpCode OPCODES[] = OpCode.values();

	boolean debug;
	boolean verbose;
	boolean run; // Keep program running while this variable is 'true'
	int callStack[]; // Stack for function calls (Program Counter)
	int code[]; // Compile assembly code (OopCodes)
	int csp; // Call stack pointer
	int exitCode = -1; // Default exit code is error (program did not start)
	int nodeId; // Current node ID (BdsNode). Used for linking to original bds code
	int nodeStack[]; // Stack of bdsNodes (stack trace functionality)
	int nsp; // Node stack pointer
	int pc; // Program counter
	int sp; // Stack pointer
	Value[] stack; // Stack: main stack used for values
	Scope scope; // Current scope (variables)
	Map<String, Integer> labels;
	Map<Integer, String> labelsByPc;
	Map<Object, Integer> constantsByObject;
	Map<String, VmFunction> functions;
	Map<String, FunctionDeclaration> functionsBySignature;
	Map<Type, Integer> typeToIndex;
	List<Object> constants;
	List<Type> types;
	BdsThread bdsThread;

	public BdsVm() {
		callStack = new int[CALL_STACK_SIZE];
		constants = new ArrayList<>();
		constantsByObject = new HashMap<>();
		functions = new HashMap<>();
		labels = new HashMap<>();
		labelsByPc = new HashMap<>();
		functionsBySignature = new HashMap<>();
		nodeStack = new int[CALL_STACK_SIZE];
		scope = new Scope();
		stack = new Value[STACK_SIZE];
		types = new ArrayList<>();
		typeToIndex = new HashMap<>();
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
	 * Add native function
	 */
	public void addFunction(FunctionDeclaration fd) {
		functionsBySignature.put(fd.signature(), fd);

		// Update parameters
		VmFunction vmf = functions.get(fd.signature());
		if (vmf != null) vmf.set(fd);
	}

	/**
	 * Create and add a new function's descriptor (PC, arg names, etc)
	 */
	public void addFunctionPc(String name, int pc) {
		addLabel(name, pc);
		VmFunction f = new VmFunction(name, pc);
		functions.put(name, f);
	}

	/**
	 *  Add native functions
	 */
	void addFunctions() {
		// Add all native functions from global scope
		for (Value v : GlobalScope.get().getValues()) {
			if (v.getType().isFunction()) {
				ValueFunction vf = (ValueFunction) v;
				FunctionDeclaration fd = vf.getFunctionDeclaration();
				addFunction(fd);
			}
		}

		// Add all native functions from all defined types
		for (Type t : types) {
			SymbolTable st = t.getSymbolTable();
			for (ValueFunction vf : st.getFunctions()) {
				FunctionDeclaration fd = vf.getFunctionDeclaration();
				addFunction(fd);
			}
		}
	}

	/**
	 * Add new label
	 * @param label
	 * @param codeidx: instruction index where label occurs
	 */
	public void addLabel(String label, int codeidx) {
		labels.put(label, codeidx);
		labelsByPc.put(codeidx, label);
	}

	/**
	 * Add new type
	 * @param type
	 * @return Type index
	 */
	public int addType(Type type) {
		if (type == null) throw new RuntimeException("Cannot add null type!");

		// Already in 'types' pool?
		if (typeToIndex.containsKey(type)) return typeToIndex.get(type);

		// Add new type
		int idx = types.size();
		types.add(type);
		typeToIndex.put(type, idx);
		return idx;
	}

	/**
	 * Call a function
	 * @param name: Function's signature
	 */
	void call(String fsig) {
		VmFunction func = getFunction(fsig); // Find function meta-data
		newScope(); // Create a new scope

		// Add all arguments to the scope. Remember that stack in reverse order
		String[] args = func.getArgs();
		for (int i = args.length - 1; i >= 0; i--)
			scope.add(args[i], pop());

		pushPc(); // Push PC to call-stack
		pc = func.getPc(); // Jump to function
		pushNodeId(); // Store latest node ID
	}

	/**
	 * Call a method
	 * @param name: Method's signature
	 */
	void callMethod(String fsig) {
		VmFunction func = getFunction(fsig); // Find method meta-data
		newScope(); // Create a new scope

		// Add all arguments to the scope. Remember that stack in reverse order
		String[] args = func.getArgs();
		for (int i = args.length - 1; i >= 0; i--)
			scope.add(args[i], pop());

		pushPc(); // Push PC to call-stack
		pc = func.getPc(); // Jump to function
		pushNodeId(); // Store latest node ID
	}

	/**
	 * Call a method
	 * @param name: Method's signature
	 */
	void callMethodNative(String fsig) {
		// Find function
		FunctionDeclaration fdecl = functionsBySignature.get(fsig);

		newScope(); // Create a new scope

		// Add all arguments to the scope. Remember that stack in reverse order
		Value vthis = null; // The last item to pop from the stack is 'this'
		List<String> args = fdecl.getParameterNames();
		for (int i = args.size() - 1; i >= 0; i--) {
			vthis = pop();
			scope.add(args.get(i), vthis);
		}

		// Run method
		MethodNative mn = (MethodNative) fdecl;
		Value retVal = mn.runMethod(bdsThread, vthis);

		push(retVal); // Push result to stack
		popScope(); // Restore old scope
	}

	/**
	 * Call native function
	 * @param fsig
	 */
	void callNative(String fsig) {
		// Find function
		FunctionDeclaration fdecl = functionsBySignature.get(fsig);

		newScope(); // Create a new scope

		// Add all arguments to the scope. Remember that stack in reverse order
		List<String> args = fdecl.getParameterNames();
		for (int i = args.size() - 1; i >= 0; i--)
			scope.add(args.get(i), pop());

		// Run function
		FunctionNative fn = (FunctionNative) fdecl;
		Value retVal = fn.runFunctionNativeValue(bdsThread);
		push(retVal); // Push result to stack
		popScope(); // Restore old scope
	}

	/**
	 * Parameters is a reference to a 'bool' constant
	 */
	boolean constantBool() {
		int idx = code[pc++];
		return (Boolean) constants.get(idx);
	}

	/**
	 * Parameters is a reference to a 'int' constant
	 */
	long constantInt() {
		int idx = code[pc++];
		return (Long) constants.get(idx);
	}

	/**
	 * Parameters is a reference to a 'real' constant
	 */
	double constantReal() {
		int idx = code[pc++];
		return (Double) constants.get(idx);
	}

	/**
	 * Parameters is a reference to a 'string' constant
	 */
	String constantString() {
		int idx = code[pc++];
		return (String) constants.get(idx);
	}

	/**
	 * Parameters is a reference to a 'type' constant
	 */
	Type constantType() {
		int idx = code[pc++];
		return types.get(idx);
	}

	/**
	 * Get exit code. If the stack is empty, then the exit-code is 0 (i.e. program finished OK)
	 */
	int exitCode() {
		exitCode = isEmptyStack() ? 0 : (int) popInt();
		return exitCode;
	}

	public BdsThread getBdsThread() {
		return bdsThread;
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

	public Scope getScope() {
		return scope;
	}

	Type getType(int idx) {
		return types.get(idx);
	}

	public Value getValue(String name) {
		return scope.getValue(name);
	}

	/**
	 * Does the OpCode at position 'pc' have a parameter?
	 */
	boolean hasParam(int pc) {
		return OPCODES[code[pc]].hasParam();
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

	/**
	 * Parameter is an int
	 */
	int paramInt() {
		return code[pc++];
	}

	public Value pop() {
		if (isEmptyStack()) throw new RuntimeException("Pop from empty stack!");
		return stack[--sp];
	}

	/**
	 * Pop a bool from stack
	 */
	public boolean popBool() {
		return pop().asBool();
	}

	/**
	 * Pop an int from stack
	 */
	public long popInt() {
		return pop().asInt();
	}

	/**
	 * Pop value from node-stack
	 */
	void popNodeId() {
		nodeId = nodeStack[--nsp];
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
		return pop().asReal();
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
		return pop().asString();
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
	void pushNodeId() {
		nodeStack[nsp++] = nodeId;
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
		// Initialize program counter
		pc = Math.max(0, getLabel(LABLE_MAIN));

		// Add functions from global scope
		addFunctions();

		run = true;
		runLoop();

		return exitCode();
	}

	/**
	 * Run the program in 'code'
	 */
	protected void runLoop() {
		debug = true;

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
		ValueClass vclass;

		// Execute while not the end of the program
		while (pc < code.length && run) {
			instruction = code[pc];
			opcode = OPCODES[instruction];
			if (debug) System.err.println(toAsm(pc) + "\t\t\tstack: " + toStringStack());
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
				name = constantString(); // Get function signature
				call(name);
				break;

			case CALLM:
				name = constantString(); // Get method signature
				callMethod(name);
				break;

			case CALLNATIVE:
				name = constantString(); // Get function signature
				callNative(name);
				break;

			case CALLMNATIVE:
				name = constantString(); // Get method signature
				callMethodNative(name);
				break;

			case DEC:
				i1 = popInt();
				push(--i1);
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

			case HALT:
				return;

			case INC:
				i1 = popInt();
				push(++i1);
				break;

			case JMP:
				name = constantString(); // Get function name
				pc = getLabel(name);
				break;

			case JMPT:
				if (popBool()) {
					name = constantString(); // Get label name
					pc = getLabel(name); // Jump to label
				} else pc++;
				break;

			case JMPF:
				if (!popBool()) {
					name = constantString(); // Get label name
					pc = getLabel(name); // Jump to label
				} else pc++;
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
				break;

			case NODE:
				nodeId = paramInt();
				break;

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

			case POP:
				pop();
				break;

			case PRINT:
				System.out.print(pop());
				break;

			case PRINTLN:
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

			case REFFIELD:
				name = constantString();
				vclass = (ValueClass) pop();
				val = vclass.getValue(name);
				push(val);
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
				popNodeId();
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

			case SETFIELD:
				name = constantString();
				vclass = (ValueClass) pop();
				val = pop();
				vclass.setValue(name, val);
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

			case SWAP:
				v2 = pop();
				v1 = pop();
				push(v2);
				push(v1);
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
	}

	public void sanityCheckStack() {
		if (sp > 1) {
			Gpr.debug("Stack size: " + sp + "\n" + toStringStack());
			throw new RuntimeException("Inconsistent stack. Size: " + sp);
		}
	}

	public void setBdsThread(BdsThread bdsThread) {
		this.bdsThread = bdsThread;
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

	public String stackTrace() {
		// TODO: !!! IMPLEMENT
		return "";
	}

	/**
	 * Output code as 'assembly' (disassembler)
	 */
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		for (int pc = 0; pc < code.length; pc++) {
			sb.append(toAsm(pc) + "\n");
			if (hasParam(pc)) pc++;
		}

		return sb.toString();
	}

	/**
	 * Show opcode at 'pc'
	 */
	String toAsm(int pc) {
		// Any label?
		String label = getLabel(pc);

		// Show opcode
		OpCode op = OPCODES[code[pc]];
		String opstr = op.toString().toLowerCase();

		// Parameter?
		String param = null;
		if (op.hasParam()) {
			int idx = code[++pc];
			if (op.isParamString()) param = "'" + getConstant(idx) + "'";
			else if (op.isParamType()) param = "'" + getType(idx) + "'";
			else param = getConstant(idx).toString();
		}

		return (label != null ? label + ":\n" : "") //
				+ "\t" + opstr //
				+ (param != null ? " " + param : "") //
		;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PC         : " + pc + "\n");
		sb.append("Stack      : " + toStringStack());
		sb.append("Call-Stack : [");
		for (int i = 0; i < csp; i++)
			sb.append(" " + callStack[i]);
		sb.append(" ]\n");
		sb.append("Scope:\n" + scope);
		return sb.toString();
	}

	String toStringStack() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < sp; i++)
			sb.append((i > 0 ? ", '" : "'") + stack[i] + "'");
		sb.append(" ]");
		return sb.toString();
	}
}
