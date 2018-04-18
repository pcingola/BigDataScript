package org.bds.vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bds.Config;
import org.bds.lang.expression.SysFactory;
import org.bds.lang.expression.TaskFactory;
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
import org.bds.run.RunState;
import org.bds.scope.GlobalScope;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;
import org.bds.task.Task;
import org.bds.task.TaskDependency;
import org.bds.util.AutoHashMap;
import org.bds.util.Gpr;
import org.bds.util.GprString;

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

	int code[]; // Compile assembly code (OopCodes)
	boolean debug;
	Integer exitCode = null; // Default exit code: program did not start
	int fp; // Frame pointer
	int nodeId; // Current node ID (BdsNode). Used for linking to original bds code
	int pc; // Program counter
	boolean run; // Keep program running while this variable is 'true'
	Scope scope; // Current scope (variables)
	int sp; // Stack pointer
	Value[] stack; // Stack: main stack used for values
	CallFrame[] callFrame; // Call Frame stack
	boolean verbose;
	Map<String, Integer> labels;
	AutoHashMap<Integer, List<String>> labelsByPc;
	Map<Object, Integer> constantsByObject;
	Map<String, VmFunction> functions;
	Map<String, FunctionDeclaration> functionsBySignature;
	Map<Type, Integer> typeToIndex;
	List<Object> constants;
	List<Type> types;
	BdsThread bdsThread;

	public BdsVm() {
		constants = new ArrayList<>();
		constantsByObject = new HashMap<>();
		functions = new HashMap<>();
		labels = new HashMap<>();
		labelsByPc = new AutoHashMap<>(new LinkedList<String>());
		functionsBySignature = new HashMap<>();
		scope = new Scope();
		stack = new Value[STACK_SIZE];
		types = new ArrayList<>();
		typeToIndex = new HashMap<>();

		sp = fp = pc = 0;
		nodeId = -1;

		// Initialize call frames
		callFrame = new CallFrame[CALL_STACK_SIZE];
		for (int i = 0; i < callFrame.length; i++)
			callFrame[i] = new CallFrame();
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
		String sig = fd.signature();
		functionsBySignature.put(sig, fd);

		// Add VM function description only for non-native functions
		if (fd.isNative()) return;

		VmFunction vmf = functions.get(sig);
		if (vmf != null) {
			// Update parameters
			vmf.set(fd);
		} else {
			// Add new entry
			vmf = new VmFunction(sig, -1);
			vmf.set(fd);
			functions.put(sig, vmf);
		}
	}

	/**
	 * Create and add a new function's descriptor (PC, arg names, etc)
	 */
	public void addFunctionPc(String name, int pc) {
		addLabel(name, pc);

		VmFunction vmf = functions.get(name);
		if (vmf != null) {
			// Update parameters
			vmf.setPc(pc);
		} else {
			// Add new entry
			vmf = new VmFunction(name, pc);
			functions.put(name, vmf);
		}

	}

	/**
	 *  Add global functions
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

		// Add all native functions from all defined types (e.g. classes, list, map, etc.)
		for (Type t : types) {
			SymbolTable st = t.getSymbolTable();
			if (st.hasFunctions()) {
				for (ValueFunction vf : st.getFunctions()) {
					FunctionDeclaration fd = vf.getFunctionDeclaration();
					addFunction(fd);
				}
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
		labelsByPc.getOrCreate(codeidx).add(label);
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
		pushCallFrame(); // Push stack frame

		VmFunction func = getFunction(fsig); // Find function meta-data
		newScope(); // Create a new scope

		// Add all arguments to the scope. Remember that stack in reverse order
		String[] args = func.getArgs();
		for (int i = args.length - 1; i >= 0; i--)
			scope.add(args[i], pop());

		pc = func.getPc(); // Jump to function
	}

	//	/**
	//	 * Call a method
	//	 * @param name: Method's signature
	//	 */
	//	void callMethod(String fsig) {
	//		pushCallFrame(); // Push stack frame
	//
	//		VmFunction func = getFunction(fsig); // Find method meta-data
	//		newScope(); // Create a new scope
	//
	//		// Add all arguments to the scope. Remember that stack in reverse order
	//		String[] args = func.getArgs();
	//		for (int i = args.length - 1; i >= 0; i--)
	//			scope.add(args[i], pop());
	//
	//		pc = func.getPc(); // Jump to function
	//	}

	/**
	 * Call a native method or function 
	 * @param name: Method's signature
	 */
	void callNative(String fsig) {
		// Find function
		FunctionDeclaration fdecl = functionsBySignature.get(fsig);

		newScope(); // Create a new scope

		// Args: Add all arguments to the scope
		// Note: Stack in reverse order
		Value vthis = null; // The last item to pop from the stack is 'this'
		List<String> args = fdecl.getParameterNames();
		for (int i = args.size() - 1; i >= 0; i--) {
			vthis = pop();
			scope.add(args.get(i), vthis);
		}

		// Invoke
		Value retVal;
		if (fdecl.isMethod()) {
			// Run method
			MethodNative mn = (MethodNative) fdecl;
			retVal = mn.runMethod(bdsThread, vthis);
		} else {
			// Run function
			FunctionNative fn = (FunctionNative) fdecl;
			retVal = fn.runFunction(bdsThread);
		}

		push(retVal); // Push result to stack
		popScope(); // Restore old scope
	}

	//	/**
	//	 * Call native function
	//	 * @param fsig
	//	 */
	//	void callNative(String fsig) {
	//		// Find function
	//		FunctionDeclaration fdecl = functionsBySignature.get(fsig);
	//
	//		newScope(); // Create a new scope
	//
	//		// Add all arguments to the scope. Remember that stack in reverse order
	//		List<String> args = fdecl.getParameterNames();
	//		for (int i = args.size() - 1; i >= 0; i--)
	//			scope.add(args.get(i), pop());
	//
	//		// Run function
	//		FunctionNative fn = (FunctionNative) fdecl;
	//		Value retVal = fn.runFunction(bdsThread);
	//		push(retVal); // Push result to stack
	//		popScope(); // Restore old scope
	//	}

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
	 * Execute a 'dep' operator
	 */
	void dep() {
		ValueList ins = (ValueList) pop();
		ValueList outs = (ValueList) pop();

		TaskDependency td = new TaskDependency();

		for (Value out : outs)
			td.addOutput(out.asString());

		for (Value in : ins)
			td.addOutput(in.asString());

		push(td.depOperator());
	}

	/**
	 * Get exit code. If the stack is empty, then the exit-code is 0 (i.e. program finished OK)
	 */
	int exitCode() {
		// Set externally? E.g. BdsThread.fatalError()
		if (exitCode != null) return exitCode;
		exitCode = isEmptyStack() ? 0 : (int) popInt();
		return exitCode;
	}

	/**
	 * Create a 'fork' vm-thread
	 */
	private BdsVm fork() {
		BdsVm vmclone = new BdsVm();

		vmclone.code = code;
		vmclone.debug = debug;
		vmclone.nodeId = nodeId;
		vmclone.run = run;
		vmclone.verbose = verbose;
		vmclone.labels = labels;
		vmclone.labelsByPc = labelsByPc;
		vmclone.constantsByObject = constantsByObject;
		vmclone.functions = functions;
		vmclone.functionsBySignature = functionsBySignature;
		vmclone.typeToIndex = typeToIndex;
		vmclone.constants = constants;
		vmclone.types = types;

		// Child process
		// vmclone.callFrame : Initialized, because child process has a new frame
		// vmclone.fp        : Initialized to zero because child process has a new frame
		// vmclone.bdsThread : Will be set by 'forked' bdsThread
		vmclone.pc = pc; // Already pointing to instruction after 'fork'		
		vmclone.scope = scope; // Same scope

		return vmclone;
	}

	/**
	 * Execute a 'fork' opcode
	 */
	void forkOpCode() {
		BdsVm vmfork = fork();
		bdsThread.fork(vmfork);
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
	public List<String> getLabel(int codeidx) {
		return labelsByPc.get(codeidx);
	}

	/**
	 * Get code index where label occurs, -1 if not found
	 */
	public int getLabel(String label) {
		return labels.containsKey(label) ? labels.get(label) : -1;
	}

	public int getNodeId() {
		return nodeId;
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
	 * Kill opCode
	 */
	void kill() {
		Value tid = pop();
		if (tid.getType().isList()) bdsThread.kill((ValueList) tid);
		else bdsThread.kill(tid);
	}

	/**
	 * Multiply a string by an int (i.e. repeat string 'n' times)
	 */
	String muls(Value v1, Value v2) {
		String s = "";
		long count = 0;
		if (v1.getType().isString()) {
			s = v1.asString();
			count = v2.asInt();
		} else {
			s = v2.asString();
			count = v1.asInt();
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++)
			sb.append(s);
		return sb.toString();
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

	public Value peek() {
		return stack[sp - 1];
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
	 * Restore from call frame
	 */
	void popCallFrame() {
		CallFrame sf = callFrame[--fp];
		pc = sf.pc;
		nodeId = sf.nodeId;
		scope = sf.scope;
	}

	/**
	 * Pop an int from stack
	 */
	public long popInt() {
		return pop().asInt();
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
	 * Push call frame
	 */
	void pushCallFrame() {
		if (fp >= callFrame.length) throw new RuntimeException("Out of stack memory! Call frame pointer: " + fp);
		CallFrame sf = callFrame[fp++];
		sf.set(pc, nodeId, scope);
	}

	/**
	 * Run the program in 'code'
	 */
	public int run() {
		// Initialize program counter
		// Note: If vm forked, then pc is already initialized in child
		//       process, do not change.
		if (pc == 0) pc = Math.max(0, getLabel(LABLE_MAIN));

		// Add functions from global scope
		addFunctions();

		run = true;
		try {
			runLoop();
		} catch (Throwable t) {
			bdsThread.setRunState(RunState.FATAL_ERROR);
			exitCode = 1;

			if (Config.get().isVerbose()) {
				t.printStackTrace();
			}
		}

		return exitCode();
	}

	/**
	 * Run the program in 'code'
	 */
	protected void runLoop() {
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

			case CALLNATIVE:
				name = constantString(); // Get signature
				callNative(name);
				break;

			//			case CALLMNATIVE:
			//				name = constantString(); // Get method signature
			//				callMethodNative(name);
			//				break;

			case CAST_TOB:
				push(pop().asBool());
				break;

			case CAST_TOI:
				push(pop().asInt());
				break;

			case CAST_TOR:
				push(pop().asReal());
				break;

			case CAST_TOS:
				push(pop().asString());
				break;

			case DEC:
				i1 = popInt();
				push(--i1);
				break;

			case DEP:
				dep();
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

			case DUP:
				push(peek().clone());
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

			case ERROR:
				System.err.println(popString());
				exitCode = Task.EXITCODE_ERROR;
				bdsThread.setRunState(RunState.FATAL_ERROR);
				return;

			case FORK:
				forkOpCode();
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
				bdsThread.setRunState(RunState.FINISHED);
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

			case KILL:
				kill();
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

			case MULS:
				v2 = pop();
				v1 = pop();
				push(muls(v1, v2));
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
				sp--; // Drop last value from stack
				break;

			case PRINT:
				System.out.print(pop());
				break;

			case PRINTLN:
				System.out.println(pop());
				break;

			case PRINTSTDERR:
				System.err.print(popString());
				break;

			case PRINTSTDERRLN:
				System.err.println(popString());
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
				popCallFrame(); // Restore everything from stack frame
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
				push(v1);
				break;

			case SETFIELD:
				name = constantString();
				vclass = (ValueClass) pop();
				vclass.setValue(name, peek()); // We leave the value in the stack
				break;

			case SETLIST:
				vlist = (ValueList) pop();
				idx = popInt();
				vlist.setValue(idx, peek()); // We leave the value in the stack
				break;

			case SETMAP:
				vmap = (ValueMap) pop();
				v1 = pop(); // Key
				vmap.put(v1, peek()); // We leave the value in the stack
				break;

			case STORE:
				name = constantString();
				scope.setValue(name, peek()); // We leave the value in the stack
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

			case SYS:
				sys();
				break;

			case SWAP:
				v2 = pop();
				v1 = pop();
				push(v2);
				push(v1);
				break;

			case TASK:
				task();
				break;

			case VAR:
				name = constantString();
				scope.add(name, peek()); // We leave the value in the stack
				break;

			case WAIT:
				waitTask();
				break;

			case WAITALL:
				waitTaskAll();
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

	public void setRun(boolean run) {
		this.run = run;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String stackTrace() {
		// TODO: !!! IMPLEMENT
		return "";
	}

	/**
	 * Execute a 'sys' instruction
	 */
	void sys() {
		SysFactory sf = new SysFactory(bdsThread);
		sf.run();
	}

	/**
	 * Execute a 'task'
	 */
	void task() {
		TaskFactory tf = new TaskFactory(bdsThread);
		tf.run();
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
		StringBuilder sb = new StringBuilder();

		// Any label?
		List<String> labels = getLabel(pc);
		if (labels != null) {
			for (String l : labels)
				sb.append(l + ":\n");
		}

		// Show opcode
		OpCode op = OPCODES[code[pc]];
		String opstr = op.toString().toLowerCase();
		sb.append("\t" + opstr);

		// Parameter?
		if (op.hasParam()) {
			String param = null;

			int idx = code[++pc];
			if (op.isParamString()) param = "'" + getConstant(idx) + "'";
			else if (op.isParamType()) param = "'" + getType(idx) + "'";
			else if (op.isParamNodeId()) param = "" + idx;
			else param = getConstant(idx).toString();

			sb.append(" " + param);
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("VM:\n");
		sb.append("  pc         : " + pc + "\n");
		sb.append("  fp         : " + fp + "\n");
		sb.append("  sp         : " + sp + "\n");
		sb.append("  Stack      : " + toStringStack() + "\n");
		sb.append("  Call-Stack : [");
		for (int i = 0; i < fp; i++)
			sb.append(" " + callFrame[i]);
		sb.append(" ]\n");
		sb.append("  Scope:\n" + scope);
		return sb.toString();
	}

	String toStringStack() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		String s;
		for (int i = 0; i < sp; i++) {
			Value v = stack[i];

			if (v == null) {
				s = "null";
			} else if (v.getType() == null) {
				s = "[ERROR: Type is null]";
				Gpr.debug("ERROR: Value has null type");
			} else {
				if (v.getType().isString()) s = "'" + GprString.escape(v.asString()) + "'";
				else s = v.toString();
			}

			sb.append((i > 0 ? ", " : "") + s);
		}
		sb.append(" ]");
		return sb.toString();
	}

	void waitTask() {
		ValueList tids = (ValueList) pop();
		boolean ok = bdsThread.wait(tids);
		push(ok);
	}

	void waitTaskAll() {
		boolean ok = bdsThread.waitAll();
		push(ok);
	}

}
