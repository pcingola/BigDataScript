
In order for Eclipse to run all the test cases, you need to 
add bds's dir to the PATH that is set for all applications.

Apparently in "OS.X El Capitan" this is done by running 
the following command:

	sudo launchctl config user path "/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bds"

