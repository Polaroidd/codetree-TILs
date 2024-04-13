import java.util.*;
class Present{
	int idx;
	int belt = 0;
	Present front,back;
	
	Present(int idx){
		this.idx = idx;
		front = null;
		back = null;
	}
}


public class Main {
	static Scanner scan = new Scanner(System.in);
	static int Q,N,M;
	static Present[] presentlst, headlst,taillst;
	static int[] numlst;
	
	
	public static void main(String[] args) {
		Q = scan.nextInt();
		
		for(int i=1;i<=Q;i++) {//Q까지 가야해
			
			int code = scan.nextInt();
			
//			System.out.println("Round : "+i+" "+code);
			switch(code) {
			case 100:
				init();
				break;
			case 200:
				movestocks();
				break;
			case 300:
				changehead();
				break;
			case 400:
				divide();
				break;
			case 500:
				getinfo();
				break;
			case 600:
				getbeltinfo();
				break;
				
			}
//			System.out.println("idx : "+presentlst[2].back.idx);
//			printstate();
		}
		

	}
	public static void printstate() {
		System.out.println("printbeltstate\n");
		for(int i=1;i<=N;i++) {
			if(headlst[i]==null) {
				System.out.print("- ");
			}else {
				Present p = headlst[i];
				System.out.print(p.idx+",");
				while(p!=taillst[i]) {
					p = p.back;
					System.out.print(p.idx+",");
				}
				System.out.print(" ");
			}
		}
		System.out.println();
		System.out.println();
		for(int i=1;i<=N;i++) {
			System.out.print(numlst[i]+" ");
		}
		System.out.println();
	}

	private static void init() {//100
		N = scan.nextInt();
		M = scan.nextInt();
		presentlst = new Present[M+1];
		headlst = new Present[N+1];
		taillst = new Present[N+1];
		numlst = new int[N+1];
		
		for(int i=1;i<=M;i++) {
			int bnum = scan.nextInt();
			Present p = new Present(i);
			presentlst[i] = p;
			if(headlst[bnum]==null) {
				//첫 추가
				headlst[bnum] = p;
				taillst[bnum] = p;
				p.front = p;
				p.back = p;
			}else {
				
				taillst[bnum].back = p;
				
				p.front = taillst[bnum];
				
				p.back = p;
				
				taillst[bnum] = p;
				
				//이미 있음
				
			}
			numlst[bnum]+=1;
		}
//		printstate();
	}


	private static void movestocks() {//200
		int from = scan.nextInt();
		int to = scan.nextInt();
		if(headlst[from]==null) {
			return;
		}
		if(headlst[to]==null) {//가려는곳이 비었어
			//복사
			headlst[to] = headlst[from];
			taillst[to] = taillst[from];
			numlst[to] = numlst[from];
			
			//head가 어느 belt 가리키는지 update
			headlst[to].belt = to;
			
			numlst[from] = 0;
			headlst[from] = null;
			taillst[from] = null;
			
		}else {
			//headlst[to]의 head를 from-head로, front를 from-tail로, from-tail의 back을 to-head로
			
			headlst[to].front = taillst[from];
			taillst[from].back = headlst[to];
			
			headlst[to] = headlst[from];
			//taillst는 그대로
			
			numlst[to] += numlst[from];
			
			//초기화
			numlst[from] = 0;
			headlst[from] = null;
			taillst[from] = null;
			
			
			
		}
		System.out.println(numlst[to]);
		// TODO Auto-generated method stub
		
	}


	private static void changehead() {//300
		int belta = scan.nextInt();
		int beltb = scan.nextInt();
		int resbelt = beltb;
		if(headlst[belta]==null&&headlst[beltb]==null) {
			System.out.println(numlst[beltb]);
			return;
		}
		
		//무조건 개수 큰게 belta
		if(numlst[belta]<numlst[beltb]) {
			//무조건 null은 b!
			int temp = beltb;
			beltb = belta;
			belta = temp;
		}
		
		if(headlst[beltb]==null) {//교환대상이 비어있어
			headlst[beltb] = headlst[belta];
			taillst[beltb] = headlst[belta];
			headlst[beltb].belt = beltb;
			numlst[belta]-=1;
			numlst[beltb]+=1;
			if(headlst[belta] == taillst[belta]) {//head밖에 없어
				headlst[belta] = null;
				taillst[belta] = null;
			}else {
				//바로 뒤에걸 head로 바꿔주고 앞을 비워줘
				headlst[belta] = headlst[belta].back;
				headlst[belta].front = headlst[belta];
				
				//head를 나로 바꿔줘
			}
			
			headlst[beltb].front = headlst[beltb];
			headlst[beltb].back = headlst[beltb];
			
		}else {
			if(numlst[beltb]==1) {
				if(numlst[belta]==1) {
					//둘다 개수가 1개, case3
					Present beltbpresent = headlst[beltb];
					//beltb 덮어씌우기
					headlst[beltb] = headlst[belta];
					taillst[beltb] = headlst[belta];
					
					headlst[beltb].belt = beltb;
					
					//belta 덮어씌우기
					
					headlst[belta] = beltbpresent;
					taillst[belta] = beltbpresent;
					
					headlst[belta].belt = belta;
				}else {
					
					//beltb만 1개
					Present beltbpresent = headlst[beltb];
					//beltb 덮어씌우기
					headlst[beltb] = headlst[belta];
					taillst[beltb] = headlst[belta];
					
					headlst[beltb].belt = beltb;
					
					
					beltbpresent.back = headlst[belta].back;
					
					//뒷 node의 head와 
					headlst[belta].back.front = beltbpresent;
					
					
					headlst[belta] = beltbpresent;
					headlst[belta].belt = belta;
					
					headlst[beltb].back = headlst[beltb];
//					return;
					
				}
			}else {

				Present beltbpresent = headlst[beltb];
				Present beltaback = headlst[belta].back;
				
				//belta link 연결
				headlst[beltb].back.front = headlst[belta];
				headlst[belta].back = headlst[beltb].back;
				headlst[beltb] = headlst[belta];
				
				//숫자갱신
				
				headlst[beltb].belt = beltb;
				
				
				//beltb linke 연결
				beltbpresent.back = beltaback;
				beltaback.front = beltbpresent;
				
				headlst[belta] = beltbpresent;
				headlst[belta].belt = belta;
				
			}
			
			
			
		}
		System.out.println(numlst[resbelt]);
		
		// TODO Auto-generated method stub
		
	}


	private static void divide() {//400
		// TODO Auto-generated method stub
		int from = scan.nextInt();
		int to = scan.nextInt();
		if(numlst[from]<=1) return;
		
		int fordiv = numlst[from]/2;
		
		int nodenum = 1;
		Present mid = headlst[from];
		while(true) {
			if(nodenum==fordiv) break;
			mid = mid.back;
			nodenum++;
		}
		Present head = headlst[from];
		Present nexthead = mid.back;
		
		headlst[from] = nexthead;
		nexthead.front = nexthead;
		nexthead.belt = from;
		
		if(headlst[to]==null) {
			headlst[to] = head;
			head.belt = to;
			taillst[to] = mid;
			mid.back = mid;
		}else {
			headlst[to].front = mid;
			mid.back = headlst[to];
			headlst[to] = head;
			headlst[to].belt = to;
		}
		numlst[from]-=fordiv;
		numlst[to]+=fordiv;
		System.out.println(numlst[to]);
		
		
		
		
		
		
		
		
		
		
		
		
	}


	private static void getinfo() {//500
//		printstate();
		// TODO Auto-generated method stub
		int pnum = scan.nextInt();
		Present p = presentlst[pnum];
//		System.out.println(p.idx);
		int res = 0;
		
		if(p.front!=p) {
			res+=p.front.idx;
		}else {
			res-=1;
		}
		
		if(p.back!=p) {
			res+= p.back.idx*2;
		}else {
			res-=2;
		}
		
		System.out.println(res);
	}


	private static void getbeltinfo() {//600
		// TODO Auto-generated method stub
		int bnum = scan.nextInt();
		
		if(numlst[bnum]==0) {
			System.out.println(-3);
			
		}else {
			System.out.println(headlst[bnum].idx+taillst[bnum].idx*2+numlst[bnum]*3);
		}
	}

}