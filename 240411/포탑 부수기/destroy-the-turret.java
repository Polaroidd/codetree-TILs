import java.util.*;
class Dir{
	int r,c;

	public Dir(int r, int c) {
		this.r = r;
		this.c = c;
	}
	
}

class Canons implements Comparable<Canons>{
	int idx,r,c,attack;
	int timestamp = 0;

	Canons( int r, int c, int attack) {
		this.r = r;
		this.c = c;
		this.attack = attack;
	}

	@Override
	public int compareTo(Canons other) {
		if(this.attack!=other.attack) return this.attack-other.attack;
		if(this.timestamp!=other.timestamp) return other.timestamp-this.timestamp;
		int rc = this.r+this.c;
		int orc = other.r+other.c;
		if(rc!=orc) return orc-rc;
		return other.c-this.c;
		}
	
}


public class Main {
	static Scanner scan = new Scanner(System.in);
	static int N,M,K;
	static int[][] bombdir = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
	static int[][] laserdir = {{0,1},{1,0},{0,-1},{-1,0}};
	static ArrayList<Canons> canonlst = new ArrayList<>();
	static boolean[][] cracked;
	static Canons[][] canonmap;
	static int timestamp = 1;
	
	
	public static void printcanonmap() {
		System.out.println("printcanonmap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				if(canonmap[i][j]==null) {
					System.out.print("- ");
					continue;
					
				}
				System.out.print(canonmap[i][j].attack+" ");
				
			}
			System.out.println();
		}
	}
	public static void printcrackedmap() {
		System.out.println("printcrackedmap");
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				if(cracked[i][j]) {
					System.out.print("1 ");
					
				}else {
					System.out.print("1 ");
					
				}
				
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		N = scan.nextInt();
		M = scan.nextInt();
		K = scan.nextInt();
		cracked = new boolean[N][M];
		canonmap = new Canons[N][M];
		
		
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) {
				int n = scan.nextInt();
				if(n==0) {
					cracked[i][j] = true;
				}else {
					Canons c = new Canons(i,j,n);
					canonmap[i][j] = c;
					canonlst.add(c);
				}
			}
		}
		
		canonlst.sort(null);
//		for(Canons c:canonlst) {
//			System.out.println(c.attack+" "+c.r+" "+c.c+" "+c.timestamp);
//		}
		// printcrackedmap();
		// printcanonmap();
		
		for(int i=1;i<=K;i++) {
			if(canonlst.size()<=1) {
				break;
			}
			boolean[][] attacked = new boolean[N][N];
			
			Canons attacker = canonlst.get(0);
			Canons target = canonlst.get(canonlst.size()-1);
			
			attacked[attacker.r][attacker.c] = true;
			attacker.timestamp = timestamp++;
			
			attacker.attack+=(N+M);
			if(!laserattack(attacker,target,attacked)) {
				canonattack(attacker,target,attacked);
			}
			if(canonlst.size()<=1) break;
			
			for(int x=0;x<N;x++) {
				for(int y=0;y<M;y++) {
					if(attacked[x][y]||cracked[x][y]) continue;
					canonmap[x][y].attack+=1;
				}
			}
			
			canonlst.sort(null);
			
			// System.out.println("Round : "+i);
			// printcrackedmap();
			// printcanonmap();
			
		}
		System.out.println(canonlst.get(canonlst.size()-1).attack);
		
		
		
		
		
		

	}
	private static void canonattack(Canons attacker, Canons target,boolean[][] attacked) {
		attacked[target.r][target.c] = true;
		
		int power = attacker.attack;
		if(power<target.attack) {
			target.attack-=power;
		}else {
			target.attack = 0;
			cracked[target.r][target.c]=true;
		}
		for(int[] dir:bombdir) {
			int nr = (target.r+dir[0]+N)%N;
			int nc = (target.c+dir[1]+N)%N;
			if(cracked[nr][nc]||(nr==attacker.r&&nc==attacker.c)) continue;
			
			
			attacked[nr][nc] =true;
			int pow = power/2;
			if(pow<canonmap[nr][nc].attack) {
				canonmap[nr][nc].attack-=pow;
			}else {
				canonmap[nr][nc].attack = 0;
				cracked[canonmap[nr][nc].r][canonmap[nr][nc].c]=true;
				canonlst.remove(canonmap[nr][nc]);
			}
			
		}
		
		
	}
	private static boolean laserattack(Canons attacker, Canons target,boolean[][] attacked) {
		int r = attacker.r;
		int c = attacker.c;
		int power = attacker.attack;
		Queue<Dir> que = new LinkedList<>();
		que.add(new Dir(r,c));
		boolean[][] visited = new boolean[N][N];
		HashMap<Dir,Dir> hm = new HashMap<>();
		boolean res = false;
		Dir nextd=null;
		loop:
		while(!que.isEmpty()) {
			Dir d = que.poll();
			if(visited[d.r][d.c]) continue;
			visited[d.r][d.c] = true;
			
			
			for(int[] dir:laserdir) {
				int nextr = (d.r+dir[0]+N)%N;
				int nextc = (d.c+dir[1]+N)%N;
				if(cracked[nextr][nextc]||visited[nextr][nextc]) continue;
				nextd = new Dir(nextr,nextc);
				que.add(nextd);
				hm.put(nextd, d);
				
				if(nextr==target.r&&nextc==target.c) {
					//발견
					res = true;
					break loop;
				}
				
			}
			
		}
		if(res) {
			while(nextd.r!=attacker.r||nextd.c!=attacker.c) {
				Canons canon = canonmap[nextd.r][nextd.c];
				attacked[nextd.r][nextd.c] = true;
				int att = power;
				if(canon!=target) {
					att = power/2;
				}
				
				if(att<canon.attack) {
					canon.attack-=att;
				}else {
					canon.attack = 0;
					cracked[canon.r][canon.c]=true;
					canonmap[canon.r][canon.c] = null;
					canonlst.remove(canon);
				}
				
				nextd = hm.get(nextd);
			}
		}
		
		
		return res;
	}

}