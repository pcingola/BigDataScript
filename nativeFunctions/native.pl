#!/usr/bin/perl

#-------------------------------------------------------------------------------
#
# Create function classes from a TXT file
#
#														Pablo Cingolani 2013
#-------------------------------------------------------------------------------

$package = "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.math";

sub createJava($$) {
	my($file, $txt) = @_;
	# print "Creating class file '$file'\n";
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
		($retType, $functionName, $argNames, $argTypes, $functionBody) = split /\t/, $l;

		$retType = toType($retType);

		@argNames = split /,/, $argNames;
		$argNames = "";
		foreach $arg (@argNames) {
			if( $argNames ne "" )	{ $argNames .= ", "; }
			$argNames .= "\"$arg\"";
		}
		$argNames = "{ $argNames }";

		$args = $argTypes;
		$args =~ tr/,/_/;
		$functionClass = "FunctionNative_$functionName\_$args";

		@argTypes = split /,/, $argTypes;
		$argTypes = "";
		foreach $arg (@argTypes) {
			if( $argTypes ne "" )	{ $argTypes .= ", "; }
			$arg = toType($arg);
			$argTypes .= "$arg";
		}
		$argTypes = "{ $argTypes }";

		$classFqn = "$package\.$functionClass";

		# List of classes created
		print ", $classFqn.class //\n";

		# Create class
		$class = <<EOF
package $package;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

public class $functionClass extends FunctionNative {
	public $functionClass() {
		super();
	}

	\@Override
	protected void initFunction() {
		functionName = "$functionName";
		returnType = $retType;

		String argNames[] = $argNames;
		Type argTypes[] = $argTypes;
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunctionToScope();
	}

	\@Override
	protected Object runFunctionNative(BigDataScriptThread bdsThread) {
		$functionBody
	}
}
EOF
;

	createJava("$functionClass\.java", $class);
	}
}

