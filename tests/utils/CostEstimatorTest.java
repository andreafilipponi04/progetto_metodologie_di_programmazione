package utils;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

import trains.Train;
import trains.FreightTrain;
import trains.PassengerTrain;

public class CostEstimatorTest {

	@Test
	public void passengerTrainCostIsCorrect() {
		PassengerTrain pt = new PassengerTrain(1, 100);
		CostEstimator estimator = new CostEstimator();
		
		pt.accept(estimator);
		
		assertThat(estimator.getTotalCost()).isEqualTo(1500.0);
	}
	
	@Test
	public void freightTrainCostIsCorrect() {
		Train ft = new FreightTrain(2, 2000);
		CostEstimator estimator = new CostEstimator();
		
		ft.accept(estimator);
		
		assertThat(estimator.getTotalCost()).isEqualTo(3000.0);
	}
	
	@Test
	public void trainCostIsUncorrect() {
		Train ft = new FreightTrain(2, 2000);
		CostEstimator estimator = new CostEstimator();
		
		ft.accept(estimator);
		
		assertThat(estimator.getTotalCost()).isNotEqualTo(4000.0);
	}

}
