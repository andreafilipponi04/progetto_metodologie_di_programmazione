package utils;

import trains.FreightTrain;
import trains.PassengerTrain;
import trains.TrainVisitor;

public class CostEstimator implements TrainVisitor {

	private double totalCost = 0;

	private static final double PASSENGER_BASE_COST = 500.0;
	private static final double SEAT_COST = 10.0;
	private static final double FREIGHT_BASE_COST = 2000.0;
	private static final double WEIGHT_COST_PER_KG = 0.5;

	@Override
	public void visit(PassengerTrain train) {
		totalCost += PASSENGER_BASE_COST + (train.getSeats() * SEAT_COST);
	}

	@Override
	public void visit(FreightTrain train) {
		totalCost += FREIGHT_BASE_COST + (train.getMaxCargoWeight() * WEIGHT_COST_PER_KG);

	}

	public double getTotalCost() {
		return totalCost;
	}
}
