import java.util.Random;
public class sensors {
	Random rand = new Random();
	int prob;
	boolean isOn;
	
	public sensors(int prob, boolean isOn) {
		this.prob = prob;
		this.isOn = isOn;
	}
	
	public void onOff() {
		int outcome = rand.nextInt(101);
		if(outcome <= prob) {
			isOn = true;
		}
		else isOn = false;
	}
}
