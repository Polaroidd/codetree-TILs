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
        if(this.d!=other.d) return other.d-this.d;
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
    
    

    static int dolfr,dolfc;

    public static int getdistance(int r1,int c1,int r2,int c2){

        return Math.abs(r1-r2)+Math.abs(c1-c2);
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

        for(int i=1;i<=P;i++){
            scan.nextInt();
            int r = scan.nextInt()-1;
            int c = scan.nextInt()-1;
            Santa s = new Santa(r,c,getdistance(r,c,dolfr,dolfc),0,i);
            arrlst.add(s);
            santalst[i] = s;
            santamap[r][c] = i;
        }
        arrlst.sort(null);
        printmap();
        //init done
        int time=0;
        while(thime<M){
        //dolf move
        dolfmove();
        if(arrlst.size() <=0) break; //모든 산타 쫓겨남

        //santa move
        santamove();
        time++;
        }


        //result;
        for(int i=1;i<=P;i++){
            System.out.print(santalst[i].score+" ");
        }
        
        
        
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
            
            pushed(s,xx,yy,x,y);

        }


    }
    public static void pushed(Santa s,int r,int c,int r2,int c2){ //밀기
        if(xx<0||xx>=N||yy<0||yy>=N){
                removeSanta(s);
                return;
        }
        if(santamap[r][c]>0){
            pushed(santalst[santamap[r][c]],r+r2,c+c2,r2,c2);
        }
        santampa[r][c] = s.idx;
    }
    public static int getdir(int from,int to){
        if(from == to) return 0;
        if(from<to) return 1;
        return -1;
    }
}