import java.util.*;

class Node implements Comparable<Node>{
    int r,c,t;
    Node parnode;

    Node (int r,int c, int t){
        this.r = r;
        this.c = c;
        this.t = t;
    }

    @Override
    public int compareTo(Node other){
        if(this.t!=other.t){
            return this.t-other.t;
        }else if(this.r!=other.r){
            return this.r-other.r;

        }else{
            return this.c-other.c;
        }

    }

}

public class Main {
    static int[][] dir = {{-1,0},{0,-1},{0,1},{1,0}};
    static int N,M;
    static boolean[][] wallmap;
    static Scanner scan = new Scanner(System.in);
    static HashSet<Node> hs;
    static Node[] storelst;
    static boolean[][] basemap;
    static boolean[] arrived;//시간 넘치면 set으로 사용자 넣어놓고 한명씩 빼도 돼

    static boolean[][][] visitedlst;
    static Node[] nowlst;

    public static void main(String[] args) {
        N = scan.nextInt();
        M = scan.nextInt();
        hs = new HashSet<>();
        storelst = new Node[M];

        basemap = new boolean[N][N];
        wallmap = new boolean[N][N];
        arrived = new boolean[M];
        nowlst = new Node[M];
        visitedlst = new boolean[M][N][N];
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(scan.nextInt()==1){
                    basemap[i][j] = true;
                }
            }
        }
        for(int i=0;i<M;i++){
            int r = scan.nextInt()-1;
            int c = scan.nextInt()-1;
            storelst[i] = new Node(r,c,0);
        }

        int time=0;

        while(true){
            move(time); //quelst iteration 이미 도착 - continue, que 비었어 - break;
            // System.out.println("move done");
        
            if(arrived(time)){
                System.out.println(time+1);
                break;
            }//도착확인 - wallmap 최신화
            // System.out.println("arrive done");
        
            if(time<M){
                basecamp(time);
            }
        

            time++;

        }
        
    }
    static void move(int time){
        // System.out.println("move : "+time);
    
        for(int i=0;i<time;i++){
            if(i>=M){
                break;
            }
            
            
            
            if(!arrived[i]){//&&nowlst[i]!=null
            	Node nde = nowlst[i];
            	Node next = bfs2(nde,i);
            	nowlst[i] = next;
            	
	            if(next.r == storelst[i].r&&next.c == storelst[i].c){
	                  arrived[i] = true;
	              }

                
            }
            
        }
    }
    static boolean arrived(int time){
        //  System.out.println("arrived : "+time);
        
        int res = 0;
        for(int i=0;i<M;i++){
            if(arrived[i]){
                //  System.out.print(i);
                wallmap[storelst[i].r][storelst[i].c] = true;
            }else{
                res++;
            }
        }
        //  System.out.println();
        //  System.out.println(res);
        
        if(res == 0){
            return true;
        }else{
            return false;
        }
    }
    static void basecamp(int time){
        
        Node nde = storelst[time];
        Node res = bfs(nde,time);
        // System.out.println("bfs done"+time);
        wallmap[res.r][res.c] = true;

        nowlst[time] = new Node(res.r,res.c,time);
        
        // System.out.println(nde.r+" "+nde.c);

    }
    static Node bfs(Node nde, int idx){
        boolean[][] visited = new boolean[N][N];


        Queue<Node> que = new LinkedList<>();
        que.add(nde);
        while(!que.isEmpty()){
            
            Node n = que.poll();
            if(visited[n.r][n.c]){
                continue;
            }
            visited[n.r][n.c] = true;
            
            // if(idx == 26){
            //     System.out.println(n.r+" "+n.c);
            // }
            for(int[] temp:dir){
                int r = temp[0]+n.r;
                int c = temp[1]+n.c;
                if(r<0||r>=N||c<0||c>=N||visited[r][c]){
                    continue;
                }
                if(basemap[r][c]){
                    basemap[r][c] = false;
                    wallmap[r][c] = true;
                    return new Node(r,c,idx);
                }
                if(wallmap[r][c]){
                    continue;
                }
                que.add(new Node(r,c,0));

            }
        }
        return new Node(-1,-1,-1);

    }
    static Node bfs2(Node nde, int idx){
    	boolean[][] visited = new boolean[N][N];
    	nde.parnode = nde;

        Queue<Node> que = new LinkedList<>();
        que.add(nde);
        while(!que.isEmpty()){
            
            Node n = que.poll();
            
            if(visited[n.r][n.c]){
                continue;
            }
            
            visited[n.r][n.c] = true;
            
            for(int[] temp:dir){
                int r = temp[0]+n.r;
                int c = temp[1]+n.c;
                if(r<0||r>=N||c<0||c>=N||visited[r][c]||wallmap[r][c]){
                    continue;
                }
                Node now_node = new Node(r,c,0);
            	now_node.parnode = n;       
                if(r==storelst[idx].r&&c==storelst[idx].c) {
                	while(now_node.parnode!=nde) {
                		now_node = now_node.parnode;
                	}
                	return new Node(now_node.r,now_node.c,0);
                }
                que.add(now_node);

            }
        }
        return new Node(-1,-1,-1);

    }
}