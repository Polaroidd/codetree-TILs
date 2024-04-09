import java.util.*;

class knite{
	int idx,r,c,h,w,k;
	int damaged = 0;

	public knite(int idx,int r, int c, int h, int w, int k) {
		this.idx = idx;
		this.r = r;
		this.c = c;
		this.h = h;
		this.w = w;
		this.k = k;
	}

	@Override
	public String toString() {
		return idx + " " + r + " " + c + " " + h + " " + w + " " + k;
	}
	
	
}

public class Main {
	static int[][] dir = {{-1,0},{0,1},{1,0},{0,-1}};
	
	static Scanner scan = new Scanner(System.in);
	static int SUM =0;
	static int[][] chessmap,knitemap;
	static int L,N,Q;
	static knite[] knitelst;
	static boolean[] erasedlst;
	
	public static void printknitemap() {
		System.out.println("printknitemap");
		for(int i=0;i<L;i++) {
			for(int j=0;j<L;j++) {
				System.out.print(knitemap[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	public static void printchessmap() {
		System.out.println("printchessmap");
		for(int i=0;i<L;i++) {
			for(int j=0;j<L;j++) {
				System.out.print(chessmap[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printknitestate() {
		System.out.println("printknitestate");
		for(int i=1;i<=N;i++) {
			
			System.out.println(knitelst[i].toString());
			
		}
		System.out.println("SUM : "+SUM);
		
	}
	
	
	public static void main(String[] args) {
		L = scan.nextInt();
		N = scan.nextInt();
		Q = scan.nextInt();
		
		chessmap = new int[L][L];
		knitemap = new int[L][L];
		knitelst = new knite[N+1];
		erasedlst = new boolean[N+1];
		
		//chessmap init
		for(int i=0;i<L;i++) {
			for(int j=0;j<L;j++) {
				chessmap[i][j] = scan.nextInt();
			}
		}
		//knitemap init
		for(int i=1;i<=N;i++) {
			int r = scan.nextInt()-1;
			int c = scan.nextInt()-1;
			int h = scan.nextInt();
			int w = scan.nextInt();
			int d = scan.nextInt();
			knite kn = new knite(i,r,c,h,w,d);
			knitelst[i] = kn;
			fillknite(kn,-1);
			
		}
//		printknitemap();
//		printchessmap();
//		printknitestate();
		
		//query
		for(int i=0;i<Q;i++) {
			int knnum = scan.nextInt();
			int d = scan.nextInt();
			if(erasedlst[knnum]) {
				continue;
			}
			move(knitelst[knnum],d);
			
//			System.out.println("ROUND : "+i);
//			printknitemap();
//			printchessmap();
//			printknitestate();
		}
		int res = 0;
		for(int i=1;i<=N;i++) {
			if(erasedlst[i]) continue;
			res+=knitelst[i].damaged;
		}
		System.out.println(res);
		
	}
	private static void move(knite kn, int d) {
//		System.out.println("move : "+ dir[d][0]+" "+dir[d][1]+" "+kn.idx);
//		System.out.println(moveOK(kn,d));
		if(!moveOK(kn,d)) {
			return;
		}
		eraseknite(kn);
		kn.r+=dir[d][0];
		kn.c+=dir[d][1];
		fillknite(kn,d);
		
		
		
	}
	private static boolean moveOK(knite kn,int d) {
		int rd = dir[d][0];
		int cd = dir[d][1];
		HashSet<Integer> hs = new HashSet<>();
		if(rd==0) {
			int c = 0;
			if(cd==1) {//0,1
				c = kn.c+kn.w;
				
			}else {//0,-1
				c = kn.c-1;
				
			}
			for(int i=kn.r;i<kn.r+kn.h;i++) {
				if(i<0||i>=L||c<0||c>=L||chessmap[i][c]==2) {
					return false;
				}
				int key = knitemap[i][c];
				if(hs.contains(key)||key==0) {
					continue;
				}
				if(!moveOK(knitelst[key],d)) {
					return false;
				}
			}
		}else {
			int r=0;
			if(rd==1) {//1,0
				r = kn.r+kn.h;
			}else {//-1,0
				r = kn.r-1;
			}
			for(int i=kn.c;i<kn.c+kn.w;i++) {
				if(i<0||i>=L||r<0||r>=L||chessmap[r][i]==2) {
					return false;
				}
				int key = knitemap[r][i];
				if(hs.contains(key)||key==0) {
					continue;
				}
				if(!moveOK(knitelst[key],d)) {
					return false;
				}
			}
			
		}
		return true;
		
	}
	private static int fillknite(knite kn,int d) {
		int res = 0;
		HashSet<Integer> hs = new HashSet<>();
		
		for(int i=kn.r;i<kn.r+kn.h;i++) {
			for(int j=kn.c;j<kn.c+kn.w;j++) {
				int idx = knitemap[i][j];
				if(idx>0&&idx!=kn.idx&&!hs.contains(idx)) {
					hs.add(idx);
					eraseknite(knitelst[idx]);
					knitelst[idx].r+=dir[d][0];
					knitelst[idx].c+=dir[d][1];
					int tmp = fillknite(knitelst[idx],d);
					knitelst[idx].k-=tmp;
					SUM+=tmp;
					knitelst[idx].damaged+=tmp;
					if(knitelst[idx].k<=0) {
						erasedlst[idx] = true;
						eraseknite(knitelst[idx]);
					}
				}
				
				
				knitemap[i][j] = kn.idx;
				if(chessmap[i][j]==1) {
					res++;
				}
			}
		}
		return res;
		
	}


	private static void eraseknite(knite kn) {
		for(int i=kn.r;i<kn.r+kn.h;i++) {
			for(int j=kn.c;j<kn.c+kn.w;j++) {
				if(knitemap[i][j]==kn.idx) knitemap[i][j]=0;
			}
			
		}
	}
	


}