import java.util.*;
class Player implements Comparable<Player>{
	
	int r,c,attack,d,idx;
	int gun = 0;
	int point = 0; //각플레이어의 초기 능력치와 가지고 있는 총의 공격력의 합의 차이 만큼 포인트 얻어!
	public Player(int r, int c, int attack, int d,int idx) {
		this.r = r;
		this.c = c;
		this.attack = attack;
		this.d = d;
		this.idx = idx;
	}

	@Override
	public int compareTo(Player other) {
		int sum = this.attack+this.gun;
		int osum = other.attack+other.gun;
		if(sum!=osum) {
			return sum-osum;
		}https://contents.codetree.ai/problems/2705/images/9a9984dc-12cf-448a-afec-c36d8f6cade7.png
		return this.attack-other.attack;
	}

	@Override
	public String toString() {
		return r+" "+c+" "+attack+" "+d+" "+gun+" "+point;
	}
	
	
	
}

public class Main {
	static Scanner scan = new Scanner(System.in);
	static PriorityQueue<Integer>[][] gunmap;
	static int N,M,round;
	static int[][] playermap;
	static Player[] playerlst;
	static int[][] dir = {{-1,0},{0,1},{1,0},{0,-1}};
	public static void printgunmap() {
		System.out.println("printgunmap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(gunmap[i][j].isEmpty()) {
					System.out.print(0+" ");
				}else {
					System.out.print(gunmap[i][j].peek()+" ");
					
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void printplayermap() {
		System.out.println("printplayermap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				System.out.print(playermap[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void printplayerstate() {
		System.out.println("printplayerstate");
		for(int i=1;i<M+1;i++) {
			System.out.println(playerlst[i]+" ");
		}
	}
	
	public static void main(String[] args) {
		N = scan.nextInt();
		M = scan.nextInt();
		round = scan.nextInt();
		
		gunmap = new PriorityQueue[N][N];
		playermap = new int[N][N];
		playerlst = new Player[M+1];
		
		

		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				gunmap[i][j] = new PriorityQueue<>(Collections.reverseOrder());
				int k = scan.nextInt();
				if(k>0) {
					gunmap[i][j].add(k);
				}
				
			}
		}
		for(int i=1;i<M+1;i++) {
			int r = scan.nextInt()-1;
			int c = scan.nextInt()-1;
			int d = scan.nextInt();
			int s = scan.nextInt();
//			System.out.println(r+" "+c);
			playerlst[i] = new Player(r,c,s,d,i);
			playermap[r][c] = i;
		}
//		printplayerstate();
//		printplayermap();
//		printgunmap();
		
		for(int r=0;r<round;r++) {
//			System.out.println("round : "+r);
			for(int i=1;i<=M;i++) {
				play(i);
			}
//			printplayerstate();
//			printplayermap();
//			printgunmap();
			
			
		}
		
		for(int i=1;i<M+1;i++) {
			System.out.print(playerlst[i].point+" ");
		}
		
		
		
		
		


	}
	private static void play(int i) {
		//목적지 지정
		if(notinmap(playerlst[i].r+dir[playerlst[i].d][0],playerlst[i].c+dir[playerlst[i].d][1])) {
			playerlst[i].d = (playerlst[i].d+2)%4;
		}
		int r = playerlst[i].r+dir[playerlst[i].d][0];
		int c = playerlst[i].c+dir[playerlst[i].d][1];
		playermap[playerlst[i].r][playerlst[i].c] = 0;
				
		//플레이어 있는지 체크
		if(playermap[r][c]>0) {
			Player playerA = playerlst[playermap[r][c]];
			Player playerB = playerlst[i];
//			System.out.println("fight : "+playerA.idx+" "+playerB.idx);
			if(playerA.compareTo(playerB)<0) {//무조건 A가 이기게 세팅
				Player temp = playerA;
				playerA = playerB;
				playerB = temp;
			}
			playermap[r][c] = playerA.idx;
			// System.out.println("fight result : "+Math.abs(playerA.attack+playerA.gun-playerB.attack-playerB.gun));
			playerA.point += Math.abs(playerA.attack+playerA.gun-playerB.attack-playerB.gun);

			gunmap[r][c].add(playerB.gun);
			playerB.gun = 0;
			putguninrc(playerA,r,c);
			while(notinmap(r+dir[playerB.d][0],c+dir[playerB.d][1])||playermap[r+dir[playerB.d][0]][c+dir[playerB.d][1]]>0) {
//				System.out.println(playerd);
				playerB.d = (playerB.d+1)%4;
			}
			int rb = r+dir[playerB.d][0];
			int cb = c+dir[playerB.d][1];
			
			playermap[rb][cb] = playerB.idx;
			putguninrc(playerB,rb,cb);
			
			
		}else {//플레이어 없어
			playermap[r][c] = i;
			putguninrc(playerlst[i],r,c);
			
		}
		
		
		
	}
	public static boolean notinmap(int r,int c) {
		if(r<0||r>=N||c<0||c>=N) {
			return true;
		}
		return false;
	}
	public static void putguninrc(Player p,int r,int c) {
		
		p.r = r;
		p.c = c;
		if(p.gun==0) {
			if(!gunmap[r][c].isEmpty()) p.gun = gunmap[r][c].poll();
			return;
		}
		gunmap[r][c].add(p.gun);
		p.gun =gunmap[r][c].poll();
		
	}

}