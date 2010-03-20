                                                                     
                                                                     
                                                                     
                                             
import java.util.Scanner;

/**
 * This program promts the user for student names and scores, and outputs the highest score.
 * 
 * Author Brittany Cannady
 * Homework #3
 */
public class Test1
{
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    // Asks user for total number of students
    System.out.print("Please, enter the number of students: ");
    int totalstudents = input.nextInt();
    
    // Asks for student names and scores     
    System.out.print("Enter a student name: ");
    String stu1 = input.next();
    System.out.print("Enter a student score: ");
    double score1 = input.nextDouble();

    for (int i = 0; i < totalstudents - 1; i++) {
      System.out.print("Enter a student name: ");
      String stu = input.next();

      System.out.print("Enter a student score: ");
      double score = input.nextDouble();
      
      //finds higher score 
      if (score > score1) {
        stu1 = stu;
        score1 = score;
      }
    }

    System.out.println("The top student is" +
      stu1 + "with a score of " + score1);
  }
}