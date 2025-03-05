public class border {
	int W, L;
	
	sensors[][] myBorder;
	
	public border(int W, int L, int prob, boolean state) {
		myBorder = new sensors[W][L];
		
		this.W = W;
		this.L = L;
		
		for(int i = 0; i < W; i++) {
			for(int j = 0; j < L; j++) {
				myBorder[i][j] = new sensors(prob, state);
			}
		}
	}
	
}
