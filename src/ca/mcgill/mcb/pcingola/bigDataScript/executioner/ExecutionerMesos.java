package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.mesos.BdsMesosFramework;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;

/**
 * Execute tasks on Mesos
 *
 * @author pcingola
 */
public class ExecutionerMesos extends Executioner {

	public static final String MESOS_MASTER_PROPERTY_NAME = "mesos.master";
	public static final String DEFAULE_MESOS_MASTER = "127.0.0.1:5050";

	BdsMesosFramework mesosFramework;

	public ExecutionerMesos(Config config) {
		super(config);

		// Initialize framework
		String master = config.getString(MESOS_MASTER_PROPERTY_NAME, DEFAULE_MESOS_MASTER);
		mesosFramework = new BdsMesosFramework(master);
	}

	/**
	 * Create a command form a task
	 * @param task
	 * @return
	 */
	@Override
	protected synchronized Cmd createCmd(Task task) {
		throw new RuntimeException("Unimplemented method for class: " + this.getClass().getCanonicalName());
	}

	@Override
	public String[] osKillCommand(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

}
