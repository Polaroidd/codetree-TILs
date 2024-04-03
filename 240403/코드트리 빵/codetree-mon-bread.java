import java.util.*;

class Node implements Comparable<Node>{
    int r,c,t;

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
    static boolean[][] storemap;
    static boolean[] arrived;//시간 넘치면 set으로 사용자 넣어놓고 한명씩 빼도 돼
    static Queue<Node>[] quelst;



    public static void main(String[] args) {
        N = scan.nextInt();
        M = scan.nextInt();
        hs = new HashSet<>();
        storelst = new Node[M];
        quelst = new LinkedList[M];
        storemap = new boolean[N][N];
        wallmap = new boolean[N][N];
        arrived = new boolean[M];
        
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(scan.nextInt()==1){
                    storemap[i][j] = true;
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

            if(arrived(time)){
                System.out.println(time+1);
                break;
            }//도착확인 - wallmap 최신화
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
            if(!arrived[i]){
                int n = quelst[i].peek().t;
                loop:
                while(n==quelst[i].peek().t){
                    Node nde = quelst[i].poll();
                    for(int[] temp:dir){
                        int rr = temp[0]+nde.r;
                        int cc = temp[1]+nde.c;
                        if(rr<0||rr>=N||cc<0||cc>=N||wallmap[rr][cc]){
                            continue;
                        }
                        if(rr == storelst[i].r&&cc == storelst[i].c){
                            arrived[i] = true;
                            break loop;
                        }
                        quelst[i].add(new Node(rr,cc,time));
                        
                    }
                }
            }
        }
    }
    static boolean arrived(int time){
        // System.out.println("arrived : "+time);
        int res = 0;
        for(int i=0;i<M;i++){
            if(arrived[i]){
                // System.out.print(i);
                wallmap[storelst[i].r][storelst[i].c] = true;
            }else{
                res++;
            }
        }
        // System.out.println();
        if(res == 0){
            return true;
        }else{
            return false;
        }
    }
    static void basecamp(int time){

        Node nde = storelst[time];
        Node res = bfs(nde,time);
        // System.out.println("bfs done");
        wallmap[res.r][res.c] = true;
        quelst[time] = new LinkedList<>();
        quelst[time].add(new Node(res.r,res.c,time));
        // System.out.println(nde.r+" "+nde.c);

    }
    static Node bfs(Node nde, int idx){
        // System.out.println("bfs : "+nde.r+" "+nde.c);
        Queue<Node> que = new LinkedList<>();
        que.add(nde);
        while(!que.isEmpty()){
            Node n = que.poll();
            for(int[] temp:dir){
                int r = temp[0]+n.r;
                int c = temp[1]+n.c;
                if(r<0||r>=N||c<0||c>=N||wallmap[r][c]){
                    continue;
                }
                if(storemap[r][c]){
                    storemap[r][c] = false;
                    return new Node(r,c,idx);
                }
                que.add(new Node(r,c,0));

            }
        }
        return new Node(-1,-1,-1);

    }
}