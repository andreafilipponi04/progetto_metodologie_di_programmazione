package station;

import java.util.ArrayList;
import java.util.Collection;

import trains.TrafficController;
import trains.Train;
import trains.TrainNotInStationException;
import trains.UnauthorizedTrainActionException;
import utils.CostEstimator;
import utils.StaffCheckObserver;
import utils.TrainLogger;

public class Station implements TrafficController{
	private TrainLogger trainLogger;
	private StaffCheckObserver staffGuard;
	private Collection<Train> trains = new ArrayList<>();

	public Station(TrainLogger trainLogger, int maxStaff) {
		this.trainLogger = trainLogger;
		this.staffGuard = new StaffCheckObserver(maxStaff);
	}
	
	public void acceptTrain(Train train) {
		trains.add(train);
		train.attachObserver(trainLogger);
	}
	
	public void releaseTrain(Train train) {
		trains.remove(train);
		train.detachObserver(trainLogger);
		train.detachObserver(staffGuard);
	}
	
	public double getAllTrainsCosts() {
		CostEstimator estimator = new CostEstimator();
		trains.stream().
			forEach(t -> t.accept(estimator));
		return estimator.getTotalCost();
	}
	
	@Override
	public void authorizeStop(Train train) {
		checkPresence(train);
		train.attachObserver(staffGuard);
		train.stop(this);
	}
	
	@Override
	public void authorizeDeparture(Train train) {
		checkPresence(train);
		if (staffGuard.isBlocked(train)) {
			throw new UnauthorizedTrainActionException("The train " + train.getId() + " is requiring too many resources!");
		}
		train.leave(this);
	}
	
	private void checkPresence(Train train) {
		if (!trains.contains(train)) {
			throw new TrainNotInStationException(
					"Train " + train.getId() + " is not in this station");
		}
	}

	Collection<Train> getTrains() {
		return trains;
	}
}
