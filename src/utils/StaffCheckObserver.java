package utils;

import java.util.ArrayList;
import java.util.Collection;

import trains.Train;
import trains.TrainObserver;

public class StaffCheckObserver implements TrainObserver {

	private int maxStaffAvaiable;
	private final Collection<Train> blockedTrains = new ArrayList<>();
	
	public StaffCheckObserver(int maxStaffAvaiable) {
		this.maxStaffAvaiable = maxStaffAvaiable;
	}

	@Override
	public void update(Train train) {
		if (train.getState().isStopped()) {
			StaffPlanner planner = new StaffPlanner();
			train.accept(planner);
			int staffRequired = planner.getRequiredStaff();
			if (staffRequired > maxStaffAvaiable) {
				if (!blockedTrains.contains(train)) {
					blockedTrains.add(train);
				}
			} else {
				blockedTrains.remove(train);
			}
		}
	}
	
	public boolean isBlocked(Train train) {
		return blockedTrains.contains(train);
	}

	Collection<Train> getBlockedTrains() {
		return blockedTrains;
	}

}
