package org.bds.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bds.BdsLog;
import org.bds.Config;
import org.bds.lang.BdsNode;
import org.bds.lang.BdsNodeFactory;
import org.bds.lang.nativeFunctions.FunctionNative;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
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
import org.bds.run.Freeze;
import org.bds.run.RunState;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;
import org.bds.task.DepImpVmOpcode;
import org.bds.task.DepVmOpcode;
import org.bds.task.SysVmOpcode;
import org.bds.task.TaskDependency;
import org.bds.task.TaskImpVmOpcode;
import org.bds.task.TaskVmOpcode;
import org.bds.util.AutoHashMap;
import org.bds.util.Gpr;
import org.bds.util.GprString;

/**
 * Bds Virtual Machine
 *
 * @author pcingola
 */
public class BdsVm implements Serializable, BdsLog {

	public static final int CALL_STACK_SIZE = 1024; // Only this many nested stacks
	public static final String LABEL_MAIN = "main";
	private static final OpCode OPCODES[] = OpCode.values();
	private static final long serialVersionUID = 6533146851765102340L;
	public static final int SLEEP_TIME_FREEZE = 200; // Milliseconds
	public static final int STACK_SIZE = 100 * 1024; // Initial stack size
	BdsThread bdsThread;
	CallFrame[] callFrames; // Call Frame stack
	int code[]; // Compile assembly code (OopCodes)
	List<Object> constants;
	Map<Integer, Integer> coverageCounter; // Count how many times a nodeId was traversed
	Map<Object, Integer> constantsByObject;
	boolean debug;
	ExceptionHandler exceptionHandler; // Current Exception handler (null if we are not in a 'try/catch' statement)
	ValueClass exceptionValue; // Latest exception thrown (this is mostly used for test cases)
	Integer exitCode = null; // Default exit code (null means: parse last entry from stack)
	int fp; // Frame pointer
	Map<String, FunctionDeclaration> functionsBySignature;
	Map<String, Integer> labels;
	AutoHashMap<Integer, List<String>> labelsByPc;
	int nodeId; // Current node ID (BdsNode). Used for linking to original bds code
	int pc; // Program counter
	boolean recoveredCheckpoint, recoveredCheckpointOp;
	boolean run; // Keep program running while this variable is 'true'
	Scope scope; // Current scope (variables)
	int sp; // Stack pointer
	Value[] stack; // Stack: main stack used for values
	List<Type> types;
	Map<Type, Integer> typeToIndex;
	boolean verbose;
	VmDebugger vmDebugger;
	VmState vmState = new VmState();
	Throwable javaException; // Any Java exception captured by the VM (used for test cases)

	public BdsVm() {
		constants = new ArrayList<>();
		constantsByObject = new HashMap<>();
		coverageCounter = new HashMap<>();
		labels = new HashMap<>();
		labelsByPc = new AutoHashMap<>(new LinkedList<String>());
		functionsBySignature = new HashMap<>();
		scope = new Scope();
		stack = new Value[STACK_SIZE];
		types = new ArrayList<>();
		typeToIndex = new HashMap<>();

		recoveredCheckpoint = false; // Is this a recovered checkpoint? This is NOT altered by the VM
		recoveredCheckpointOp = false; // Is this a recovered checkpoint? This IS reset by the VM after the first read

		sp = fp = pc = 0;
		nodeId = -1;

		// Initialize call frames
		callFrames = new CallFrame[CALL_STACK_SIZE];
		for (int i = 0; i < callFrames.length; i++)
			callFrames[i] = new CallFrame();
	}

	/**
	 * Add arguments to the scope for a function / method call
	 * Note: Remember that stack in reverse order
	 *
	 * @return Last argument added (it corresponds to 'this' in a method call)
	 */
	Value addArgsCallScope(FunctionDeclaration fdecl) {
		Value vthis = null; // The last item to pop from the stack is 'this'
		List<String> args = fdecl.getParameterNames();
		for (int i = args.size() - 1; i >= 0; i--) {
			vthis = pop();
			scope.add(args.get(i), vthis);
		}
		return vthis;
	}

	/**
	 * Add arguments to the scope for a function / method call
	 */
	void addArgsCallScope(FunctionDeclaration fdecl, Value[] values) {
		List<String> args = fdecl.getParameterNames();
		for (int i = 0; i < args.size(); i++)
			scope.add(args.get(i), values[i]);
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
	 * Add function
	 */
	public void addFunction(FunctionDeclaration fd) {
		addFunction(fd, null);
	}

	/**
	 * Add function using an optional 'label'
	 *
	 * 'Label' is the signature as written in the ASM code. If the
	 * code is written manually, there can be additional spaces
	 * or other minor changes.
	 */
	void addFunction(FunctionDeclaration fd, String label) {
		if (label != null) functionsBySignature.put(label, fd);
		functionsBySignature.put(fd.signature(), fd);
		functionsBySignature.put(fd.signatureVarNames(), fd);
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
	 * Add all methods for type 't'
	 */
	void addMethods(Type t) {
		SymbolTable st = t.getSymbolTable();
		if (st.hasFunctions()) {
			for (ValueFunction vf : st.getFunctions()) {
				FunctionDeclaration fd = vf.getFunctionDeclaration();
				addFunction(fd, null);
			}
		}
	}

	/**
	 * Add multiple strings
	 */
	void adds(int count) {
		int spStart = sp - count;
		StringBuilder sb = new StringBuilder();
		for (int i = spStart; i < sp; i++)
			sb.append(stack[i]);

		sp = spStart;
		push(sb.toString());
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

		// Add all method for 'type'
		addMethods(type);

		return idx;
	}

	/**
	 * Call a function
	 * @param name: Function's signature
	 */
	void call(String fsig) {
		pushCallFrame(); // Push stack frame
		FunctionDeclaration fdecl = functionsBySignature.get(fsig); // Find function meta-data
		newScope(); // Create a new scope
		addArgsCallScope(fdecl); // Add function arguments to scope
		pc = fdecl.getPc(); // Jump to function
	}

	/**
	 * Call a function
	 * @param name: Function's signature
	 */
	void callMethod(String fsig, boolean isSuper) {
		pushCallFrame(); // Push stack frame
		FunctionDeclaration fdecl = functionsBySignature.get(fsig);
		newScope(); // Create a new scope
		Value[] values = getArgsFromStack(fdecl); // Get arguments from scope
		Value vthis = values[0]; // First argument is 'this'
		fdecl = resolveVirtualMethod(vthis, fdecl, isSuper, fsig); // Find 'virtual method' (class inheritance)
		addArgsCallScope(fdecl, values); // Add arguments to scope
		pc = fdecl.getPc(); // Jump to method
	}

	/**
	 * Call a native method or function
	 * @param name: Method's signature
	 */
	Value callNative(String fsig) {
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

		popScope(); // Restore old scope
		return retVal;
	}

	boolean canPopFrame() {
		return fp > 0;
	}

	/**
	 * Can the latest value in the stack be casted to 'className'?
	 * If it can, do nothing, if it cannot, fatal error
	 * @param className
	 */
	void castToClass(String className) {
		// Peek value and get type
		Value val = peek();
		Type typeSrc = val.getType();
		// Get destination type
		Type typeDst = Types.get(className);
		// Any null type? (i.e. not found)
		if (typeSrc != null && typeDst != null) {
			// Same type? OK, can cast
			if (typeSrc.getCanonicalName().equals(typeDst.getCanonicalName())) return;
			// Subclass? OK, can cast
			if (typeSrc.isClass() && typeDst.isClass() && typeSrc.canCastTo(typeDst)) return;
		}
		// Cannot cast
		fatalError("Cannot cast '" + typeSrc + "' to '" + className + "'");
	}

	/**
	 * Throw an exception using an exceptionHandler
	 */
	void catchException(ValueClass exceptionValue) {
		CatchBlockInfo catchBlockInfo = null;

		// Are we already handling an exception?
		if (exceptionHandler.isCatchStart()) {
			// We are already in a 'catch' block handling another exception
			// Handle this jumping to a finally block, this exception (exceptionValue)
			// will be re-thrown after finally block is executed
			catchBlockInfo = null;
		} else if (exceptionHandler.isFinallyStart()) {
			// An exception thrown while we are in a 'finally' block
			// Add exception as 'pending' and finish handling exception
			// forcing the new exception to be re-thrown immediately
			exceptionHandler.setPendingException(exceptionValue);
			ehEnd();
			return;
		} else {
			// We are not handling an exception, find a handler and
			// jump to corresponding catch block. If no catch block is
			// found, jump to finally block and re-throw exception after.
			catchBlockInfo = exceptionHandler.getCatchBlockInfo(exceptionValue);
		}

		// Get ready to jump to this call frame
		if (catchBlockInfo != null) {
			// This catch block can handle the Exception
			scope.add(catchBlockInfo.variableName, exceptionValue); // Add exception object to scope
			pc = getLabel(catchBlockInfo.handlerLabel); // Jump to catch block
		} else {
			// There is no catch block that can handle this Exception.
			// Execute the 'finally' block, which will re-throw the
			// pending Exception after successful execution.

			// Set this as a 'pending Exception'. The 'finally' block
			// will re-throw it after finishing executing statements
			exceptionHandler.setPendingException(exceptionValue);
			pc = getLabel(exceptionHandler.getFinallyLabel()); // Jump to finally block
		}
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
	 * debug OpCode
	 */
	void debug() {
		System.err.println(popString());
		if (vmDebugger == null) vmDebugger = new VmDebugger(this);
		vmDebugger.setDebug(debug);
		vmDebugger.debug();
	}

	/**
	 * Execute a 'dep' operator
	 */
	void dep() {
		ValueList ins = (ValueList) pop();
		ValueList outs = (ValueList) pop();

		BdsNode bdsNode = bdsThread.getBdsNodeCurrent();
		TaskDependency td = new TaskDependency(bdsNode);
		td.setDebug(debug);

		for (Value out : outs)
			td.addOutput(out.asString());

		for (Value in : ins)
			td.addInput(in.asString());

		push(td.depOperator());
	}

	void discardCallFrame() {
		--fp;
	}

	/**
	 * Exception handler: Add catch block parameters to Exception handler
	 */
	void ehAdd(String handlerLabel, String typeExceptionClassName, String catchVarName) {
		exceptionHandler.addHandler(handlerLabel, typeExceptionClassName, catchVarName);
	}

	/**
	 * Exception handler: Create a new Exception handler
	 * @param finallyLabel : Label for 'finally' code
	 */
	void ehCreate(String finallyLabel) {
		pushCallFrame();
		exceptionHandler = new ExceptionHandler(finallyLabel);
	}

	void ehcStart() {
		exceptionHandler.catchStart();
	}

	/**
	 * Exception handler: End exception handling and Re-throw pending exception
	 */
	void ehEnd() {
		ValueClass pendingException = exceptionHandler.getPendingException();
		discardCallFrame(); // Discard CallFrame created in 'ehcreate'
		exceptionHandler = callFrames[fp].exceptionHandler; // Restore exception handler from previous CallFrame
		if (pendingException != null) throwException(pendingException); // Rethrow pending exception
	}

	void ehfStart() {
		exceptionHandler.finallyStart();
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

	void fatalError(String msg) {
		bdsThread.fatalError(getBdsNode(), msg);
	}

	/**
	 * Get a list of all bds nodes in this VM program
	 */
	public List<BdsNode> findNodes() {
		List<BdsNode> bdsNodes = new ArrayList<>();
		for (int pc = 0; pc < code.length; pc++) {
			OpCode op = OPCODES[code[pc]];
			if (op == OpCode.NODE || op == OpCode.NODE_COVERAGE) {
				int idx = code[pc + 1];
				BdsNode bdsNode = BdsNodeFactory.get().getNode(idx);
				bdsNodes.add(bdsNode);
			}
			if (hasParam(pc)) pc++;
		}
		return bdsNodes;
	}

	/**
	 * Get arguments from scope for a function / method call
	 * Note: Remember that stack in reverse order
	 *
	 * @return List of arguments. First argument corresponds to 'this' in a method call
	 */
	Value[] getArgsFromStack(FunctionDeclaration fdecl) {
		List<String> args = fdecl.getParameterNames();
		Value[] values = new Value[args.size()];
		for (int i = args.size() - 1; i >= 0; i--) {
			Value val = pop();
			values[i] = val;
		}
		return values;
	}

	/**
	 * Get node currently executed by the VM
	 */
	public BdsNode getBdsNode() {
		return BdsNodeFactory.get().getNode(nodeId);
	}

	public BdsThread getBdsThread() {
		return bdsThread;
	}

	Object getConstant(int idx) {
		return constants.get(idx);
	}

	public Map<Integer, Integer> getCoverageCounter() {
		return coverageCounter;
	}

	public ValueClass getExceptionValue() {
		return exceptionValue;
	}

	public int getExitCode() {
		return exitCode;
	}

	public Throwable getJavaException() {
		return javaException;
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

	public int getPc() {
		return pc;
	}

	RunState getRunState() {
		return bdsThread != null ? bdsThread.getRunState() : RunState.OK;
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
	 * Implements goal OpCode
	 */
	void goal() {
		Value vgoal = pop();

		// Return a list of all taskIds to execute
		ValueList taskIds;

		if (vgoal.getType().isList()) {
			// List of values: Execute each goal
			taskIds = new ValueList(TypeList.get(Types.STRING));
			ValueList vlist = (ValueList) vgoal;
			// Is it a list? Add goal for each item
			for (Value v : vlist) {
				taskIds.addAll(bdsThread.goal(v.asString()));
			}
		} else {
			// Single value: execute one goal
			taskIds = bdsThread.goal(vgoal.asString());
		}

		push(taskIds);
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

	public boolean isRecoveredCheckpoint() {
		return recoveredCheckpoint;
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
		BdsNode bdsNode = BdsNodeFactory.get().getNode(nodeId);
		scope = new Scope(scope, bdsNode);
	}

	/**
	 * Create a 'parallel' vm-thread
	 */
	private BdsVm parallel(int pushCount) {
		BdsVm vmclone = new BdsVm();

		vmclone.code = code;
		vmclone.debug = debug;
		vmclone.nodeId = nodeId;
		vmclone.run = run;
		vmclone.verbose = verbose;
		vmclone.labels = labels;
		vmclone.labelsByPc = labelsByPc;
		vmclone.constantsByObject = constantsByObject;
		vmclone.functionsBySignature = functionsBySignature;
		vmclone.typeToIndex = typeToIndex;
		vmclone.constants = constants;
		vmclone.types = types;

		// Child process
		// vmclone.callFrame : Initialized, because child process has a new frame
		// vmclone.fp        : Initialized to zero because child process has a new frame
		// vmclone.bdsThread : Will be set by 'parallel' bdsThread
		vmclone.pc = pc; // Already pointing to instruction after 'parallel'
		vmclone.scope = scope; // Same scope

		// Push 'pushCount' values to new VM (from current VM)
		if (pushCount > 0) {
			Value[] vals = new Value[pushCount];

			// Pop values
			for (int i = 0; i < pushCount; i++)
				vals[i] = pop();

			// Push values: stack must be in same order
			// Note: We clone all primitive values. This saves us
			//       from some race conditions issues
			for (int i = pushCount - 1; i >= 0; i--) {
				Value v = vals[i];
				if (v.getType().isPrimitive()) v = v.clone();
				vmclone.push(v);
			}
		}

		return vmclone;
	}

	/**
	 * Execute a 'parallel' opcode
	 */
	void parallelOpCode(int pushCount) {
		BdsVm vmpar = parallel(pushCount);
		bdsThread.parallel(vmpar);
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
		Value v = pop();
		return v != null ? v.asBool() : false;
	}

	/**
	 * Restore from call frame
	 */
	void popCallFrame() {
		setFromCallFrame(callFrames[--fp]);
		// Note: Should we free space in previous call frame (scope, exceptionHandler)?
	}

	/**
	 * Pop an int from stack
	 */
	public long popInt() {
		Value v = pop();
		return v != null ? v.asInt() : 0;
	}

	/**
	 * Pop a real from stack
	 */
	public double popReal() {
		Value v = pop();
		return v != null ? v.asReal() : 0.0;
	}

	/**
	 * Restore old scope
	 */
	public void popScope() {
		// Old scope should be set to null to free memory?
		scope = scope.getParent();
	}

	/**
	 * Pop a string from stack
	 */
	public String popString() {
		Value v = pop();
		return v != null ? v.asString() : "null";
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
		if (fp >= callFrames.length) throw new RuntimeException("Out of stack memory! Call frame pointer: " + fp);
		CallFrame sf = callFrames[fp++];
		sf.set(pc, nodeId, scope, exceptionHandler);
		exceptionHandler = null;
	}

	/**
	 * Resolve method call
	 * Note: For non-class types, there is nothing to resolve
	 */
	public FunctionDeclaration resolve(FunctionDeclaration fdecl) {
		return fdecl;
	}

	/**
	 * Resolve 'virtual' method call
	 * @param vthis : 'this' Value
	 * @param fdecl : Method declaration (to be resolved)
	 * @param isSuper : Is this a 'super' method call?
	 * @param fsig : Method's signature string
	 * @return The resolved method declaration
	 */
	FunctionDeclaration resolveVirtualMethod(Value vthis, FunctionDeclaration fdecl, boolean isSuper, String fsig) {
		if (vthis == null) throw new RuntimeException("Null pointer: Cannot call method '" + fsig + "' on null object.");
		if (!isSuper) return vthis.getType().resolve(fdecl);

		//---
		// This is a 'super.f()' method call
		//---

		// Get method's declaration class
		MethodDeclaration methodDecl = (MethodDeclaration) fdecl;
		TypeClass typeClass = (TypeClass) methodDecl.getClassType();
		ClassDeclaration classDecl = typeClass.getClassDeclaration();

		// Get 'super' class
		ClassDeclaration superClassDecl = classDecl.getClassParent();
		if (superClassDecl == null) return fdecl; // No super class? Nothing to resolve. This can happen on "super.Constructor()" calls
		FunctionDeclaration superMethodDecl = superClassDecl.getType().resolve(fdecl);

		if (superMethodDecl == null) throw new RuntimeException("Null pointer: Cannot resolve 'super' method '" + fsig + "'.");
		return superMethodDecl;
	}

	/**
	 * Run the program in 'code'
	 */
	public int run() {
		// Initialize program counter
		// Note: If vm parallel, then pc is already initialized in child
		//       process, do not change.
		if (pc == 0) pc = Math.max(0, getLabel(LABEL_MAIN));

		run = true;
		try {
			while (run || Freeze.isFreeze()) {
				// If we are in 'freeze' mode, perform a busy wait
				if (Freeze.isFreeze()) {
					sleepFreeze();
				} else {
					vmStateRecover(); // Is this recovering from an interrupted long running operation?
					runLoop(); // Run main loop (instruction processing)
				}
			}
		} catch (Throwable t) {
			if (Config.get().isVerbose()) {
				System.err.println("Fatal error running BdsThread " + bdsThread.getBdsThreadId() + "\n");
				t.printStackTrace();
			}

			exitCode = BdsThread.EXITCODE_FATAL_ERROR;
			javaException = t;
			bdsThread.fatalError(getBdsNode(), t.getMessage());
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

		// In case of recovered checkpoints, the task ID could be repeated (e.g. recovering checkpoints several times)
		// We append PID to avoid file name collision
		boolean usePidInFileNames = recoveredCheckpoint;

		// Execute while not the end of the program
		while (pc < code.length && run) {
			instruction = code[pc];
			opcode = OPCODES[instruction];
			if (debug) {
				String msg = "" //
						+ (sp > 0 ? "\n\t\t\t\t\t\t\t\t# stack: " + toStringStack() : "") //
						+ (exceptionHandler != null ? "\n\t\t\t\t\t\t\t\t# exceptionHandler: " + exceptionHandler.getFinallyLabel() : "") //
						+ (fp > 0 ? "\n\t\t\t\t\t\t\t\t# call stack: " + toStringCallStack() : "") //
						+ "\n" //
						+ (bdsThread != null ? bdsThread.getBdsThreadId() + "\t\t|" : "") //
						+ toAsm(pc) //
				;
				System.err.print(msg);
			}
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

			case ADDSM:
				adds(paramInt());
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

			case BREAKPOINT:
				s1 = popString();
				if (vmDebugger != null) vmDebugger.breakpoint(s1);
				break;

			case CALL:
				name = constantString(); // Get function signature
				call(name);
				break;

			case CALLMETHOD:
				name = constantString(); // Get method signature
				callMethod(name, false);
				break;

			case CALLNATIVE:
				vmStateSave();
				name = constantString(); // Get signature
				v1 = callNative(name);
				vmStateInvalidate();
				push(v1);
				break;

			case CALLSUPER:
				name = constantString(); // Get method signature
				callMethod(name, true);
				break;

			case CAST_TOB:
				push(pop().asBool());
				break;

			case CAST_TOC: // Cast object to class (only if up-casting)
				name = constantString();
				castToClass(name);
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

			case CHECKPOINT:
				s1 = popString(); // File name (may be empty)
				bdsThread.checkpoint(s1, getBdsNode());
				break;

			case CHECKPOINTVM:
				s1 = popString(); // File name (may be empty)
				s1 = bdsThread.checkpointVm(s1, getBdsNode()); // Return checkpoint file name
				push(s1); // Push checkpoint file name to stack
				break;

			case CHECKPOINT_RECOVERED:
				// Checkpoint recovered: Push true to the stack ONLY if both conditions are satisfied:
				//   a) this VM was recovered from a checkpoint
				//   b) this is the first time we check
				// Important: Only true the first time we check
				push(recoveredCheckpointOp);
				recoveredCheckpointOp = false;
				break;

			case DEBUG:
				debug();
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

			case EHADD:
				ehAdd(constantString(), popString(), popString());
				break;

			case EHCREATE:
				ehCreate(constantString());
				break;

			case EHEND:
				ehEnd();
				break;

			case EHCSTART:
				ehcStart();
				break;

			case EHFSTART:
				ehfStart();
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
				bdsThread.fatalError(popString());
				exitCode = BdsThread.EXITCODE_ERROR;
				return;

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

			case GOAL:
				goal();
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
				pc--; // Next instruction is this same 'halt'. Used when recovering from a checkpoint.
				run = false;
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

			case JSR:
				name = constantString(); // Get label name
				pushCallFrame();
				pc = getLabel(name);
				break;

			case KILL:
				kill();
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

			case LOAD:
				name = constantString();
				push(scope.getValue(name));
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
				if (vmDebugger != null) vmDebugger.node();
				break;

			case NODE_COVERAGE:
				nodeId = paramInt();
				coverageCounter.put(nodeId, coverageCounter.getOrDefault(nodeId, 0) + 1); // Increment node counter
				if (vmDebugger != null) vmDebugger.node();
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

			case PARALLEL:
				parallelOpCode(0);
				break;

			case PARALLELPUSH:
				i1 = popInt();
				parallelOpCode((int) i1);
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

			case PUSHNULL:
				push((Value) null);
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
				if (vclass != null) {
					val = vclass.getValue(name);
					push(val);
				} else {
					fatalError("Null pointer. Trying to access field '" + name + "' in null object.");
				}
				break;

			case REFLIST:
				vlist = (ValueList) pop();
				idx = popInt();
				if (vlist != null) {
					val = vlist.getValue(idx);
					push(val);
				} else {
					fatalError("Null pointer. Trying to access item " + idx + " in null list.");
				}
				break;

			case REFMAP:
				vmap = (ValueMap) pop();
				v1 = pop();
				if (vmap != null) {
					val = vmap.getValue(v1);
					push(val);
				} else {
					fatalError("Null pointer. Trying to access item '" + v1 + "' in null map.");
				}
				break;

			case RET:
				popCallFrame();
				break;

			case RMONEXIT:
				v1 = pop();
				bdsThread.rmOnExit(v1);
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

			case SETPOP:
				v1 = pop();
				v2 = pop();
				v1.setValue(v2);
				break;

			case SETFIELD:
				name = constantString();
				vclass = (ValueClass) pop();
				vclass.setValue(name, peek()); // We leave the value in the stack
				break;

			case SETFIELDPOP:
				name = constantString();
				vclass = (ValueClass) pop();
				vclass.setValue(name, pop());
				break;

			case SETLIST:
				vlist = (ValueList) pop();
				idx = popInt();
				vlist.setValue(idx, peek()); // We leave the value in the stack
				break;

			case SETLISTPOP:
				vlist = (ValueList) pop();
				idx = popInt();
				vlist.setValue(idx, pop());
				break;

			case SETMAP:
				vmap = (ValueMap) pop();
				v1 = pop(); // Key
				vmap.put(v1, peek()); // We leave the value in the stack
				break;

			case SETMAPPOP:
				vmap = (ValueMap) pop();
				v1 = pop(); // Key
				vmap.put(v1, pop());
				break;

			case STORE:
				name = constantString();
				scope.setValue(name, peek()); // We leave the value in the stack
				break;

			case STOREPOP:
				name = constantString();
				scope.setValue(name, pop());
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
				vmStateSave();
				SysVmOpcode sf = new SysVmOpcode(bdsThread, usePidInFileNames);
				s1 = sf.run();
				vmStateInvalidate();
				push(s1);
				break;

			case SWAP:
				v2 = pop();
				v1 = pop();
				push(v2);
				push(v1);
				break;

			case TASK:
				TaskVmOpcode taskVmOp = new TaskVmOpcode(bdsThread, usePidInFileNames);
				s1 = taskVmOp.run();
				push(s1);
				break;

			case TASKDEP:
				TaskVmOpcode depVmOp = new DepVmOpcode(bdsThread, usePidInFileNames);
				s1 = depVmOp.run();
				push(s1);
				break;

			case TASKDEPIMP:
				DepImpVmOpcode depImpVmOp = new DepImpVmOpcode(bdsThread, usePidInFileNames);
				s1 = depImpVmOp.run();
				push(s1);
				break;

			case TASKIMP:
				TaskImpVmOpcode taskImpVmOp = new TaskImpVmOpcode(bdsThread, usePidInFileNames);
				s1 = taskImpVmOp.run();
				push(s1);
				break;

			case THROW:
				throwException((ValueClass) pop()); // Get Exception object to throw
				break;

			case VAR:
				name = constantString();
				scope.add(name, peek()); // We leave the value in the stack
				break;

			case VARPOP:
				name = constantString();
				scope.add(name, pop());
				break;

			case WAIT:
				vmStateSave();
				ValueList tids = (ValueList) pop();
				b1 = bdsThread.wait(tids);
				vmStateInvalidate();
				push(b1);
				break;

			case WAITALL:
				vmStateSave();
				b1 = bdsThread.waitAll();
				vmStateInvalidate();
				push(b1);
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

		// Finished running code? We are done
		run = false;
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

	public void setExitCode(Integer exitCode) {
		this.exitCode = exitCode;
	}

	void setFromCallFrame(CallFrame callFrame) {
		pc = callFrame.pc;
		nodeId = callFrame.nodeId;
		scope = callFrame.scope;
		exceptionHandler = callFrame.exceptionHandler;
	}

	public void setRecoveredCheckpoint(boolean recoveredCheckpoint) {
		this.recoveredCheckpoint = recoveredCheckpoint;
		recoveredCheckpointOp = recoveredCheckpoint;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	void sleepFreeze() {
		try {
			Thread.sleep(SLEEP_TIME_FREEZE);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Show stack trace
	 */
	public String stackTrace() {
		StringBuilder sb = new StringBuilder();

		// List of nodes
		List<Integer> nodes = new ArrayList<>();
		for (int i = 0; i < fp; i++) {
			CallFrame cf = callFrames[i];
			nodes.add(cf.getNodeId());
		}
		nodes.add(nodeId);

		// File names and line numbers
		int maxLen = 0;
		List<String> fileLines = new ArrayList<>();
		for (int nid : nodes) {
			String fl = stackTraceFileLine(nid);
			fileLines.add(fl);
			int len = fl.length();
			maxLen = Math.max(maxLen, len);
		}

		// Show stack trace
		for (int i = 0; i < fp; i++) {
			CallFrame cf = callFrames[i];
			sb.append(stackTrace(cf.getNodeId(), fileLines.get(i), maxLen) + "\n");
		}

		return sb.toString();
	}

	/**
	 * Show stack trace information on node bdsNode
	 */
	String stackTrace(int nodeId, String fileLine, int maxFileLineLen) {
		BdsNode bdsNode = BdsNodeFactory.get().getNode(nodeId);

		if (bdsNode == null) return "";

		String lines[] = bdsNode.toString().split("\n");
		String line = lines[0];
		String format = "%-" + maxFileLineLen + "s: \t%s";
		return String.format(format, fileLine, line);
	}

	/**
	 * Stack trace file name and line number
	 * @param nodeId
	 * @return
	 */
	String stackTraceFileLine(int nodeId) {
		BdsNode bdsNode = BdsNodeFactory.get().getNode(nodeId);
		if (bdsNode == null) return "";
		return bdsNode.getFileName() + ":" + bdsNode.getLineNum();
	}

	/**
	 * Implement 'throw' opcode
	 */
	void throwException(ValueClass exceptionValue) {
		this.exceptionValue = exceptionValue;

		// Populate Exception's stack trace message, if empty
		if (exceptionValue.getValue("stackTrace") == null) {
			exceptionValue.setValue("stackTrace", new ValueString(stackTrace()));
		}

		// Use current exception handler if available
		if (exceptionHandler != null) {
			catchException(exceptionValue);
			return;
		}

		// Look for an exceptionHandler
		while (canPopFrame()) {
			popCallFrame();
			if (exceptionHandler != null) {
				catchException(exceptionValue);
				return;
			}
		}

		// No Exception handler was found
		fatalError(exceptionValue.getType() + " thrown: " + exceptionValue);
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
		sb.append(String.format("%6d    %s", pc, opstr));

		// Parameter?
		String comment = null;
		if (op.hasParam()) {
			String param = null;

			int idx = code[++pc];
			if (op.isParamString()) param = "'" + GprString.escape(getConstant(idx).toString()) + "'";
			else if (op.isParamType()) param = "'" + getType(idx) + "'";
			else if (op == OpCode.NODE || op == OpCode.NODE_COVERAGE) {
				// Show some code for this node
				comment = toStringNode(idx);
				param = "" + idx;
			} else if (op.isParamDirect()) param = "" + idx;
			else param = getConstant(idx).toString();

			sb.append(" " + param);
		}
		if (comment != null) sb.append("\n# " + comment);

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("VM:\n");
		sb.append("  RunState          : " + getRunState() + "\n");
		sb.append("  pc                : " + pc + "\n");
		sb.append("  fp                : " + fp + "\n");
		sb.append("  sp                : " + sp + "\n");
		sb.append("  Stack             : " + toStringStack() + "\t\n");
		sb.append("  Call-Stack        : [");
		for (int i = 0; i < fp; i++)
			sb.append(" " + callFrames[i]);
		sb.append(" ]\n");
		if (exceptionHandler != null) sb.append("  Exception handler :\n" + Gpr.prependEachLine("      ", exceptionHandler.toString()));
		sb.append("  Scope:\n" + scope);
		return sb.toString();
	}

	String toStringCallStack() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fp; i++) {
			CallFrame cf = callFrames[i];
			sb.append((i > 0 ? ", " : "") + cf);
		}
		return sb.toString();
	}

	String toStringNode(int nodeId) {
		int maxLen = 40;
		BdsNode bdsNode = BdsNodeFactory.get().getNode(nodeId);
		String s = bdsNode.toString();
		s = s.split("\n")[0];
		if (s.length() > maxLen) s = s.substring(0, maxLen) + "...";
		return bdsNode.getClass().getSimpleName() + " : " + s;
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

	/**
	 * Update function descriptor's PC
	 */
	public void updateFunctionPc(String signature, int pc) {
		FunctionDeclaration fdecl = functionsBySignature.get(signature);
		if (fdecl == null) {
			// Function declaration not found? Try to create one form signature
			fdecl = new FunctionDeclaration(signature);
			addFunction(fdecl);
		}

		addLabel(signature, pc);
		fdecl.setPc(pc);
	}

	/**
	 * Invalidate saved vm state after long running opcode finished.
	 */
	void vmStateInvalidate() {
		vmState.reset();
	}

	/**
	 * Recover state from long running opcode (e.g. checkpoint during 'wait' statement)
	 */
	void vmStateRecover() {
		if (vmState.isValid()) {
			fp = vmState.fp;
			nodeId = vmState.nodeId;
			pc = vmState.pc;
			sp = vmState.sp;
			scope = vmState.scope;
		}
		vmState.reset(); // Make sure we don't recover state again
	}

	/**
	 * Save vm state before long running opcode starts running
	 */
	void vmStateSave() {
		// Save VM state variables
		// Note: Unwind pc for the current opcode
		vmState.set(fp, nodeId, pc - 1, sp, scope);
	}

}
