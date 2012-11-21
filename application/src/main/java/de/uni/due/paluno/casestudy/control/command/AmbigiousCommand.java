package de.uni.due.paluno.casestudy.control.command;

public abstract class AmbigiousCommand extends Command {

	/**
	 * Returns an unique identification for each event instance is used to
	 * create an single eventstream for each event
	 * 
	 * @return
	 */
	public abstract String getUniqueEventIdentifier();

	@Override
	public String getEventName() {
		return this.getEntity() + this.getUniqueEventIdentifier();
	}

	/**
	 * Entity that is used in EPL
	 * 
	 * @return class name
	 */
	protected abstract String getEntity();
}
