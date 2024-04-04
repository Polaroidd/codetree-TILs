import java.util.*;

class Santa implements Comparable<Santa> {
    int r,c,d,score,idx;
    Santa(int r,int c,int d,int score,int idx){
        this.r = r;
        this.c = c;
        this.d = d;
        this.score = score;
        this.idx = idx;
    }
    @Override
    public int compareTo(Santa other){
        if(this.d!=other.d) return this.d-other.d;
        else if(this.r!=other.r) return other.r-this.r;
        return other.c-this.c;
    }



}
public class Main {
    static int N,M,P,C,D;
    static Scanner scan = new Scanner(System.in);
    static Santa[] santalst;
    static int[] stopped;
    static ArrayList<Santa> arrlst;
    static int[][] santamap;
    static int[][] dir = {{-1,0},{1,0},{0,-1},{0,1}};
    static int time;
    static boolean[] removed;
    
    

    static int dolfr,dolfc;

    public static int getdistance(int r1,int c1,int r2,int c2){

        return (int)Math.pow(r1-r2,2)+(int)Math.pow(c1-c2,2);
    }
    public static void printmap(){
        System.out.println("dolf : "+dolfr+" "+dolfc);
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(i==dolfr&&j==dolfc){
                    System.out.print("-1 ");
                    continue;
                }
                System.out.print(santamap[i][j]+" ");
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        //init
        N = scan.nextInt();
        M = scan.nextInt();
        P = scan.nextInt();
        C = scan.nextInt();
        D = scan.nextInt();
        dolfr = scan.nextInt()-1;
        dolfc = scan.nextInt()-1;
        santalst = new Santa[P+1];
        stopped = new int[P+1];
        arrlst = new ArrayList<>();
        santamap = new int[N][N];
        removed = new boolean[P+1];

        for(int i=1;i<=P;i++){
            scan.nextInt();
            int r = scan.nextInt()-1;
            int c = scan.nextInt()-1;
            Santa s = new Santa(r,c,getdistance(r,c,dolfr,dolfc),0,i);
            // System.out.println(r+" "+c+" "+dolfr+" "+dolfc+" "+getdistance(r,c,dolfr,dolfc));
            arrlst.add(s);
            santalst[i] = s;
            santamap[r][c] = i;
        }
        arrlst.sort(null);
        // printmap();
        //init done
        time=1;
        // printmap();
        while(time<=M){
        //dolf move
        dolfmove();
        if(arrlst.size() <=0) break; //모든 산타 쫓겨남

        //santa move
        santamove();

        if(arrlst.size() <=0) break; //모든 산타 쫓겨남
        for(int i=1;i<=P;i++){
            if(!removed[i]){
                santalst[i].score+=1;
            }
        }
        // printres();
        // printmap();
        time++;
        }


        //result;
        for(int i=1;i<=P;i++){
            System.out.print(santalst[i].score+" ");
            
        }
        System.out.println();

        // printmap();
        
        
        
    }
    public static void printres(){
        for(int i=1;i<=P;i++){
            System.out.print(santalst[i].score+" ");
        }
            System.out.println();
    }
    public static void santamove(){

        for(int i=1;i<=P;i++){
            if(arrlst.size() <=0) return; //모든 산타 쫓겨남
            if(removed[i]) continue;
            Santa s = santalst[i];

            if(stopped[s.idx]!=0){
                if(time==stopped[s.idx]+1) stopped[s.idx] = 0;
                continue;
            }
            int mind = Integer.MAX_VALUE;
            int minx=0;
            int miny=0;
            int[] tmp = {0,0};

            int now_d = getdistance(s.r,s.c,dolfr,dolfc);

            for(int[] temp:dir){
                int x = s.r+temp[0];
                int y = s.c+temp[1];
                if(x<0||x>=N||y<0||y>=N||santamap[x][y]>0){
                    continue;
                }
                int next_d = getdistance(x,y,dolfr,dolfc);
                if(next_d<mind){
                    mind = next_d;
                    minx = x;
                    miny = y;
                    tmp[0] = temp[0];
                    tmp[1] = temp[1];
                }
                
            }


            int x = minx;
            int y = miny;

            if(mind<now_d){
                if(x==dolfr&&y == dolfc){
                    stopped[s.idx] = time;
                    s.score +=D;
                    int revx = tmp[0]*-1;
                    int revy = tmp[1]*-1;
                    pushed(s,dolfr+revx*D,dolfc+revy*D,revx,revy);
                    continue;
                }
                santamap[s.r][s.c] = 0;
                santamap[x][y] = s.idx;
                s.r = x;
                s.c = y;
                s.d = mind;
            }else{
                s.d = now_d;
            }

            

        }
        arrlst.sort(null);

    }

    public static void dolfmove(){
        Santa s = arrlst.get(0);

        int x = getdir(dolfr,s.r);
        int y = getdir(dolfc,s.c);
        dolfr+=x;
        dolfc+=y;
        if(dolfr == s.r && dolfc == s.c){
            int xx = x*C+s.r;
            int yy = y*C+s.c;
            s.score+=C;
            stopped[s.idx] = time;
            
            pushed(s,xx,yy,x,y);

        }


    }
    public static void pushed(Santa s,int r,int c,int r2,int c2){ //밀기
        if(r<0||r>=N||c<0||c>=N){
                removeSanta(s);
                return;
        }
        if(santamap[r][c]>0){
            pushed(santalst[santamap[r][c]],r+r2,c+c2,r2,c2);
        }
        santamap[s.r][s.c] = 0;
        s.r = r;
        s.c = c;
        santamap[r][c] = s.idx;
        s.d = getdistance(r,c,dolfr,dolfc);
    }
    public static void removeSanta(Santa s){
        removed[s.idx] = true;
        santamap[s.r][s.c] = 0;
        arrlst.remove(s);

    }
    public static int getdir(int from,int to){
        if(from == to) return 0;
        if(from<to) return 1;
        return -1;
    }
}