import java.util.Random;
public class infil {
	int x, y;
	Random rand = new Random();
	
	public infil() {
		this.x = 0;
		this.y = 0;
	}
	
	public void move(int W, int L) {
		int dx[] = {1, 1, 1, -1, -1, -1, 0, 0, 0};
		int dy[] = {1, -1, 0, 1, -1, 0, 1, -1, 0};
		
		while(true) {
			int dir = rand.nextInt(9);

			int newX = x+dx[dir];
			int newY = y+dy[dir];
			
			if(newX >= 0 && newX < W && newY >= 0 && newY < L) {
				x = newX;
				y = newY;
				break;
			}
		}
	}
}
