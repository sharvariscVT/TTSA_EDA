package TTSA;
import java.util.*;

public class Backtrack {

    public static int n ;
    public static List<Integer> list;

    public int[][] getTable() {
        return table;
    }

    public  int[][] table ;
    Backtrack(int n){
        this.n = n ;
        this.list = new ArrayList<>();
        for(int i=1; i<=n; i++)
        {
            list.add(i);
            list.add(-i);
        }
        table = new int[n][2*n-2];

    }


  public  boolean  isSafe(int[][] table , int row, int col, int num){

       if(Math.abs(num) == row+1)
           return false;

       for(int i=0; i<table.length; i++){
           if(Math.abs(table[i][col]) == Math.abs(num))
               return false;
       }


//       for(int i=0; i<table[0].length; i++){
//           if(Math.abs(table[row][i]) == row+1)
//               return false;
//       }

       for(int i=0; i<table[0].length; i++){
           if(table[row][i] == num)
               return false;
       }
        return true;

    }

    public boolean backtrack(){

       boolean isFull = true;
       int row =-1;
       int col =-1;
   //    Check if there is any value left to be filled
        loop:  for(int i=0; i<table.length; i++){
              for(int j=0; j<table[0].length; j++){
                  if(table[i][j] ==0)
                  {
                      row = i;
                      col = j;
                      isFull = false;
                      break loop;
                  }
              }
          }

          if(isFull) return true;


          Collections.shuffle(list);
          for(int val: list){
              if(isSafe(table, row, col, val)){
                  table[row][col] = val;

                  if(backtrack())
                      return true;
                  else
                      table[row][col]= 0;
              }

          }

          return false;
    }


    public static void printTable(int[][] table){

       System.out.print("Rounds   ");
       for(int i=0; i<table[0].length; i++)
           System.out.print((i+1)+"    ");
       System.out.println();
       System.out.println("Teams   ");


       for(int i=0; i<table.length; i++){
           System.out.print(i+1+"        ");
           for(int j=0; j<table[0].length; j++)
               System.out.print(table[i][j]+"   ");
           System.out.println();
       }


    }


    public static void main(String[] args){
       int n=4;

      Backtrack back = new Backtrack(n);
       // back.table = new int[n][2*n-2];
       if(back.backtrack())
           printTable(back.table);

    }





}
