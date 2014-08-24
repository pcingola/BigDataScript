#!/usr/bin/perl

#-------------------------------------------------------------------------------
#
# Create method classes from a TXT file
#
#														Pablo Cingolani 2013
#-------------------------------------------------------------------------------

$package = "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods";
$packageStr = "$package.string";

sub createJava($$) {
	my($file, $txt) = @_;
	print "Creating class file '$file'\n";
	open CLASS, "> $file";
	print CLASS $txt;
	close CLASS;
}

#-------------------------------------------------------------------------------
# Transform to 'Type'
#-------------------------------------------------------------------------------
sub toType($) {
	($type) = @_;
	if( $type =~ /^Type/ )	{ return $type; }
	$type =~ tr/[a-z]/[A-Z]/;
	$type = "Type.$type";
	return $type;
}

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

# Read and parse 'native.txt' file (from STDIN)
while( $l = <STDIN> ) {
	chomp $l;
	if( $l !~ /^#/ ) {
		($retType, $classType, $methodName, $argNames, $argTypes, $methodBody) = split /\t/, $l;

		$class= $classType;
		$retType = toType($retType);
		$classType = toType($classType);

		@argNames = split /,/, $argNames;
		$argNames = "";
		$args = "";
		foreach $arg (@argNames) {
			if( $argNames ne "" )	{ $argNames .= ", "; }
			$argNames .= "\"$arg\"";

			if( $arg eq 'this' )	{ ; }	# Don't add
			else 					{ $args .= "_$arg"; }
		}
		$argNames = "{ $argNames }";

		$methodClass = "MethodNative_$class\_$methodName$args";

		@argTypes = split /,/, $argTypes;
		$argTypes = "";
		foreach $arg (@argTypes) {
			if( $argTypes ne "" )	{ $argTypes .= ", "; }
			$arg = toType($arg);
			$argTypes .= "$arg";
		}
		$argTypes = "{ $argTypes }";

		$classFqn = "$package\.string.$methodClass";

		# List of classes created
		if( $classFqnAll ne "" )	{ $classFqnAll .= "\t, "; }
		else 						{ $classFqnAll .= "\t"; }
		$classFqnAll .= "\"$classFqn\" // \n";

		# Create class
		$class = <<EOF
package $packageStr;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class $methodClass extends MethodNative {
	public $methodClass() {
		super();
	}

	\@Override
	protected void initMethod() {
		functionName = "$methodName";
		classType = $classType;
		returnType = $retType;

		String argNames[] = $argNames;
		Type argTypes[] = $argTypes;
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	\@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		$methodBody
	}
}
EOF
;

	createJava("$methodClass\.java", $class);
	}
}

#---
# Create 'NativeLibrary', which loads all classes
#---
$class = <<EOF

package $package;

import java.util.ArrayList;

/**
 * Loads all classes used for native library
 * \@author pcingola
 *
 */
public class NativeLibraryString {

	
	public static String classNames[] = {
		$classFqnAll
	};

	ArrayList<MethodNative> methods;

	\@SuppressWarnings("rawtypes")
	public NativeLibraryString() {
		try {
			methods = new ArrayList<MethodNative>();

			for (String className : classNames) {
				Class c = Class.forName(className);
				methods.add((MethodNative) c.newInstance());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error creating native library", e);
		}
	}

	\@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + ":\\n");
		for (MethodNative m : methods)
			sb.append("\\t" + m.getClass().getSimpleName() + "\\n");
		return sb.toString();
	}

}
EOF
;

createJava("NativeLibraryString.java", $class);
