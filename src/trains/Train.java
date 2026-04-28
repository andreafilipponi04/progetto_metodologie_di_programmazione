package trains;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Train {

	private int id;
	private TrainState state;
	private Collection<TrainObserver> observers = new ArrayList<TrainObserver>();

	protected Train(int id) {
		this.id = id;
		this.state = TrainState.ofInArrival();
	}

	public void stop(TrafficController controller) {
		if (controller == null) {
			throw new UnauthorizedTrainActionException("Unauthorized Stop");
		}
		this.state = this.state.stop();
		notifyObservers();
	}

	public void leave(TrafficController controller) {
		if (controller == null) {
			throw new UnauthorizedTrainActionException("Unauthorized Departure");
		}
		this.state = this.state.leave();
		notifyObservers();
	}

	public TrainState getState() {
		return this.state;
	}

	public int getId() {
		return id;
	}

	public void attachObserver(TrainObserver o) {
		observers.add(o);
	}

	public void detachObserver(TrainObserver o) {
		observers.remove(o);
	}

	public void notifyObservers() {
		observers.stream().forEach(o -> o.update(this));
	}

	// only for testing
	Collection<TrainObserver> getObservers() {
		return observers;
	}

	public abstract void accept(TrainVisitor visitor);

}
