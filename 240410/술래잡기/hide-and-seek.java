import java.util.*;
class Dir{
	int r,c;

	public Dir(int r, int c) {
		this.r = r;
		this.c = c;
	}
	
}

class Thief{
	int idx,r,c,rd,cd;
	public Thief(int idx, int r, int c, int rd, int cd) {
		this.idx = idx;
		this.r = r;
		this.c = c;
		this.rd = rd;
		this.cd = cd;
	}
	
}

public class Main {
	static int[][] dir = {{-1,0},{0,1},{1,0},{0,-1}};
	static boolean taggerreversed = false;
	static Dir[][] taggerdirmap;
	static Thief[] thieflst;
	static boolean[][] treemap;
	static HashSet<Integer>[][] thiefmap;
	static Scanner scan = new Scanner(System.in);
	static int N,M,H,K;
	static int result=0;
	static int tagr,tagc;
	static int round=1;
	static boolean[]  caught;
	public static void printthiefmap() {
		System.out.println("printthiefmap");
		System.out.println("tagger location : "+tagr+" "+tagc);
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(thiefmap[i][j].isEmpty()) {
					System.out.print("0 ");
					
				}else {
					for(int k:thiefmap[i][j]) {
						System.out.print(k);
						
					}
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	public static void printtreemap() {
		System.out.println("printtreemap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(treemap[i][j]) {
					System.out.print("1 ");
					
				}else {
					System.out.print("0 ");
					
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		//init
		N = scan.nextInt();
		M = scan.nextInt();
		H = scan.nextInt();
		K = scan.nextInt();
		caught = new boolean[M+1];
		tagr = N/2;
		tagc = N/2;
		thieflst = new Thief[M+1];
		treemap = new boolean[N][N];
		thiefmap = new HashSet[N][N];
		taggerdirmap = new Dir[N][N];
		//thiefmap init
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				thiefmap[i][j] = new HashSet<>();
			}
		}
		for(int i=1;i<=M;i++) {
			int rr = scan.nextInt()-1;
			int cc = scan.nextInt()-1;
			int d = scan.nextInt();
			thieflst[i] = new Thief(i,rr,cc,dir[d][0],dir[d][1]);
			thiefmap[rr][cc].add(i);
		}
		//treemap init
		
		for(int i=0;i<H;i++) {
			int rr = scan.nextInt()-1;
			int cc = scan.nextInt()-1;
			treemap[rr][cc] = true;
		}
		
		//taggerdirmap init
		int r = N/2;
		int c = N/2;
		int d = 0;
		
		loop:
		for(int i=1;r!=0||c!=0;i++) {
			for(int j=0;j<2;j++) {
				for(int k=0;k<i;k++) {
					
					taggerdirmap[r][c] = new Dir(dir[d][0],dir[d][1]);
					if(r==0&&c==0) {
						
						break loop;
					}
					r+=dir[d][0];
					c+=dir[d][1];
					
				}
				d = (d+1)%4;
			}
		}
		//print taggerdirmap
//		for(int i=0;i<N;i++) {
//			for(int j=0;j<N;j++) {
//				if(taggerdirmap[i][j]==null) {
//					System.out.print("(-,-) ");
//					continue;
//				}
//				System.out.print("("+taggerdirmap[i][j].r+","+taggerdirmap[i][j].c+") ");
//				
//			}
//			System.out.println();
//		}
//		System.out.println();
//		
//		printthiefmap();
//		printtreemap();
		
		
		
		//round start
		for(round=1;round<=K;round++) {
			movethief();
			movetagger();
//			System.out.println("Round : "+round);
//			printthiefmap();
		}
		System.out.println(result);
		

	}
	private static void movetagger() {
		int rd = taggerdirmap[tagr][tagc].r;
		int cd = taggerdirmap[tagr][tagc].c;
		
		if(taggerreversed) {
			rd*=-1;
			cd*=-1;
		}
		tagr+=rd;
		tagc+=cd;
		
		int seerd = taggerdirmap[tagr][tagc].r;
		int seecd = taggerdirmap[tagr][tagc].c;
		
		for(int i=0;i<3;i++) {
			int sawr = tagr+seerd*i;
			int sawc = tagc+seecd*i;
			if(thiefmap[sawr][sawc].isEmpty()||treemap[sawr][sawc]) continue;
			result+=round*thiefmap[sawr][sawc].size();
			for(int k:thiefmap[sawr][sawc]) {
				caught[k] = true;
				thiefmap[sawr][sawc].remove(k);
			}
		}
		if((tagr==0&&tagc==0)||(tagr==N/2&&tagc==N/2)) {
			taggerreversed = !taggerreversed;
		}
		
		
		
	}
	private static void movethief() {
		for(int i=1;i<=M;i++) {
			if(caught[i]) continue;
			Thief th = thieflst[i];
			int r = th.r+th.rd;
			int c = th.c+th.cd;
			if(r<0||r>=N||c<0||c>=N) {
				th.rd *=-1;
				th.cd*=-1;
				r = th.r+th.rd;
				c = th.c+th.cd;
			}
			//자리에 술래가 있어
			if(r==tagr&&c==tagc) continue;
			thiefmap[th.r][th.c].remove(i);
			th.r =r;
			th.c = c;
			thiefmap[r][c].add(i);
			
		}
//		printthiefmap();
		
		
	}

}