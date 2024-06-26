import java.util.*;


class Rabbit implements Comparable<Rabbit>{
	int pid,d,r,c;
	int num=0;

	public Rabbit(int pid, int d, int r, int c) {
		this.pid = pid;
		this.d = d;
		this.r = r;
		this.c = c;
	}

	@Override
	public int compareTo(Rabbit other) {
		if(this.num!=other.num) return this.num-other.num;
		else if(this.r+this.c!=other.r+other.c) return (this.r+this.c)-(other.r+other.c);
		else if(this.r!=other.r) return this.r-other.r;
		else if(this.c!=other.c) return this.c-other.c;
		return this.pid - other.pid;
		}
	
	
}


public class Main {
	static int Q,N,M,P,SUM;
	static int[] scorelst;
	static Scanner scan = new Scanner(System.in);
	static HashMap<Integer,Integer> idmap;
	static PriorityQueue<Rabbit> pq;
	static Rabbit[] rabbitlst;
	static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Q = scan.nextInt();
		
		for(int i=0;i<Q;i++) {
			int com = scan.nextInt();
			switch(com) {
			case 100:
				init();
				break;
			case 200:
				start();
				break;
			case 300:
				distchange();
				break;
			case 400:
				select();
				break;
			}
			
			
		}
		
	}
	private static void init() {
		N = scan.nextInt();
		M = scan.nextInt();
		P = scan.nextInt();
		rabbitlst = new Rabbit[P];
		pq = new PriorityQueue<>();
		idmap = new HashMap<>();
		scorelst = new int[P];
		SUM=0;
		
		for(int i=0;i<P;i++) {
			int pid = scan.nextInt();
			idmap.put(pid, i);
			Rabbit rab = new Rabbit(pid,scan.nextInt(),0,0);
			rabbitlst[i] = rab;
			pq.add(rab);
		}
	}
	private static void start() {
		ArrayList<Rabbit> arrlst = new ArrayList<>();
		boolean[] added = new boolean[P];
		
		int K = scan.nextInt();
		int S = scan.nextInt();
		for(int i=0;i<K;i++) {
			Rabbit rab = pq.poll();
			rab.num+=1;
			int resx = -1;
			int resy = -1;
			for(int[] temp:dir) {
				int r = rab.r;
				int c = rab.c;
				int x = temp[0];
				int y = temp[1];
				for(int j=0;j<rab.d;j++) {//시간 줄이기 가능
					if(r+x<0||c+y<0||r+x>=N||c+y>=M) {
						x*=-1;
						y*=-1;
					}
					r+=x;
					c+=y;
					
				}
				if(r+c==resx+resy) {
					if(r>resx) {
						resx= r;
						resy =c;
						
					}else {
						if(c>resy) {
							resx= r;
							resy =c;
							
						}
					}
				}else if(r+c>resx+resy){
					resx= r;
					resy =c;
					
				}
			}
			rab.r =resx;
			rab.c = resy;
			SUM+=resx+resy+2;
			scorelst[idmap.get(rab.pid)]-=resx+resy+2;
			pq.add(rab);
			if(!added[idmap.get(rab.pid)]) {
				arrlst.add(rab);
				added[idmap.get(rab.pid)] = true;
			}
			
		}
		Collections.sort(arrlst, new Comparator<Rabbit>() {
            @Override
            public int compare(Rabbit rabbit1, Rabbit rabbit2) {
                if (rabbit1.r + rabbit1.c != rabbit2.r + rabbit2.c)
                    return Integer.compare(rabbit1.r + rabbit1.c, rabbit2.r + rabbit2.c);
                else if (rabbit1.r != rabbit2.r)
                    return Integer.compare(rabbit1.r, rabbit2.r);
                else if (rabbit1.c != rabbit2.c)
                    return Integer.compare(rabbit1.c, rabbit2.c);
                return Integer.compare(rabbit1.pid, rabbit2.pid);
            }
        });
		scorelst[idmap.get(arrlst.get(arrlst.size()-1).pid)] +=S;
		
		
		// TODO Auto-generated method stub
		
	}
	private static void distchange() {
		int pid = scan.nextInt();
		int L = scan.nextInt();
		rabbitlst[idmap.get(pid)].d*=L;
		
		
	}
	private static void select() {
		
		int res = SUM;
		int max = Integer.MIN_VALUE;
		for(int i=0;i<scorelst.length;i++) {
			if(scorelst[i]>max) {
				max = scorelst[i];
			}
		}
		System.out.println(res+max);
		
	}
	
	

}