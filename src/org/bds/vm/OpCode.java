package org.bds.vm;

/**
 * The opcodes are stored into an array of Integers
 *
 * Literal always reference the pool of constants: bool, int, real, string or function
 *
 * Nomenclature:
 *     literal  : An integer referencing a literal (bool|int|real|string) in the pool of constants
 *     funcName : An integer referencing a string (the function's name) in the pool of constants
 *     varName  : An integer referencing a string (the variable's name) in the pool of constants
 *
 * @author pcingola
 */
public enum OpCode {
	// Addition (int, real, string)
	ADDI, ADDR, ADDS
	// And: bool (logic), int (bitwise)
	, ANDB, ANDI
	// Cast values {b,i,r} => {b,i,r}
	, CAST_BTOI, CAST_BTOR, CAST_ITOB, CAST_ITOR, CAST_RTOB, CAST_RTOI
	// Function call:
	//    CALL funcName
	, CALL
	// Division
	, DIVI, DIVR
	// Equality test
	, EQB, EQI, EQR
	// Greater or equal than
	, GEB, GEI, GER, GES
	// Greater than
	, GTB, GTI, GTR
	// Halt (stop execution in current thread)
	, HALT
	// Jumps: unconditional, jump if true, jump if false: 'JMP pc'
	, JMP, JMPT, JMPF
	// Load variable/array/dictionary local scope (into stack)
	//    LOAD varName
	//    LOADIDX varListName     # Use latest stack element as index
	//    LOADDICT varDictName    # Use latest stack element as key
	, LOAD, LOADIDX, LOADDICT
	// Less than
	, LTB, LTI, LTR, LTS
	// Less or equal than
	, LEB, LEI, LER, LES
	// Modulo (int)
	, MODI
	// Multiplication (int)
	, MULI, MULR
	// No operation
	, NOP
	// OR: bool (logical), int (bitwise)
	, ORB, ORI
	// Push literal
	//    PUSHNULL                 # Pushes a 'null' literal
	//    PUSH{B|I|R|S}  literal   # Pushes a literal constant into the stack
	, PUSHB, PUSHI, PUSHNULL, PUSHR, PUSHS
	// Return (from function)
	, RET
	// Store variable/array/dictionary in local scope
	//    STORE varName
	//    STOREIDX varListName     # Use latest stack element as index
	//    STOREDICT varDictName    # Use latest stack element as key
	, STORE, STOREIDX, STOREDICT
	// Subtraction
	, SUBI, SUBR
	// XOR
	, XORB, XORI
}
