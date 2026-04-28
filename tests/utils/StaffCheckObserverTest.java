package utils;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

import trains.MockTrafficController;
import trains.PassengerTrain;
import trains.Train;

public class StaffCheckObserverTest {
	
	private final MockTrafficController controller = new MockTrafficController();
	
	@Test
	public void blocksTrainIfStaffInsufficient() {
		Train pt = new PassengerTrain(1, 1000);
		StaffCheckObserver guard = new StaffCheckObserver(5);
		
		pt.attachObserver(guard);
		pt.stop(controller);
		
		assertThat(guard.isBlocked(pt)).isTrue();
	}
	
	@Test
	public void allowsTrainIfStaffSufficient() {
		Train pt = new PassengerTrain(1, 10);
		StaffCheckObserver guard = new StaffCheckObserver(100);

		pt.attachObserver(guard);
		pt.stop(controller);

		assertThat(guard.isBlocked(pt)).isFalse();
	}
	
	@Test
	public void unblocksTrainAfterReducingSeats() {
		PassengerTrain pt = new PassengerTrain(1, 1000);
		StaffCheckObserver guard = new StaffCheckObserver(5);
		
		pt.attachObserver(guard);
		pt.stop(controller);
		
		assertThat(guard.isBlocked(pt)).isTrue();
		
		pt.setSeats(50);
		
		assertThat(guard.isBlocked(pt)).isFalse();
	}
	
	@Test
	public void keepsTrackOfMultipleBlockedTrains() {
		StaffCheckObserver guard = new StaffCheckObserver(2);

		PassengerTrain t1 = new PassengerTrain(1, 1000);
		PassengerTrain t2 = new PassengerTrain(2, 10);
		PassengerTrain t3 = new PassengerTrain(3, 2000);

		t1.attachObserver(guard);
		t2.attachObserver(guard);
		t3.attachObserver(guard);

		t1.stop(controller);
		t2.stop(controller);
		t3.stop(controller);

		assertThat(guard.getBlockedTrains())
			.hasSize(2)
			.contains(t1, t3)
			.doesNotContain(t2);

		t1.setSeats(1);

		assertThat(guard.getBlockedTrains())
			.hasSize(1)
			.containsOnly(t3);
	}
}
