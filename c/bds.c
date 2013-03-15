/*-----------------------------------------------------------------------------
 *
 * ClousScript wrapper: Invoke BigDataScript's program (java)
 *
 * Note: Kill any process in the process groups after child finishes
 *
 *														Pablo Cingolani 2013
 *-----------------------------------------------------------------------------
 */

#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include  <signal.h>

#define JAVA "java"
#define JAVA_MEM "-Xmx1G"
#define JAR_FILE "BigDataScript.jar"
#define BIGDATASCRIPT_CLASS "ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript"
#define JAVA_ARGC 3

/*
 * Signal handler function
 */
void signalHandler(int dummy) {
	kill(0, SIGKILL);
	exit(1);
}

/*
 * Main
 */
int main(int argc, char *argv[]){
	char **args;
	pid_t pid;
	int retval;	/* Child process return value */
	int i, n;

	/* Set up signal handlers */
	signal(SIGABRT, &signalHandler);
	signal(SIGTERM, &signalHandler);
	signal(SIGINT,  &signalHandler);

	/* Attempt to fork and check for errors */
	if( (pid=fork()) == -1){
		fprintf(stderr,"Fork error!\n");  /* something went wrong */
		exit(1);        
	}

	if(pid){
		/* A positive (non-negative) PID indicates the parent process */
		wait(&retval);				/* Wait for child process to end */
		kill( - pid, SIGKILL );		/* Send a kill signal to process group (notice the negative sign) */
	}
	else{
		/* A zero PID indicates that this is the child process: Execute java program */

		/* Create new arguments array */
		n = argc + JAVA_ARGC;
		args = calloc( n , sizeof(char *) );
		args[0] = JAVA;
		args[1] = JAVA_MEM;
		args[2] = BIGDATASCRIPT_CLASS;

		/* Copy this program's arguments */
		for( i=0 ; i < argc ; i++ ) 
			args[i+JAVA_ARGC] = argv[i+1];
		args[n-1] = NULL;

		/* Replace the child fork with a new process */
		execvp(JAVA, args);

		/* This only gets executed if execvp fails */
		fprintf(stderr,"Error: Cannot execute java program.");	
		_exit(-1);	/* The reason why you need _exit() as opposed to the more normal exit() is because you want to quit the child process but you do not want to run any registered cleanup code associated with the parent process. */
	}

	/* Use same value a child porcess */
	return retval;
}

