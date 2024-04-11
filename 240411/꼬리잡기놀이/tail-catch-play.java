import java.util.*;
class People{
	int r,c,team;
	People right,left;
	boolean isend;
	People(int r,int c, int team,boolean isend){
		this.r = r;
		this.c = c;
		this.team = team;
		this.isend = isend;
	}
	@Override
	public String toString() {
		return r + "," + c + "," + team;
	}
	
}
class Pair{
	int x,y;

	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}

public class Main {
	static int N,M,K;
	static Scanner scan = new Scanner(System.in);
	static People[] headlst;//idx = teamnum
	static People[] taillst;
	static boolean[][] roadmap;
	static boolean[] reversedlst;
	static int[][] peopleidxmap;
	static People[][] peoplemap;
	static int teamidx;
	static int[][] dirs = {{1,0},{0,1},{-1,0},{0,-1}};
	static int SUM;
		
	public static void main(String[] args) {
		N = scan.nextInt();
		M = scan.nextInt();
		K = scan.nextInt();
		teamidx = 1;
		SUM = 0;
		roadmap = new boolean[N][N];
		peopleidxmap = new int[N][N];
		peoplemap = new People[N][N];
		reversedlst = new boolean[M+1];
		headlst = new People[M+1];
		taillst = new People[M+1];
		
		
		HashSet<Pair> hs = new HashSet<>();
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				int tmp = scan.nextInt();
				
				if(tmp>0) {
					roadmap[i][j] = true;
					peopleidxmap[i][j] = tmp;
					if(tmp==1) {
						hs.add(new Pair(i,j));
						
					}
				}
				
			}
		}
		for(Pair p:hs) {
			int r = p.x;
			int c = p.y;
			People head = new People(r,c,teamidx,true);
			headlst[teamidx] = head;
			peoplemap[r][c] = head;
			fillmap(head);
			teamidx++;
		}
//		printpeoplemap();
//		printroadmap();
		
		for(int round = 0;round<K;round++) {
//			System.out.println("Round : "+round);
			movepeople();
//			printpeoplemap();
//			printroadmap();
			
			int temp = round%(4*N);
			int a = temp/N; //0,1,2,3
			int b = temp%N; //0 ~ round-1
			
			throwball(a,b);
//			printpeoplemap();
//			printroadmap();
			
		}
		System.out.println(SUM);
		
		
		
	}

	private static void throwball(int a, int b) {
		switch(a){
		case 0:
			for(int i=0;i<N;i++) {
				if(ball(b,i)) {
					return;
				};
			}
			break;
		case 1:
			for(int i=N-1;i>=0;i--) {
				if(ball(i,b)) {
					return;
				};
			}
			break;
		case 2:
			for(int i=N-1;i>=0;i--) {
				if(ball(b,i)) {
					return;
				};
			}
			
			break;
		case 3:
			for(int i=0;i<N;i++) {
				if(ball(i,b)) {
					return;
				};
			}
			break;
		}
		
	}

	private static boolean ball(int r,int c) {
		if(notinroad(r,c)) return false;
		if(peoplemap[r][c]==null) return false;
		People p = peoplemap[r][c];
		int res = 0;
		if(reversedlst[p.team]) {
			if(taillst[p.team]==p) {
				res = 1;
			}else{
				res = getnum(p.right,2);
				
			}
		}else {
			if(headlst[p.team]==p) {
				res = 1;
			}else {
				res = getnum(p.left,2);
				
			}
			
		}
		SUM+=Math.pow(res,2);
//		System.out.println("SUM : "+SUM);
		
		reversedlst[p.team] = !reversedlst[p.team];
		
		return true;
	}

	private static int getnum(People p,int i) {
		if(p.isend) return i;
		if(reversedlst[p.team]) {
			return getnum(p.right,i+1);
		}else {
			return getnum(p.left,i+1);
			
		}
	}

	private static void movepeople() {
		for(int i=1;i<M+1;i++) {
			People people = null;
			if(reversedlst[i]) {
				people = taillst[i];
				
				
			}else {
				people = headlst[i];
				
			}
			int r = people.r;
			int c = people.c;
			for(int[] dir:dirs) {
				int nextr = r+dir[0];
				int nextc = c+dir[1];
				if(notinroad(nextr,nextc)) continue;
				if(peoplemap[nextr][nextc]!=null) continue;
				people.r = nextr;
				people.c = nextc;
				peoplemap[r][c] = null;
				peoplemap[nextr][nextc] = people;
				if(reversedlst[i]) {
					move(people.left,reversedlst[i],r,c);
					
					
				}else {
					move(people.right,reversedlst[i],r,c);
					
				}
				
				
			}
			
		}
	}

	private static void move(People people, boolean b,int nextr,int nextc) {
		int r = people.r;
		int c = people.c;
		people.r = nextr;
		people.c = nextc;
		peoplemap[r][c] = null;
		peoplemap[nextr][nextc] = people;
		if(people.isend) return;
		if(b) {
			move(people.left,b,r,c);
			
			
		}else {
			move(people.right,b,r,c);
			
		}
		
	}

	private static void printroadmap() {
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(roadmap[i][j]) {
					System.out.print("1 ");
				}else {
					System.out.print("0 ");
					
				}
				
			}
			System.out.println();
		}
		
	}

	private static void printpeoplemap() {
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(peoplemap[i][j]==null) {
					System.out.print("- ");
					
				}else {
					System.out.print(peoplemap[i][j].team+" ");
				}
				
			}
			System.out.println();
		}
		
	}

	private static void fillmap(People p) {
		int r = p.r;
		int c = p.c;
		for(int[] dir:dirs) {
			int nextr = r+dir[0];
			int nextc = c+dir[1];
			if(notinroad(nextr,nextc)) continue;
			if(peoplemap[nextr][nextc]!=null) continue;
			if(peopleidxmap[nextr][nextc]==2) {
				People people = new People(nextr,nextc,teamidx,false);
				p.right = people;
				people.left = p;
				peoplemap[nextr][nextc] = people;
				fillmap(people);
			}else if(peopleidxmap[nextr][nextc]==3) {

				People people = new People(nextr,nextc,teamidx,true);
				p.right = people;
				people.left = p;
				peoplemap[nextr][nextc] = people;
				taillst[teamidx] = people;
				return;
			}
				
		}
		
		
	}

	private static boolean notinroad(int nextr, int nextc) {
		if(nextr<0||nextr>=N||nextc<0||nextc>=N||!roadmap[nextr][nextc]) return true;
		// TODO Auto-generated method stub
		return false;
	}

}