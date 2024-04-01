import java.util.*;
class present{
    int id;
    int w;
    present(int id,int w){
        this.id = id;
        this.w = w;
    }

}

public class Main {
    static Scanner scan = new Scanner(System.in);
    static int N, command;
    static int NUM,M,w_max;
    static HashMap<Integer,Integer> idmap = new HashMap<>();
    static Queue<present>[] belts;
    static boolean[] isRemoved,isBroken;
    static int[] beltnum;
    
    

    public static void main(String[] args) {
            N = scan.nextInt();
            

            for(int t = 0;t<N;t++){
                command = scan.nextInt();
                switch(command){
                    case 100:
                        init();
                        break;
                    case 200:
                        down();
                        break;
                    case 300:
                        remove();
                        break;
                    case 400:
                        check();
                        break;
                    case 500:
                        broke();
                        break;

                }


            }

        // 여기에 코드를 작성해주세요.
    }
    public static void init(){
        NUM = scan.nextInt();
        M = scan.nextInt();
        isRemoved = new boolean[NUM];
        isBroken = new boolean[M];
        beltnum = new int[NUM];
        int num = NUM/M;
        belts = new LinkedList[M];


        for(int i=0;i<NUM;i++){
            idmap.put(scan.nextInt(),i);
        }
        for(int i=0;i<NUM;i++){
            int idx = i/num;
            if(belts[idx]==null){
                belts[idx] = new LinkedList<>();
            }
            beltnum[i]=idx;
            belts[idx].add(new present(i,scan.nextInt()));
        }
        
    }

    public static void down(){
        int res = 0;
        w_max = scan.nextInt();
        for(int i=0;i<M;i++){
            if(belts[i].isEmpty()||isBroken[i]){
                continue;
            }
            while(!belts[i].isEmpty()&&isRemoved[belts[i].peek().id]){
                belts[i].poll();
            }
            if(belts[i].isEmpty()){
                continue;
            }

            present p = belts[i].poll();

            if(p.w<=w_max){
                res+=p.w;
                isRemoved[p.id] = true;
            }else{
                belts[i].add(p);
            }
            
        }
        System.out.println(res);

    }
    public static void remove(){
        int r_id = scan.nextInt();
        if(!idmap.containsKey(r_id)||isRemoved[idmap.get(r_id)]){
            System.out.println(-1);
            return;
        }
        isRemoved[idmap.get(r_id)] = true;
        System.out.println(r_id);
            
    }
    public static void check(){
        int f_id = scan.nextInt();
        if(!idmap.containsKey(f_id)||isRemoved[idmap.get(f_id)]){
            System.out.println(-1);
            return;
        }
        int idx = beltnum[idmap.get(f_id)];


        while(belts[idx].peek().id!=idmap.get(f_id)){
            belts[idx].add(belts[idx].poll());
        }
        
        System.out.println(beltnum[idmap.get(f_id)]+1);

    }
    public static void broke(){
        int b_num = scan.nextInt()-1;
        if(b_num>=M||isBroken[b_num]){
            System.out.println(-1);
            return;
        }
        if(movebelt(b_num)){
            isBroken[b_num] = true;
            System.out.println(b_num+1);

        }else{
            System.out.println(-1);

        }

    }
    public static boolean movebelt(int b_num){
        int blt = (b_num+1)%M;
        while(isBroken[blt]){
            if(blt == b_num){
                return false;
            }
            blt = (blt+1)%M;
        }
        while(!belts[b_num].isEmpty()){
            present p = belts[b_num].poll();
            beltnum[p.id] = blt;
            belts[blt].add(p);
        }
        return true;


    }

}