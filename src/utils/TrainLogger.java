package utils;

import trains.Train;
import trains.TrainObserver;

public class TrainLogger implements TrainObserver {
	
	private TrainPrinter printer;
	
	public TrainLogger(TrainPrinter printer) {
		this.printer = printer;
	}

	@Override
	public void update(Train train) {
		printer.print("Train " + train.getId() + ": changed state to " + train.getState());
	}

}
