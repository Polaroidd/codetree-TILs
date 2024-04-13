import java.util.*;

class tridir{
	int[] dir = new int[3];

	public tridir(int a, int b, int c) {
		dir[0] = a;
		dir[1] = b;
		dir[2] = c;
	}

	@Override
	public String toString() {
		return dir[0]+" "+dir[1]+" "+dir[2];
	}
	
	
}



class Mon{
	int idx,dir,r,c;

	public Mon(int idx, int dir,int r,int c) {
		this.idx = idx;
		this.dir = dir;
		this.r = r;
		this.c = c;
	}
	
	
}

public class Main {
	static Scanner scan = new Scanner(System.in);
	static int M,T,t; //마리수, 총 시간, 현재 시간
	static HashSet<Mon>[][] monmap;
	static int[][] deadmap;
	static tridir[] tridirlst;
	static int[][] dir = {{0,1},{1,0},{0,-1},{-1,0}};
	static int[][] mondir = {{-1,0},{-1,-1},{0,-1},{1,-1},{1,0},{1,1},{0,1},{-1,1}};
	static int pacr,pacc;
	static int foreggidx;
	static HashSet<Mon> eggset = new HashSet<>();
	static ArrayList<Mon> monlst;
	
	static void printmonmap() {
		
		System.out.println("printmap");
		System.out.println("PACMAN : "+pacr+" "+pacc);
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				if(monmap[i][j].isEmpty()) {
					System.out.print("- ");
					continue;
				}
				for(Mon m:monmap[i][j]) {
					System.out.print(m.dir+",");
					
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	static void printdeadmap() {
		System.out.println("printdeadmap");
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				

				System.out.print(deadmap[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		M = scan.nextInt();
		T = scan.nextInt();
		
		monmap = new HashSet[4][4];
		monlst = new ArrayList<>();
		deadmap = new int[4][4];
		tridirlst = new tridir[64];
		int fortri = 0;
		
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				for(int k=0;k<4;k++) {
					tridirlst[fortri++] = new tridir(i,j,k);
				}
				monmap[i][j] = new HashSet<>();
			}
		}
//		for(int i=0;i<64;i++) { 
//			System.out.println(tridirlst[i].dir[0]+" "+tridirlst[i].dir[1]+" "+tridirlst[i].dir[2]);
//		}
		pacr = scan.nextInt()-1;
		pacc = scan.nextInt()-1;
		
		for(int i=0;i<M;i++) {
			int r =scan.nextInt()-1;
			int c = scan.nextInt()-1;
			Mon m = new Mon(i,scan.nextInt()-1,r,c);
			monmap[r][c].add(m);
			monlst.add(m);
			
			
		}
		foreggidx = M;
		
		//init 끝
//		printmonmap();
//		printdeadmap();
		
		for(t=1;t<=T;t++) {
//			System.out.println("Round : "+t);
			replicationandmove();	
//			printmonmap();
			pacmove();
//			printmonmap();
			eggcomplete();
//			printmonmap();
//			printdeadmap();
			
		}
		System.out.println(monlst.size());
		
		

	}
	private static void eggcomplete() {
		
		for(Mon m:eggset) {
			int r = m.r;
			int c = m.c;
			monlst.add(m);
			monmap[r][c].add(m);
		}
		eggset = new HashSet<>();
	}
	
	private static void pacmove() {
		int max = Integer.MIN_VALUE;
		tridir maxtri = new tridir(0,0,0);
		loop:
		for(int i=0;i<64;i++) {
			boolean[][] visited = new boolean[4][4];
			tridir trid = tridirlst[i];
			int nextr = pacr;
			int nextc = pacc;
			
			int num = 0;
			for(int di:trid.dir) {
				nextr+=dir[di][0];
				nextc+=dir[di][1];
				if(nextr<0||nextr>=4||nextc<0||nextc>=4||visited[nextr][nextc]) continue loop;
//				System.out.print(di+" ");
				num+=monmap[nextr][nextc].size();
				visited[nextr][nextc] = true;
			}
//			System.out.println("result : "+trid.toString()+" "+nextr+" "+nextc+" "+num);
			if(num>=max) {
				max = num;
				maxtri = trid;
			}
			
		}
		
		int nextr = pacr;
		int nextc = pacc;
//		System.out.println("max : "+max);
		
//		System.out.print("tridir : ");
		for(int di:maxtri.dir) {
			nextr+=dir[di][0];
			nextc+=dir[di][1];
//			System.out.print(di+" ");
			if(!monmap[nextr][nextc].isEmpty()) {
				deadmap[nextr][nextc] = t;
			}
			for(Mon m:monmap[nextr][nextc]) {
				monlst.remove(m);
			}
//			System.out.println("remove : "+nextr+" "+nextc);
			monmap[nextr][nextc] = new HashSet<>();
		}
//		System.out.println();
		pacr = nextr;
		pacc = nextc;
	}
	
	private static void replicationandmove() {
		for(Mon m:monlst) {
			eggset.add(new Mon(foreggidx++,m.dir,m.r,m.c));
			
			for(int i=0;i<9;i++) {
				int d = (m.dir+i)%8;
				int nextr = m.r+mondir[d][0];
				int nextc = m.c+mondir[d][1];
				//맵 밖 or 시체 or 팩맨위치면 continue
				if(nextr<0||nextr>=4||nextc<0||nextc>=4||(deadmap[nextr][nextc]!=0&&deadmap[nextr][nextc]+2>=t)||(nextr==pacr&&nextc==pacc)) continue;
				//기존맵삭제
				monmap[m.r][m.c].remove(m);
				//위치업데이트
				m.dir = d;
				m.r = nextr;
				m.c = nextc;
//				System.out.println(nextr+" "+nextc);
				monmap[nextr][nextc].add(m);
				break;
			}
		}
		
	}

}