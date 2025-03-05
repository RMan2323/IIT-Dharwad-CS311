public class softwareSim {
	public static void main(String args[]) {
		int W = 5, L = 100, prob = 1;
		
		border b = new border(W, L, prob, false);
		
		infil badGuy = new infil();
		
		int time = 1;
		
		while(true) {
			if(time % 10 == 1) {
				badGuy.move(b.W, b.L);
			}
			if(time % 10 == 0) {
				for(int i = 0; i < b.W; i++) {
					for(int j = 0; j < b.L; j++) {
						b.myBorder[i][j].onOff();
					}
				}
			}
			if(badGuy.x == W-1) {
				System.out.println("Defeated! Time = "+time);
				return;
			}
			if((2 <= time % 10 || time % 10 == 0)  && b.myBorder[badGuy.x][badGuy.y].isOn) {
				System.out.println("Caught! Time = "+time);
				return;
			}
			time++;
		}
	}
}
