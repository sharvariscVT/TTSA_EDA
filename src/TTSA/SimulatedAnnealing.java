package TTSA;
import java.util.*;
import java.util.stream.IntStream;

public class SimulatedAnnealing {

   static class Schedule {

        static int noOfTeams;
        int maxC;
        int maxP;
        int maxR;
        static Double w ;
        int rounds;
        int[][] distMat;
        int[][] bestInfeasibleSchedule;
        int[][] bestFeasibleSchedule;
    public static int[][] getScheduleMap() {
        return scheduleMap;
    }

    static int[][] scheduleMap;

//Schedule Constructor for initializing the parameters
        Schedule(int n) throws Exception {
            this.noOfTeams = n;
            this.maxP = Constants.maxP;
            this.maxR = Constants.maxR;
            this.maxC = Constants.maxC;
            this.w = Constants.w;
            this.rounds = 2*noOfTeams-2;

            this.distMat = getDistMat(n);
           this.scheduleMap = buildRandomSchedule();
//             Backtrack back = new Backtrack(this.noOfTeams);
//             back.backtrack();
            //this.scheduleMap = back.getTable();
            printInitialParam();
        }

        public  void printInitialParam(){
            System.out.println("Initial input parameters: ");
            System.out.println("Number of Teams: "+noOfTeams);
            System.out.println("Number of Rounds: "+this.rounds);
            System.out.println("maxP: "+this.maxP);
            System.out.println("maxR: "+this.maxR);
            System.out.println("maxC: "+this.maxC);
            System.out.println("w :"+w);
            System.out.println("Temperature: "+Constants.T);
            System.out.println();

        }
        public static int[][] getDistMat(int n) throws Exception {

            if(n==4) return Constants.getDistance4();
            else if(n==6) return Constants.getDistance6();
            else if(n==10) return Constants.getDistances10();
            else if (n==12) return Constants.getDistances12();
            else if (n==14) return Constants.getDistances14();

            else if(n>16) throw new Exception("Error: invalid number of teams (should be less than 16)");

            return Constants.getDistances16();


        }
        public static void printTable(){

            System.out.print("Rounds   ");
            for(int i=0; i<scheduleMap[0].length; i++)
                System.out.print((i+1)+"    ");
            System.out.println();
            System.out.println("Teams   ");


            for(int i=0; i<scheduleMap.length; i++){
                System.out.print(i+1+"        ");
                for(int j=0; j<scheduleMap[0].length; j++)
                    System.out.print(scheduleMap[i][j]+"   ");
                System.out.println();
            }


        }
        public static  void printAnyTable(int[][] S){
            System.out.print("Rounds-> ");
            for(int i=0; i<S[0].length; i++)
                System.out.print((i+1)+"    ");
            System.out.println();
            System.out.println("Teams   ");


            for(int i=0; i<S.length; i++){
                System.out.print(i+1+"        ");
                for(int j=0; j<S[0].length; j++)
                    System.out.print(S[i][j]+"   ");
                System.out.println();
            }
        }

//Five moves to be performed on the schedule
        void swapHomes(int team1, int team2){
            int idx1 = team1-1;
            int idx2 = team2-1;

            for(int i=0; i<scheduleMap[0].length;i++){
               if(Math.abs(scheduleMap[idx1][i])==team2){
                   scheduleMap[idx1][i] = -1*scheduleMap[idx1][i];
               }
           }

            for(int i=0; i<scheduleMap[0].length;i++){
                if(Math.abs(scheduleMap[idx2][i])==team1){
                    scheduleMap[idx2][i] = -1*scheduleMap[idx2][i];
                }
            }

        }

        void swapRounds(int round1, int round2){

            int r1= round1-1;
            int r2= round2-1;

            for(int i=0; i<scheduleMap.length; i++)
            {
                int temp = scheduleMap[i][r1];
                scheduleMap[i][r1] = scheduleMap[i][r2];
                scheduleMap[i][r2] = temp;
            }
        }

        void swapTeams(int team1 , int team2){
            int t1 = team1-1, t2 = team2-1;
            int[] idx = new int[2];
            int id=0;
            int id3 =0;
            for(int i=0;i<scheduleMap[0].length; i++){
                if(Math.abs(scheduleMap[t1][i])!=team2){
                    int temp = scheduleMap[t1][i];
                    scheduleMap[t1][i] = scheduleMap[t2][i];
                    scheduleMap[t2][i] = temp;
                }
                else {
                  //  System.out.println(team1+" "+team2+" "+id3+" "+scheduleMap[t1][i]);
                    idx[id3++]=i;
                }
            }

            id =0;
            int[] rows;
            int id2;
            while(id<scheduleMap[0].length){
               if(id==idx[0] || id==idx[1])
               {    id++;
                   continue;}
                id2=0;
                rows = new int[2];
                for(int j=0; j<scheduleMap.length; j++){
                    if(Math.abs(scheduleMap[j][id])==team1 || Math.abs(scheduleMap[j][id])==team2){
                        rows[id2]=j;
                        id2+=1;
                    }
                }
              //  System.out.println(rows[0]+" "+rows[1]);

                int sign1 = scheduleMap[rows[0]][id]>0 ? 1:-1;
                int sign2 = scheduleMap[rows[1]][id]>0 ? 1:-1;
                int temp;
                if(sign1!=sign2){
                    temp = -1*scheduleMap[rows[0]][id];
                    scheduleMap[rows[0]][id] = -1*scheduleMap[rows[1]][id];
                }
                else {
                    temp = scheduleMap[rows[0]][id];
                    scheduleMap[rows[0]][id] = scheduleMap[rows[1]][id];
                }
                scheduleMap[rows[1]][id] = temp;
                id++;
            }

        }

        void partialSwapRounds(int team, int round1, int round2){
            int r1 = round1-1;
            int r2 = round2-1;
            int t = team-1;

            for(int i=0;i<scheduleMap.length;i++){

                if(Math.abs(scheduleMap[i][r1]) != Math.abs(scheduleMap[i][r2]))
                {
                    int temp = scheduleMap[i][r1];
                    scheduleMap[i][r1] = scheduleMap[i][r2];
                    scheduleMap[i][r2] = temp;
                }

            }

        }

//        void partialSwapTeams(int team1 , int team2){
//
//        }



        int[][] buildRandomSchedule(){
            int[][] S = new int[noOfTeams][2*noOfTeams-2];

           for(int[] t: S)
             Arrays.fill(t, noOfTeams+1);

            return buildSchedule(S, 0, 0);
        }
//Backtracking solution
        int[][] buildSchedule(int[][] S, int team, int roundN){

            if(checkComplete(S))
                return S;

            int nextRound = roundN +1;
            int nextTeam = team;
            if(nextRound == rounds){
                nextRound =0;
                nextTeam = nextTeam +1;
            }

            if(S[team][roundN] != noOfTeams+1)
                return buildSchedule(S, nextTeam, nextRound);

            List<Integer> Q = makeChoice(S, team, roundN);
            Collections.shuffle(Q);
            //randomize Q
            if(Q==null)
                return null;

            for(int q: Q) {
                //cloning a matrix
                int[][] St = Arrays.stream(S).map(int[]::clone).toArray(int[][]::new);;
                St[team][roundN] = q;
                int sign = (q < 0) ? -1 : 1;
                St[Math.abs(q) - 1][roundN] = (team + 1) * sign * (-1);
                int[][] Snext = buildSchedule(St, nextTeam, nextRound);
                if (Snext != null)
                    return Snext;
            }
             return null;

        }

        //to be completed:
        boolean checkComplete(int[][] S){
            for(int i=0; i<noOfTeams; i++){
                for(int j=0; j<rounds; j++){
                    if(S[i][j] == noOfTeams+1)
                        return false;
                }
            }
            return true;
        }

        List<Integer> makeChoice(int[][] S, int team, int roundN){
            List<Integer> Q = new ArrayList<>();

            for(int i=1; i<=noOfTeams; i++){
                Q.add(i);
                Q.add(-i);
            }
        //Discard all unnecessary entries
            int[] temp = IntStream.of(S[team]).distinct().toArray();
            for(int i: temp){
                if(Q.contains(i))
                    Q.remove(Q.indexOf(i));
            }

            if(Q.contains(team+1))
                Q.remove(Q.indexOf(team+1));
            if(Q.contains(-(team+1)))
                Q.remove(Q.indexOf(-(team+1)));

            if(roundN>0){
                if(Q.contains(S[team][roundN-1]))
                    Q.remove(Q.indexOf(S[team][roundN-1]));
                if(Q.contains(-1*S[team][roundN-1]))
                    Q.remove(Q.indexOf(-1*S[team][roundN-1]));
            }


             Set<Integer> set = new HashSet();
           for(int i=0; i<S.length; i++){
                set.add(S[i][roundN]);
           }
           for(int k: set){
               if(Q.contains(k))
                   Q.remove(Q.indexOf(k));
               if(Q.contains(-k))
                   Q.remove(Q.indexOf(-k));
           }

         return Q;
        }

    public static int getNoOfTeams() {
        return noOfTeams;
    }

    //calculating cost of schedule
    public Double cost(int[][] S){

            int[] distance = new int[getNoOfTeams()];
            Arrays.fill(distance, 0);

            for(int i=0;i<S.length;i++){
                int dist =0;
                int prev =0;
                if(S[i][0]<0){
                    dist += this.distMat[i][Math.abs(S[i][0])-1];
                           // Math.abs((i+1)-Math.abs(S[i][0]));
                }
                prev = S[i][0];
                for(int j=1;j <S[0].length; j++){
                    if(prev < 0){
                        if(S[i][j]<0)
                              dist += this.distMat[Math.abs(prev)-1][Math.abs(S[i][j])-1] ;
                          else
                              dist += this.distMat[i][Math.abs(S[i][j])-1];
                                   //Math.abs((i+1)-Math.abs(S[i][0]));
                    }
                    else{
                        if(S[i][j]<0)
                            dist += this.distMat[i][Math.abs(S[i][j])-1];
                                    //Math.abs((i+1)-Math.abs(S[i][0]));
                    }
                    prev = S[i][j];
                }

                distance[i]=dist;
            }

            Double totalCost = Double.valueOf(0);
            for(int val : distance)
                totalCost+=val;

            int violations = getViolations2(S);
            if(violations>0)
                return complexCost(totalCost, violations);

                return totalCost;

        }

 static Double complexCost(Double cost, int v){
            return Math.sqrt((cost*cost)+Math.pow(w * func(v),2));
 }

 static double func(int v){
            if(v==1) return v;

            return 1+(Math.sqrt(v) * Math.log(v))/2;
 }
static int getViolations2(int[][] S){
          int violations =0;


          for(int i=0; i<S.length; i++){
              int prev = S[i][0] > 0? 1:-1;
              int prevVal = S[i][0];
              for(int j=1; j<S[0].length; j++){

                  if(S[i][j]>0){
                      if(prev>0) prev += 1;
                      else prev =1;
                  }
                  else {
                      if(prev<0) prev -= 1;
                      else prev =-1;
                  }

                  if(Math.abs(prev) >3)
                      violations+=1;

                  if(Math.abs(prevVal) == Math.abs(S[i][j])){
                      violations+=1;
                  }
                  prevVal = S[i][j];
              }
          }

          return violations;

}



//The actual TTSA algorithm: Simulated Annealing
  void simulatedAnnealing(){

            System.out.println("Distance Matrix: ");
            for(int i=0; i<this.distMat.length; i++)
                System.out.println(Arrays.toString(this.distMat[i]));
            System.out.println();

            Double bestFeasible = Double.POSITIVE_INFINITY;
            Double nbf = Double.POSITIVE_INFINITY;
            Double bestInfeasible = Double.POSITIVE_INFINITY;
            Double nbi = Double.POSITIVE_INFINITY;
            int reheat =0;
            int counter =0;

            int maxR = this.maxR;
            int maxP = this.maxP;
            int maxC = this.maxC;

            int T = Constants.T;
            int bestTemperature = Constants.T;
            double theta = Constants.theta;
            double beta = Constants.beta;
            double sigma = Constants.sigma;

            long start_time = System.currentTimeMillis();
            boolean accept = false;
            while(reheat<=maxR){
                int phase =0;
                while(phase <= maxP){
                    counter =0;
                    while (counter <=maxC){

                       int[][] S ;

                       S = Arrays.stream(scheduleMap).map(int[]::clone).toArray(int[][]::new);
//

                       int [][] St;
                       St = randomMove();

                       Double costS = cost(S);
                       Double costSt = cost(St);
                       int violationsForS = getViolations2(S);
                       int violationsForSt = getViolations2(St);

                       if((costSt < costS) || (violationsForSt ==0 && costSt<bestFeasible) || (violationsForS>0 && costS<bestInfeasible)){
                           accept = true;
                       }
                       else{

                           if(Math.exp(-1*Math.abs(costS-costSt)/T) > 0.5)
                               accept = true;
                           else
                           {
                               accept = false;
                               scheduleMap = Arrays.stream(S).map(int[]::clone).toArray(int[][]::new);;
                           }

                       }
//Store the best feasible and best infeasible schedule
                       if(costSt<bestFeasible && violationsForSt ==0)
                           this.bestFeasibleSchedule = Arrays.stream(St).map(int[]::clone).toArray(int[][]::new);
                       if(costSt<bestInfeasible && violationsForSt >0)
                           this.bestInfeasibleSchedule = Arrays.stream(St).map(int[]::clone).toArray(int[][]::new);

                       if(accept){
                           scheduleMap = Arrays.stream(St).map(int[]::clone).toArray(int[][]::new);

                           if(violationsForS ==0)
                               nbf = Math.min(costS, bestFeasible);
                           else
                               nbi = Math.min(costS, bestInfeasible);
                        //  System.out.println("nbf: "+nbf+" nbi"+nbi);
                           if(nbf<bestFeasible || nbi<bestInfeasible){
                               reheat = 0;
                               counter =0;
                               phase =0;
                               bestTemperature = T;
                               bestFeasible = nbf;
                               bestInfeasible = nbi;
                               if(violationsForSt == 0){
                                   w = w/theta;
                               }else w= w*sigma;
                           }
                           else
                               counter++;
                       }

                    }
                    phase++;
                    T=(int)(T*beta);
                }

                reheat++;
                T = 2*bestTemperature;
            }


            long requiredTime = System.currentTimeMillis()-start_time;

       printTable();
      System.out.println("Time Required: "+requiredTime+" ms");
      System.out.println("bestFeasible: " +bestFeasible);
      System.out.println("bestInfeasible: "+bestInfeasible);
      System.out.println("bestTemperature: "+ bestTemperature);

      System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
      System.out.println("Best infeasible schedule");
      printAnyTable(bestInfeasibleSchedule);
      System.out.println("The Cost for infeasible is : "+ cost(bestInfeasibleSchedule));

      System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
      System.out.println("Best feasible schedule");



      printAnyTable(bestFeasibleSchedule);
      System.out.println("The Cost for feasible is : "+ cost(bestFeasibleSchedule));


  }
//Function to choose one value randomly from the given choices
  int[][] randomMove(){

            int option = (int)(Math.random()*4);
            int[][] S ;

            switch(option){

                case 0:
                    int team1 = (int)(Math.random()*(noOfTeams)) +1;
                    int team2 = (int)(Math.random()*(noOfTeams)) +1;

                    if(team1==team2){
                       while(team1==team2) team2 =(int)(Math.random()*(noOfTeams)) +1;
                    }
               //     System.out.println("Swap Team "+team1+" "+team2 );
                    swapTeams(team1, team2);
                    break;
                case 1:
                    int r1 = (int)(Math.random()*(this.rounds))+1;
                    int r2 = (int)(Math.random()*(this.rounds))+1;

                    if(r1==r2) {
                        while(r1==r2) r2 = (int)(Math.random()*(this.rounds))+1;
                    }
               //     System.out.println("Swap Rounds "+r1+" "+r2);
                    swapRounds(r1, r2);
                   break;
                case 2:
                    int teamA = (int)(Math.random()*(noOfTeams))+1;
                    int teamB = (int)(Math.random()*(noOfTeams))+1;

                    if(teamA==teamB){
                        while(teamA == teamB) teamA =(int)(Math.random()*(noOfTeams))+1;
                    }
                 //   System.out.println("Swap Homes "+teamA+" "+teamB);
                    swapHomes(teamA, teamB);
                 break;
                case 3:
                    int rA = (int)(Math.random()*(this.rounds))+1;
                    int rB = (int)(Math.random()*(this.rounds))+1;
                    int team = (int)(Math.random()*(noOfTeams))+1;

                    if(rA == rB) {
                        while(rA==rB) rB = (int)(Math.random()*(this.rounds))+1;
                    }
                 //   System.out.println("Partial Swap Team "+rA+" "+rB+" "+team);
                    partialSwapRounds(team, rA, rB);

            }
        S = Arrays.stream(scheduleMap).map(int[]::clone).toArray(int[][]::new);

      return S;

  }

    }




}


