package org.bds.vm;

import java.util.Map;

import org.bds.lang.type.Type;
import org.bds.util.Gpr;
import org.bds.util.GprString;

/**
 * The opcodes are stored into an array of Integers
 *
 * This is a stack machine, so operands act on the element/s of the stack
 * e.g. ADDI    # pop the last two items in the stack, add them and push the result to the stack
 *
 * Notes:
 *     Literal always reference the pool of constants: bool, int, real, string or function
 *
 * Nomenclature:
 *     funcName : An integer referencing a string (the function's name) in the pool of constants
 *     literal  : An integer referencing a literal (bool|int|real|string) in the pool of constants
 *     pc       : Program counter (new position to jump to)
 *     varName  : An integer referencing a string (the variable's name) in the pool of constants
 *
 * @author pcingola
 */
public enum OpCode {
	// Addition (int, real, string, string multiple)
	ADDI, ADDR, ADDS, ADDSM
	// And: bool (logic), int (bitwise)
	, ANDB, ANDI
	// Breakpoint (debugging mode)
	, BREAKPOINT
	// Function call:
	//    CALL function_signature
	//    CALLNATIVE function_signature
	//    CALLMETHOD method_signature
	//    CALLSUPER method_signature
	, CALL, CALLMETHOD, CALLNATIVE, CALLSUPER
	// Cast values: bool, real, int, string
	, CAST_TOB, CAST_TOI, CAST_TOR, CAST_TOS
	// Cast to class
	, CAST_TOC
	// Checkpoint
	, CHECKPOINT, CHECKPOINTVM
	// Checkpoint recovered: Push bool to the stack if this is the first instruction after a checkpoint was recovered.
	, CHECKPOINT_RECOVERED
	// Debug breakpoint
	, DEBUG
	// Decrement (i.e. valueInt--)
	, DEC
	// Dependency operator
	, DEP
	// Division: integer, real
	, DIVI, DIVR
	// Duplicate value on stack
	, DUP
	// Add to Exception Handler (parameter string 'Exception handler label (catch entry)')
	, EHADD
	// Create new exception handler
	, EHCREATE
	// Finish exception handling
	, EHEND
	// Start exception handler (catch or finally block)
	, EHCSTART, EHFSTART
	// Equality test
	, EQB, EQI, EQR, EQS
	// Error
	, ERROR
	// Greater or equal than
	, GEB, GEI, GER, GES
	// Goal
	, GOAL
	// Greater than
	, GTB, GTI, GTR, GTS
	// Halt (stop execution in current thread)
	, HALT
	// Kill a task
	, INC
	// Increment (i.e. valueInt++)
	, JMP
	// Jumps: unconditional, jump if true, jump if false:
	//    JMP[T|F]    pc
	, JMPF, JMPT, JSR
	// Jump to Sub-Routine (unconditional)
	, KILL
	// Less than (bool, int, real, string)
	, LEB, LEI, LER, LES
	// Load variable from scope into stack
	//    LOAD varName
	, LOAD
	// Less or equal than
	, LTB, LTI, LTR, LTS
	// Modulo (int)
	, MODI
	// Multiplication (int)
	, MULI, MULR, MULS
	// Equality test (not equals)
	, NEB, NEI, NER, NES
	// Create new object (of type 'Type') and push it to the stack
	//    NEW Type
	, NEW
	// Set current BdsNode number. Used for references to bds code (debugging, stack trace, etc.)
	, NODE, NODE_COVERAGE
	// No operation (do nothing)
	, NOOP
	// Negation
	, NOTB, NOTI
	// OR: bool (logical), int (bitwise)
	, ORB, ORI
	// Create another thread ('parallelpush' pushes 'n' values into the new stack)
	, PARALLEL, PARALLELPUSH
	// Pop: remove latest element from stack
	, POP
	// Print stdout & stderr
	, PRINT, PRINTLN, PRINTSTDERR, PRINTSTDERRLN
	// Push literal
	//    PUSHNULL                 # Pushes a 'null' literal
	//    PUSH{B|I|R|S}  literal   # Pushes a literal constant into the stack
	, PUSHB, PUSHI, PUSHNULL, PUSHR, PUSHS
	// Reference: object's field, list index or hash key
	, REFFIELD, REFLIST, REFMAP//
	// Return (from function)
	, RET
	// Remove file on exit
	, RMONEXIT
	// Scope: create new scope (and push it), restore old scope (pop current scope)
	, SCOPEPOP, SCOPEPUSH
	// Set value
	, SET, SETFIELD, SETFIELDPOP, SETLIST, SETLISTPOP, SETMAP
	// Leave value in the stack
	, SETMAPPOP
	// Remove value from stack
	, SETPOP
	// Store value to local variable (scope). Leaves the value in the stack (stack is not changed)
	//    STORE varName
	, STORE, STOREPOP
	// Subtraction
	, SUBI, SUBR
	// Swap two values in stack
	, SWAP
	// Sys command
	, SYS
	// Dispatch a task
	, TASK, TASKDEP, TASKDEPIMP, TASKIMP
	// Throw an Exception
	, THROW
	// Create a variable in local scope (and pop)
	, VAR, VARPOP
	// Wait for task to finish
	, WAIT, WAITALL
	// XOR
	, XORB, XORI
	//
	;

	Type getType(String typeName, Map<String, Type> typeByName) {
		if (!typeByName.containsKey(typeName)) throw new RuntimeException("Cannot find type '" + typeName + "'");
		return typeByName.get(typeName);
	}

	/**
	 * Number of parameters associated with this operand
	 */
	public boolean hasParam() {
		switch (this) {
		case ADDSM:
		case CALL:
		case CALLMETHOD:
		case CALLNATIVE:
		case CALLSUPER:
		case CAST_TOC:
		case EHADD:
		case EHCREATE:
		case JMP:
		case JMPT:
		case JMPF:
		case JSR:
		case LOAD:
		case NEW:
		case NODE:
		case NODE_COVERAGE:
		case PUSHB:
		case PUSHI:
		case PUSHR:
		case PUSHS:
		case REFFIELD:
		case SETFIELD:
		case SETFIELDPOP:
		case STORE:
		case STOREPOP:
		case VAR:
		case VARPOP:
			return true;

		default:
			return false;
		}
	}

	/**
	 * Is the parameters a 'nodeId'?
	 * Note that node ID is a 32bit int encoded encoded directly in
	 * the opcode parameter (as opposed to using poll of constants)
	 */
	public boolean isParamDirect() {
		return this == NODE || this == NODE_COVERAGE || this == ADDSM;
	}

	/**
	 * Is the parameter a string?
	 */
	public boolean isParamString() {
		switch (this) {
		case CALL:
		case CALLMETHOD:
		case CALLNATIVE:
		case CALLSUPER:
		case CAST_TOC:
		case EHADD:
		case EHCREATE:
		case JMP:
		case JMPT:
		case JMPF:
		case JSR:
		case LOAD:
		case PUSHS:
		case REFFIELD:
		case SETFIELD:
		case SETFIELDPOP:
		case STORE:
		case STOREPOP:
		case VAR:
		case VARPOP:
			return true;

		default:
			return false;
		}
	}

	/**
	 * Is the parameters a 'type'?
	 */
	public boolean isParamType() {
		return this == NEW;
	}

	/**
	 * Convert parameter string to appropriate constant type
	 */
	public Object parseParam(String param, Map<String, Type> typeByName) {
		switch (this) {
		case CALL:
		case CALLMETHOD:
		case CALLNATIVE:
		case CALLSUPER:
		case CAST_TOC:
		case EHADD:
		case EHCREATE:
		case JMP:
		case JMPT:
		case JMPF:
		case JSR:
		case LOAD:
		case PUSHS:
		case REFFIELD:
		case SETFIELD:
		case SETFIELDPOP:
		case STORE:
		case STOREPOP:
		case VAR:
		case VARPOP:
			return parseParamString(param);

		case NEW:
			return getType(param, typeByName);

		case ADDSM:
		case NODE:
		case NODE_COVERAGE:
			return Gpr.parseIntSafe(param);

		case PUSHB:
			return Gpr.parseBoolSafe(param);

		case PUSHI:
			return Gpr.parseLongSafe(param);

		case PUSHR:
			return Gpr.parseDoubleSafe(param);

		default:
			throw new RuntimeException("Unknown parameter type for opcode '" + this + "'");
		}
	}

	/**
	 * Parse literal string
	 */
	String parseParamString(String param) {
		int lastCharIdx = param.length() - 1;
		if ((param.charAt(0) == '\'' && param.charAt(lastCharIdx) == '\'') // Using single quotes
				|| (param.charAt(0) == '"' && param.charAt(lastCharIdx) == '"')) // Using double quotes
		{
			String escapedStr = param.substring(1, param.length() - 1); // Remove quotes
			return GprString.unescape(escapedStr);
		}
		return param; // Unquoted string
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
