package utils;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

import trains.FreightTrain;
import trains.PassengerTrain;
import trains.Train;

public class StaffPlannerTest {

	@Test
	public void passengerStaffIsCorrect() {
		Train pt = new PassengerTrain(1, 100);
		StaffPlanner planner = new StaffPlanner();
		
		pt.accept(planner);
		
		assertThat(planner.getRequiredStaff()).isEqualTo(4);
	}
	
	@Test
	public void freightStaffIsCorrect() {
		Train ft = new FreightTrain(2, 2000);
		StaffPlanner planner = new StaffPlanner();
		
		ft.accept(planner);
		
		assertThat(planner.getRequiredStaff()).isEqualTo(3);
	}
	
	@Test
	public void trainStaffIsUncorrect() {
		Train pt = new PassengerTrain(1, 100);
		StaffPlanner planner = new StaffPlanner();
		
		pt.accept(planner);
		
		assertThat(planner.getRequiredStaff()).isNotEqualTo(7);
	}

}
