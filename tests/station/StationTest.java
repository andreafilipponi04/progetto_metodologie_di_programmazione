package station;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import trains.FreightTrain;
import trains.PassengerTrain;
import trains.Train;
import trains.TrainNotInStationException;
import trains.TrainState;
import trains.UnauthorizedTrainActionException;
import utils.MockTrainPrinter;
import utils.TrainLogger;

public class StationTest {

	private Station station;
	private TrainLogger logger;
	private MockTrainPrinter printer;

	@Before
	public void setUp() {
		printer = new MockTrainPrinter();
		logger = new TrainLogger(printer);
		station = new Station(logger, 100);
	}

	@Test
	public void getAllTrainsCostsTest() {
		Train t1 = new PassengerTrain(1, 100);
		Train t2 = new FreightTrain(2, 2000);

		station.acceptTrain(t1);
		station.acceptTrain(t2);

		double total = station.getAllTrainsCosts();

		assertThat(total).isEqualTo(4500.0);
	}

	@Test
	public void testStationRefusesUnknownTrains() {
		Train sconosciuto = new PassengerTrain(99, 100);

		assertThatThrownBy(() -> station.authorizeStop(sconosciuto))
			.isInstanceOf(TrainNotInStationException.class)
			.hasMessage("Train 99 is not in this station");
	}


	@Test
	public void testAcceptAndCalculateCosts() {
		Train t1 = new PassengerTrain(1, 100);
		Train t2 = new FreightTrain(2, 2000);

		station.acceptTrain(t1);
		station.acceptTrain(t2);

		assertThat(station.getAllTrainsCosts()).isEqualTo(4500.0);

		assertThat(station.getTrains()).contains(t1, t2);
	}

	@Test
	public void testReleaseTrain() {
		Train t1 = new PassengerTrain(1, 100);
		station.acceptTrain(t1);

		station.releaseTrain(t1);

		assertThat(station.getAllTrainsCosts()).isEqualTo(0.0);

		assertThatThrownBy(() -> station.authorizeStop(t1))
				.isInstanceOf(TrainNotInStationException.class)
				.hasMessage("Train 1 is not in this station");
	}


	@Test
	public void testAuthorizeStop() {
		Train t1 = new PassengerTrain(1, 50);
		station.acceptTrain(t1);

		assertThat(t1.getState()).isEqualTo(TrainState.ofInArrival());

		station.authorizeStop(t1);

		assertThat(t1.getState()).isEqualTo(TrainState.ofStopped());
	}

	@Test
	public void testAuthorizeDeparture() {
		Train t1 = new PassengerTrain(1, 50);
		station.acceptTrain(t1);

		station.authorizeStop(t1);
		station.authorizeDeparture(t1);

		assertThat(t1.getState()).isEqualTo(TrainState.ofDeparted());
	}


	@Test
	public void testStaffCheckBlocksDeparture() {
		Train heavyTrain = new PassengerTrain(1, 10000);

		station.acceptTrain(heavyTrain);
		station.authorizeStop(heavyTrain);

		assertThat(heavyTrain.getState())
			.isEqualTo(TrainState.ofStopped());

		assertThatThrownBy(() -> station.authorizeDeparture(heavyTrain))
			.isInstanceOf(UnauthorizedTrainActionException.class)
			.hasMessageContaining("too many resources");
	}

}
