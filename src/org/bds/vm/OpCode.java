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
	// Cast values {b,i,r} => {b,i,r}
	, CAST_TOB, CAST_TOI, CAST_TOR, CAST_TOS
	// Create a checkpoint
	, CHECKPOINT
	// Function call:
	//    CALL function_signature
	//    CALLNATIVE function_signature
	//    CALLMETHOD method_signature
	, CALL, CALLMETHOD, CALLNATIVE
	// Debug breakpoint
	, DEBUG
	// Decrement (i.e. valueInt--)
	, DEC
	// Dependency operator
	, DEP
	// Division
	, DIVI, DIVR
	// Duplicate value on stack
	, DUP
	// Equality test
	, EQB, EQI, EQR, EQS
	// Error
	, ERROR
	// Create another thread ('forkpush' pushes 'n' values into the new stack)
	, FORK, FORKPUSH
	// Greater or equal than
	, GEB, GEI, GER, GES
	// Goal
	, GOAL
	// Greater than
	, GTB, GTI, GTR, GTS
	// Halt (stop execution in current thread)
	, HALT
	// Kill a task
	, KILL
	// Increment (i.e. valueInt++)
	, INC
	// Jumps: unconditional, jump if true, jump if false:
	//    JMP[T|F]    pc
	, JMP, JMPT, JMPF
	// Load variable from scope into stack
	//    LOAD varName
	, LOAD
	// Less than
	, LTB, LTI, LTR, LTS
	// Less or equal than
	, LEB, LEI, LER, LES
	// Modulo (int)
	, MODI
	// Multiplication (int)
	, MULI, MULR, MULS
	// Set current BdsNode number. Used for references to bds code (debugging, stack trace, etc.)
	, NODE
	// No operation
	, NOOP
	// Equality test (not equals)
	, NEB, NEI, NER, NES
	// Create new object (of type 'Type') and push it to the stack
	//    NEW Type
	, NEW
	// Negation
	, NOTB, NOTI
	// OR: bool (logical), int (bitwise)
	, ORB, ORI
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
	// Scope: create new scope (and push it), restore old scope (pop current scope)
	, SCOPEPUSH, SCOPEPOP
	// Set value
	, SET, SETFIELD, SETLIST, SETMAP // Leave value in the stack
	, SETPOP, SETFIELDPOP, SETLISTPOP, SETMAPPOP // Remove value from stack
	// Store value to local variable (scope). Leaves the value in the stack (stack is not changed)
	//    STORE varName
	, STORE, STOREPOP
	// Subtraction
	, SUBI, SUBR
	// Sys command
	, SYS
	// Swap two values in stack
	, SWAP
	// Dispatch a task
	, TASK, TASKDEP
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
		case JMP:
		case JMPT:
		case JMPF:
		case LOAD:
		case NEW:
		case NODE:
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
		return this == NODE || this == OpCode.ADDSM;
	}

	/**
	 * Is the parameter a string?
	 */
	public boolean isParamString() {
		switch (this) {
		case CALL:
		case CALLMETHOD:
		case CALLNATIVE:
		case JMP:
		case JMPT:
		case JMPF:
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
		case JMP:
		case JMPT:
		case JMPF:
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
