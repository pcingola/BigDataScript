package org.bds.vm;

public enum OpCode {
	ADDI // Addition (int)
	, ADDR // Addition (real) 
	, ADDS // Addition (string): Concatenate 
	, ANDB // And (bool)
	, ANDI // And (int)
	, BTOI // bool to int
	, BTOR // bool to real
	, CALL // Call a function
	, DIVI // Division (int)
	, DIVR // Division (real)
	, EQB // Equality (bool)
	, EQI // Equality (int)
	, EQR // Equality (real)
	, EQS // Equality (string)
	, GEB // Greater or equal than (bool)
	, GEI // Greater or equal than (int)
	, GER // Greater or equal than (real)
	, GES // Greater or equal than (string)
	, GTB // Greater than (bool)
	, GTI // Greater than (int)
	, GTR // Greater than (real)
	, GTS // Greater than (string)
	, HALT // Halt (stop execution in current thread)
	, ITOB // int to bool
	, ITOR // int to real
	, JMP // Jump (inconditional)
	, JMPT // Jump if true
	, JMPF // Jump if false
	, LOAD // Load from local scope
	, LTB // Less than (bool)
	, LTI // Less than (int)
	, LTR // Less than (real)
	, LTS // Less than (string)
	, LEB // Less or equal than (bool)
	, LEI // Less or equal than (int)
	, LER // Less or equal than (real)
	, LES // Less or equal than (string)
	, MODI // Modulo (int)
	, MULI // Multiplication (int)
	, MULR // Multiplication (real)
	, NOP // No operation
	, ORB // Or (bool)
	, ORI // Or (int)
	, PUSHB // Push literal (bool)
	, PUSHI // Push literal (int)
	, PUSHR // Push literal (real)
	, PUSHS // Push literal (string)
	, RET // Return (from function)
	, RTOB // real to bool
	, RTOI // real to int
	, STORE // Store in local scope
	, SUBI // Subtraction (int) 
	, SUBR // Subtraction (real)
	, XORB // Xor (bool)
	, XORI // Xor (int)

}
