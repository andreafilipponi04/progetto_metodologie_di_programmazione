package utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import trains.FreightTrain;
import trains.Train;
import trains.MockTrafficController;
import trains.PassengerTrain;


public class TrainLoggerTest {
	
	private MockTrainPrinter printer;
	private TrainLogger logger;
	private MockTrafficController controller;
	private Train firstTrain;
	
	@Before
	public void setUp() {
		printer = new MockTrainPrinter();
		logger = new TrainLogger(printer);
		controller = new MockTrafficController();
		firstTrain = new PassengerTrain(1, 100);
		
		firstTrain.attachObserver(logger);
	}
	
	@Test
	public void loggerWritesMessageOnStop() {
		firstTrain.stop(controller);
		assertThat(printer.toString())
			.isEqualTo("Train 1: changed state to STOPPED\n");
	}
	
	@Test
	public void loggerWritesMessageOnDeparture() {
		firstTrain.stop(controller);
		firstTrain.leave(controller);
		assertThat(printer.toString())
			.isEqualTo("Train 1: changed state to STOPPED\n"
					+ "Train 1: changed state to DEPARTED\n");
	}
	
	@Test
	public void loggerWritesMessageOnMultipleTrain() {
		Train secondTrain = new FreightTrain(2, 2000);
		secondTrain.attachObserver(logger);

		firstTrain.stop(controller);
		secondTrain.stop(controller);
		firstTrain.leave(controller);
		assertThat(printer.toString())
			.isEqualTo("Train 1: changed state to STOPPED\n"
					+ "Train 2: changed state to STOPPED\n"
					+ "Train 1: changed state to DEPARTED\n");
	}

}
