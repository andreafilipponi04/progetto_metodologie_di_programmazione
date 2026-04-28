package utils;

import trains.FreightTrain;
import trains.PassengerTrain;
import trains.TrainVisitor;

public class StaffPlanner implements TrainVisitor {

	private static final int BASE_CREW_PASSENGER = 2;
	private static final int PASSENGERS_PER_CREW_MEMBER = 50;
	private static final int BASE_CREW_FREIGHT = 1;
	private static final int TONNES_PER_ASSISTANT = 1000;
	private int staffCount = 0;

	@Override
	public void visit(PassengerTrain train) {
		staffCount += BASE_CREW_PASSENGER + train.getSeats() / PASSENGERS_PER_CREW_MEMBER;
	}

	@Override
	public void visit(FreightTrain train) {
		staffCount += BASE_CREW_FREIGHT + train.getMaxCargoWeight() / TONNES_PER_ASSISTANT;
	}

	public int getRequiredStaff() {
		return staffCount;
	}
}
