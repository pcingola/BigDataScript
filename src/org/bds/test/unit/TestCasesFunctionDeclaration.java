package org.bds.test.unit;

import org.bds.lang.Parameters;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.TypeMap;
import org.bds.lang.type.Types;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesFunctionDeclaration extends TestCasesBase {

	@Test
	public void test_function_declaration_01() {
		Gpr.debug("Test");

		Types.reset();

		String signature = "f(int x) -> int";
		FunctionDeclaration fd = new FunctionDeclaration(signature);

		// Check 
		Assert.assertEquals("Function name", "f", fd.getFunctionName());
		Assert.assertEquals("Return type", "int", fd.getReturnType().toString());
		Assert.assertEquals("Number of parameters", 1, fd.getParameterNames().size());
		Assert.assertEquals("Parameter name", "x", fd.getParameterNames().get(0));
		Assert.assertEquals("Signature", signature, fd.signatureVarNames());
	}

	@Test
	public void test_function_declaration_02() {
		Gpr.debug("Test");

		Types.reset();

		String signature = "function_name_is_long(int paramx, real param2,string s3, string slist) -> bool";
		FunctionDeclaration fd = new FunctionDeclaration(signature);

		// Check 
		Assert.assertEquals("Function name", "function_name_is_long", fd.getFunctionName());
		Assert.assertEquals("Return type", "bool", fd.getReturnType().toString());

		Parameters params = fd.getParameters();
		Assert.assertEquals("Number of parameters", 4, params.size());

		int i = 0;
		Assert.assertEquals("Parameter " + i + " type ", "int", params.getType(i).toString());
		Assert.assertEquals("Parameter " + i + " name ", "paramx", params.getVarName(i));
		i++;

		Assert.assertEquals("Parameter " + i + " type ", "real", params.getType(i).toString());
		Assert.assertEquals("Parameter " + i + " name ", "param2", params.getVarName(i));
		i++;

		Assert.assertEquals("Parameter " + i + " type ", "string", params.getType(i).toString());
		Assert.assertEquals("Parameter " + i + " name ", "s3", params.getVarName(i));
		i++;

		Assert.assertEquals("Parameter " + i + " type ", "string", params.getType(i).toString());
		Assert.assertEquals("Parameter " + i + " name ", "slist", params.getVarName(i));
		i++;
	}

	@Test
	public void test_function_declaration_03() {
		Gpr.debug("Test");

		Types.reset();
		TypeList.get(Types.STRING);
		TypeMap.get(Types.STRING, Types.STRING);

		String signature = "zzz(string s, string[] slist, string{string} smap) -> string[]";
		FunctionDeclaration fd = new FunctionDeclaration(signature);

		// Check 
		Assert.assertEquals("Function name", "zzz", fd.getFunctionName());
		Assert.assertEquals("Return type", "string[]", fd.getReturnType().toString());

		Parameters params = fd.getParameters();
		Assert.assertEquals("Number of parameters", 3, params.size());

		int i = 0;
		Assert.assertEquals("Parameter " + i + " type ", "string", params.getType(i).toString());
		Assert.assertEquals("Parameter " + i + " name ", "s", params.getVarName(i));
		i++;

		Assert.assertEquals("Parameter " + i + " type ", "string[]", params.getType(i).toString());
		Assert.assertEquals("Parameter " + i + " name ", "slist", params.getVarName(i));
		i++;

		Assert.assertEquals("Parameter " + i + " type ", "string{string}", params.getType(i).toString());
		Assert.assertEquals("Parameter " + i + " name ", "smap", params.getVarName(i));
		i++;
	}
}
