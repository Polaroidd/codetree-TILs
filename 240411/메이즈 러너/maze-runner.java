import java.util.*;

class Player implements Comparable<Player>{
	int idx,r,c;
	int num = 0;

	Player(int idx, int r, int c) {
		this.idx = idx;
		this.r = r;
		this.c = c;
	}

	@Override
	public int compareTo(Player other) {
		return 0;
	}
	
}


public class Main {
	static Scanner scan = new Scanner(System.in);
	static int N,M,K;
	static int[][] wallmap;
	static int exitr,exitc;
	static Player[] playerlst;
	static HashSet<Integer>[][] playermap;
	static int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
	static int[][] sqdirs = {{-1,-1},{-1,1},{1,-1},{1,1}};
	
	static boolean[] playerexit;
	
	
	public static void main(String[] args) {
		N = scan.nextInt();
		M = scan.nextInt();
		K = scan.nextInt();

		playerexit = new boolean[M+1];
		playerlst = new Player[M+1];
		wallmap = new int[N][N];
		playermap = new HashSet[N][N];
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				wallmap[i][j] = scan.nextInt();
				playermap[i][j] = new HashSet<>();
				
			}
		}
		
		for(int i=1;i<=M;i++) {
			int r = scan.nextInt()-1;
			int c = scan.nextInt()-1;
			playermap[r][c].add(i);
			playerlst[i] = new Player(i,r,c);
		}
		exitr = scan.nextInt()-1;
		exitc = scan.nextInt()-1;
		//init 끝
		
//		printwallmap();
//		printplayermap();
		
		
		//round 진행
		for(int i=1;i<=K;i++) {
//			System.out.println("Round : "+i);
//			System.out.println("move");
			playermove();
			if(allplayerexit()) {
				break;
			}
//			printplayermap();
//			System.out.println("rotate");
			mazerotate();
//			printplayermap();
			
			
//			printwallmap();
		}
		
		int res = 0;
		for(int i=1;i<=M;i++) {
			res+=playerlst[i].num;
		}
		System.out.println(res);
		System.out.println((exitr+1) + " "+(exitc+1));
		
		
	}
	

	private static void mazerotate() {
		int r = 0;
		int c = 0;
		int i=1;
		loop:
		for(i=1;i<N+1;i++) {
			for(r=exitr-i;r<=exitr;r++) {
				for(c=exitc-i;c<=exitc;c++) {
					if(contains(r,c,r+i,c+i)) {
//						System.out.println("contains : "+r+" "+c);
						break loop;
						
					}
				}
			}
			
			
//			for(int j=i;j>=0;j--) {
////				System.out.println(r+" "+c);
//				r = exitr-j;
//				c = exitc-i;
//				if(contains(r,c,r+i,c+i)) {
////					System.out.println("contains : "+r+" "+c);
//					break loop;
//					
//				}
//			}
//			for(int j=i;j>=0;j--) {
////				System.out.println(r+" "+c);
//				r = exitr-j;
//				c = exitc;
//				if(contains(r,c,r+i,c+i)) {
////					System.out.println("contains : "+r+" "+c);
//					break loop;
//					
//				}
//			}
			
		}
		int minr = r;
		int minc = c;
		int maxidx = i;
		
		
		int num = maxidx+1;
//		printplayermap();
//		System.out.println("minrc : "+minr+" "+minc+" "+" "+maxidx);
		
		int[][] tempwall = new int[num][num];
		HashSet<Integer>[][] tempplayer = new HashSet[num][num];
		int tempexitr = 0;
		int tempexitc = 0;
		i=0;
		for(i=0;i<num;i++) {
			for(int j=0;j<num;j++) {
				if(wallmap[minr+i][minc+j]>0) {
					tempwall[j][maxidx-i] = wallmap[minr+i][minc+j]-1;
					
				}else {

					tempwall[j][maxidx-i] = wallmap[minr+i][minc+j];
				}
				tempplayer[j][maxidx-i] = playermap[minr+i][minc+j];
				if(minr+i==exitr&&minc+j==exitc) {
					tempexitr = j;
					tempexitc = maxidx-i;
				}
				
			}
		}
		
//		System.out.println("tempwall");
		
//		for(i=0;i<num;i++) {
//			for(int j=0;j<num;j++) {
//				System.out.print(tempwall[i][j]+" ");
//			}
//			System.out.println();
//		}
		
		for(i=0;i<num;i++) {
			for(int j=0;j<num;j++) {
				wallmap[minr+i][minc+j] = tempwall[i][j];
				if(!tempplayer[i][j].isEmpty()) {
					for(int idx:tempplayer[i][j]) {
						playerlst[idx].r = minr+i;
						playerlst[idx].c = minc+j;
					}
				}
				playermap[minr+i][minc+j] = tempplayer[i][j];
				
				
			}
		}
//		int tempexitr = exitc;
//		int tempexitc = maxidx-exitr;
		
		exitr = minr+tempexitr;
		exitc = minc+tempexitc;
	}


	private static boolean contains(int minr, int minc,int maxr,int maxc) {
		if(minr<0||minr>=N||minc<0||minc>=N) return false;
		if(maxr<0||maxr>=N||maxc<0||maxc>=N) return false;
		
		
		for(int i=minr;i<=maxr;i++) {
			for(int j=minc;j<=maxc;j++) {
				if(!playermap[i][j].isEmpty()) return true;
			}
		}
		return false;
		
	}
	




	private static boolean allplayerexit() {
		for(int i=1;i<=M;i++) {
			if(!playerexit[i]) return false;		
		}
		return true;
	}


	private static void playermove() {
		for(int i=1;i<=M;i++) {
			if(playerexit[i]) continue;
			Player p = playerlst[i];
			for(int[] dir:dirs) {
				int r = p.r+dir[0];
				int c = p.c+dir[1];
				
				//밖이거나 벽이면 continue
				if(r<0||r>=N||c<0||c>=N||wallmap[r][c]>0) continue;
//				System.out.println("here : "+r+" "+c+" "+i);
				//갈 곳 r,c가 더 멀어지는 경우 continue
				if(getdis(r,c)>=getdis(p.r,p.c)) {
					continue;
				}
				//이동
				p.num+=1;
				playermap[p.r][p.c].remove(i);
				p.r =r;
				p.c = c;
				if(r==exitr&&c==exitc) {
					playerexit[i] = true;
					break;
				}
				playermap[r][c].add(i);
				break;
				
			}
			
		}
		
	}


	private static int getdis(int r, int c) {
		// TODO Auto-generated method stub
		return Math.abs(r-exitr)+Math.abs(c-exitc);
	}
	public static void printwallmap() {
		System.out.println("printwallmap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				System.out.print(wallmap[i][j]+" ");
				
			}
			System.out.println();
		}
	}
	public static void printplayermap() {
		System.out.println("EXIT : "+exitr+" "+exitc);
		System.out.println("printplayermap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(playermap[i][j].isEmpty()) {
					System.out.print("0 ");
					
				}else {
					for(int k:playermap[i][j]) {
						System.out.print(k+",");
					}
					System.out.print(" ");
				}
				
			}
			System.out.println();
		}
	}

}