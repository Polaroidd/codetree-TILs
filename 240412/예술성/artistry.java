import java.util.*;
class Pair {
	int r,c;

	public Pair(int r, int c) {
		this.r = r;
		this.c = c;
	}

	
	
}
class Picture{
	
	int id;
	int num;
	int sum = 0;
	HashMap<Integer,Integer> idtomeet = new HashMap<>();
	public Picture(int id,int num) {
		this.id = id; //art id
		this.num = num; // 해당하는 숫자
	}
	
	@Override
	public String toString() {
		String s = "Picture\nid=" + id + ", num=" + num + ", sum=" + sum+"\n";
		for(int k:idtomeet.keySet()) {
			s+= k+" : "+idtomeet.get(k)+"\n";
		}
		return  s;
	}
	
}

public class Main {
	static Scanner scan = new Scanner(System.in);
	static int N,ididx,SUM;
	static int[][] artintmap;
	static Picture[][] picmap;
	static ArrayList<Picture> piclst;
	static int[][] makedir = {{1,0},{-1,0},{0,1},{0,-1}};
	static int[][] measuredir = {{1,0},{0,1}};
	public static void printartintmap() {
		System.out.println("printartintmap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				System.out.print(artintmap[i][j]+" ");
			}
			System.out.println();
		}
	}
	public static void printpicmap() {
		System.out.println("printpicmap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(picmap[i][j]==null) {
					
				}else {
					System.out.print(picmap[i][j].num+" ");
					
				}
			}
			System.out.println();
		}
	}
	public static void printpiclst() {
		System.out.println("printpiclst");
		for(Picture p:piclst) {
			System.out.println(p.toString());
		}
	}
	
	public static void main(String[] args) {
		
		N = scan.nextInt();
		artintmap = new int[N][N];
		SUM=0;
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				artintmap[i][j] = scan.nextInt();
			}
		}
		getartpoint();
		
//		printartintmap();
//		printpicmap();
//		printpiclst();
		
//		System.out.println("flag"+0+" : "+SUM);
		
		for(int i=0;i<3;i++) {
			pictureturn();
			getartpoint();
			
//			printartintmap();
			
//			System.out.println("flag"+(i+1)+" : "+SUM);
			
		}
		
		System.out.println(SUM);

	}

	private static void pictureturn() {
		//십자가
		int mid = N/2;
		//회전되었을 때 row,col
		
		int[] row = new int[N];
		int[] col = new int[N];
		
		for(int i=0;i<N;i++) {
			row[i]=artintmap[i][mid];
			col[N-1-i] = artintmap[mid][i];
		}
		
		for(int i=0;i<N;i++) {
			artintmap[mid][i] = row[i];
			artintmap[i][mid] = col[i];
		}
		
		//네개의 사각형
		int[] minr = {0,mid+1,0,mid+1};
		int[] maxr = {mid-1,N-1,mid-1,N-1};
		
		int[] minc = {0,0,mid+1,mid+1};
		int[] maxc = {mid-1,mid-1,N-1,N-1};
		for(int i=0;i<4;i++) {
			fillartmap(minr[i],minc[i],maxr[i],maxc[i]);
		}
		
		
		
		// TODO Auto-generated method stub
		
	}

	private static void fillartmap(int minr, int minc, int maxr, int maxc) {
		int n = N/2;
		int[][] tempmap = new int[n][n];
		for(int r=0;r<n;r++) {
			for(int c=0;c<n;c++) {
				tempmap[c][n-1-r]=artintmap[minr+r][minc+c];
			}
		}
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				artintmap[minr+i][minc+j] = tempmap[i][j];
			}
		}
		
	}
	private static void getartpoint() {
		picmap = new Picture[N][N];
		piclst = new ArrayList<>();
		ididx=1;
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<N;j++) {
				if(picmap[i][j] == null) {
					makepic(i,j,ididx++);
				}
				for(int[] dir:measuredir) {
					int r = i+dir[0];
					int c = j+dir[1];
					if(r<0||r>=N||c<0||c>=N) continue;
					if(picmap[r][c]==null) {
						makepic(r,c,ididx++);
					}
					if(picmap[r][c].id==picmap[i][j].id) continue;
					HashMap<Integer,Integer> hm = picmap[i][j].idtomeet;
					if(hm.containsKey(picmap[r][c].id)) {
						hm.put(picmap[r][c].id,hm.get(picmap[r][c].id)+1);
					}else {
						hm.put(picmap[r][c].id, 1);
					}
					
				}
				
			}
		}
		
		int maxint = piclst.size()-1;
		for(int i=0;i<maxint;i++) {
			for(int j=i+1;j<=maxint;j++) {
				addscore(piclst.get(i),piclst.get(j));
			}
		}
		
		
	}

	private static void addscore(Picture p1, Picture p2) {
		
		int met = 0;
		if(p1.idtomeet.containsKey(p2.id)) {
			met+=p1.idtomeet.get(p2.id);
		}
		
		if(p2.idtomeet.containsKey(p1.id)) {
			met+=p2.idtomeet.get(p1.id);
		}
		
		int res = (p1.sum+p2.sum)*p1.num*p2.num*met;
		SUM+=res;
		
	}
	private static void makepic(int i, int j, int ididx) {
		int num = artintmap[i][j];
		Picture p = new Picture(ididx,num);
		picmap[i][j] = p;
		Queue<Pair> que = new LinkedList<>();
		que.add(new Pair(i,j));
		boolean[][] visited = new boolean[N][N];
		
		while(!que.isEmpty()) {
			Pair pair = que.poll();
			int r = pair.r;
			int c = pair.c;
			if(visited[r][c]) continue;
			visited[r][c] = true;
			picmap[r][c].sum+=1;
			for(int[] dir:makedir) {
				int nextr = r+dir[0];
				int nextc = c+dir[1];
				if(nextr<0||nextr>=N||nextc<0||nextc>=N||visited[nextr][nextc]) continue;
				if(artintmap[nextr][nextc]!=num) continue;
				picmap[nextr][nextc] = p;
				que.add(new Pair(nextr,nextc));
			}
			
		}
		piclst.add(p);
		
		
		
	}

}