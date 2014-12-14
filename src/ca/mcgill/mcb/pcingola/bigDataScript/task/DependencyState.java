package ca.mcgill.mcb.pcingola.bigDataScript.task;

public enum DependencyState {
	OK // All dependencies have successfully finished execution
	, WAIT // Still waiting for a dependency to finish
	, ERROR // One or more dependency failed
}
