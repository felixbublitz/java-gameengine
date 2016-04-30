package engine.core;

import java.util.Random;

public class Support {

	public int createRandom(int min, int max) {
		if(min < 0){
			System.err.println("Create Random: min < 0");
			min = 0;
		}
		if(max < 0){
			System.err.println("Create Random: max < 0");
			max = 0;
		}

		if(max < min){
			System.err.println("Create Random: max < min");
			return 0;
		}

		return new Random().nextInt((max - min) + 1) + min;
	}

}
