import java.util.*;
class Node{
int row;
int col;
Node(int r,int c){
    row = r;
    col = c;
}

}
public class Main {
    static int[][] dir = {{0,1},{1,0},{0,-1},{-1,0}};
    static boolean reversed = false;
    static int[] nexttop = {-1,4,-1,1,6,-1,3};
    static Scanner scan = new Scanner(System.in);
    static int r,c,direction,top;
    static int res;
    static int[][] map;
    static int N;
    static Queue<Node> que = new LinkedList<>();
    public static void move(){

        

        
        r = r+dir[direction][0];
        c = c+dir[direction][1];
        
        res+= map[r][c]*getnum(r,c);
        // System.out.println("move"+r+" "+c+" "+direction+" "+reversed+" "+(7-top)+" "+res);
        
        top = nexttop[top];
        if(reversed){
            top = 7-top;
        }
        int bottom = 7-top;

        if(map[r][c]>bottom){
            if(direction ==0) direction = 3;
            else{
                direction--;
            }
        }else if(map[r][c]<bottom){
            direction+=1;
            direction %=4;
        }
        switch(direction){
            case 0:
                if(r==N-1){
                    reversed = !reversed;
                    direction = (direction+2)%4;
                } 
                break;
            case 1:
                if(c==N-1){
                    reversed = !reversed;
                    direction = (direction+2)%4;
                } 
                break;
            case 2:
                if(r==0){
                    reversed = !reversed;
                    direction = (direction+2)%4;
                } 
                break;
            case 3:
                if(c==0){
                    reversed = !reversed;
                    direction = (direction+2)%4;
                } 
                break;

        }
            
        

    }
    public static int getnum(int r,int c){
        boolean[][] visited = new boolean[N][N];
        que.clear();
        que.add(new Node(r,c));
        int num = 0;
        while(!que.isEmpty()){
            Node nde = que.poll();
        
            int row = nde.row;
            int col = nde.col;
            if(visited[row][col]) continue;
            visited[row][col] = true;
            if(map[row][col]==map[r][c]){
                num++;
            }
            for(int[] d:dir){
                int rr = row+d[0];
                int cc = col+d[1];
                if(rr>=N||rr<0||cc>=N||cc<0) continue;
                if(!visited[rr][cc]){
                    que.add(new Node(rr,cc));
                }

            }


        }
        // System.out.println("num : "+num);
        return num;

    }
    public static void main(String[] args) {
        int n = scan.nextInt();
        int m = scan.nextInt();
        N = n;
        r=0;
        c=0;
        direction = 0;
        top=1;
        res = 0;
        map = new int[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                map[i][j] = scan.nextInt();
            }
        }
        for(int h = 0;h<m;h++){
            move();

        }
        System.out.println(res);


        // 여기에 코드를 작성해주세요.
    }
}