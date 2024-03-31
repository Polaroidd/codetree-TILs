import java.util.*;

class Canon implements Comparable<Canon>{
    int r;
    int c;
    int power;
    int timestamp=0;
    Canon(int row,int col,int power){
        r=row;
        c = col;
        this.power = power;

    }
    @Override
    public int compareTo(Canon other){
        if(this.power != other.power){
            return Integer.compare(this.power,other.power);

        }else if(this.timestamp!=other.timestamp){
            return Integer.compare(other.timestamp,this.timestamp);

        }else if(this.r+this.c!=other.r+other.c){
            return Integer.compare(other.r+other.c,this.r+this.c);

        }else{
            return Integer.compare(other.c,this.c);

        }

    }
    


}

public class Main {
    static Scanner scan = new Scanner(System.in);
    static ArrayList<Canon> canonlst;
    static boolean[][] cracked;
    static int N,M,K;
    static Canon[][] canonmap;
    static int[][] bombdir = {{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1}};
    static int[][] laserdir = {{0,1},{-1,0},{0,-1},{1,0}};
    static boolean[][] attacked;
    static boolean[][] visited;
    static int timestamp;
    public static boolean laserattack(Canon att,Canon def){
        
        visited[att.r][att.c] = true;
        Queue<int[]> que = new LinkedList<>();
        HashMap<int[],int[]> prev = new HashMap<>();
        int fordif = att.power;
        int[] prevdir = {att.r,att.c};
        que.add(prevdir);
        while(!que.isEmpty()){
            prevdir = que.poll();
            visited[prevdir[0]][prevdir[1]] = true;
            if(prevdir[0]==def.r&&prevdir[1]==def.c){
                canonmap[prevdir[0]][prevdir[1]].power-=fordif;
                attacked[prevdir[0]][prevdir[1]]=true;
                while(prev.containsKey(prevdir)){
                    prevdir = prev.get(prevdir);
                    
                    if(prevdir[0]==att.r && prevdir[1]==att.c){
                        break;
                    }
                    attacked[prevdir[0]][prevdir[1]]=true;
                    canonmap[prevdir[0]][prevdir[1]].power-=(fordif/2);

                }

                return true;
            }
            for(int[] temp:laserdir){
                int x = (prevdir[0]+temp[0]+N)%N;
                int y = (prevdir[1]+temp[1]+M)%M;
                int[] nextdir = {x,y};
                if(visited[x][y]||cracked[x][y]){
                    continue;
                }
                
                prev.put(nextdir,prevdir);
                que.add(nextdir);

            }

        }


        
        return false;
    }
    public static void bombattack(Canon att,Canon def){

        def.power-=att.power;

        int r = def.r;
        int c = def.c;

        attacked[r][c] = true;

        int fordif = att.power/2;
        for(int[] temp:bombdir){
            int x = (r+temp[0]+N)%N;
            int y = (c+temp[1]+M)%M;
            if(cracked[x][y]||(x==att.r&&y==att.c)){
                continue;
            }
            attacked[x][y] = true;
            canonmap[x][y].power-=(att.power/2);

        }

    }

    public static void Attack(){
        timestamp++;


        Canon attacker = canonlst.get(0);
        attacker.timestamp = timestamp;
        Canon defencer = canonlst.get(canonlst.size()-1);
        attacker.power+=(N+M);
        attacked = new boolean[N][M];
        visited = new boolean[N][M];
        attacked[attacker.r][attacker.c] = true;
        // System.out.println("attacker : "+attacker.r+" "+attacker.c);
        // System.out.println("defencer : "+defencer.r+" "+defencer.c);

        if(!laserattack(attacker,defencer)){//laser attack
            bombattack(attacker,defencer);   
        }


        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                if(!cracked[i][j]){
                    if(!attacked[i][j]){
                    canonmap[i][j].power+=1;

                    }else{
                        if(canonmap[i][j].power<=0){
                            canonmap[i][j].power = 0;
                            cracked[i][j] = true;
                        }
                    }
                    
                }
            }
        }

        canonlst.sort(null);
        
        while(true){
            if(canonlst.size()==0){
                break;
            }
            Canon c = canonlst.get(0);
            if(c.power>0){
                break;
            }
            cracked[c.r][c.c] = true;
            canonlst.remove(0);
        }
    }
    public static void main(String[] args) {
        N = scan.nextInt();
        M = scan.nextInt();
        K = scan.nextInt();
        canonlst = new ArrayList<>();
        cracked = new boolean[N][M];
        canonmap = new Canon[N][M];
        timestamp = 0;

        for(int i=0;i<N;i++){
            for(int j=0;j<M;j++){
                int t = scan.nextInt();
                if(t==0){
                    cracked[i][j] = true;

                }else{
                    Canon c = new Canon(i,j,t);
                    canonlst.add(c);
                    canonmap[i][j] = c;
                }

            }
        }
        
        for(int k=0;k<K;k++){
            canonlst.sort(null);
            if(canonlst.size()<=1){
                break;
            }
            
            Attack();
       
        }
        // System.out.println(timestamp);
        // for(int i=0;i<N;i++){
        //     for(int j=0;j<M;j++){
        //         if(cracked[i][j]){
        //         System.out.print(0+" ");
        //         continue;
                    
        //         }
        //         System.out.print(canonmap[i][j].power+" ");

        //     }
        //     System.out.println();
        // }
        if(canonlst.size()==0){
        System.out.println(0);

        }else{
        System.out.println(canonlst.get(canonlst.size()-1).power);

        }
        // 여기에 코드를 작성해주세요.
    }
}