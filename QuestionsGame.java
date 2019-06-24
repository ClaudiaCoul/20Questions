// Claudia Coulibaly
//CSE 143 AC with Dylan Jergens
//QuestionsGame allows a user to play a game of "20 Questions"
//but with an unlimited number of guesses. Can start from scratch or
//from a file already containing questions and possible answers!

import java.io.*;
import java.util.*;
public class QuestionsGame {
   private Scanner console;
   public QuestionNode start;//the first question or answer in the game
   
   //Starts a new QuestionsGame with a single answer of "computer"
   public QuestionsGame(){
      console = new Scanner(System.in);
      start = new QuestionNode("computer");
   }
   
   //replaces the current QuestionsGame tree with another one using data
   //from a file connected to the given Scanner 
   //File must be in standard form:
   //   Every Question must have a line printed above containing "Q:"
   //   Every Answer must have a line above containing "A:"
   public void read(Scanner input){
      start = read(input, start);
   }
   
   
   //replaces the QuestionsGame tree with a new one
   //using the given data from a file connected to the given Scanner
   private QuestionNode read(Scanner input, QuestionNode node){
         String information = input.nextLine();
         QuestionNode nextNode = new QuestionNode(input.nextLine());
         if(information.contains("Q:")){
            nextNode.yes = read(input, nextNode.yes);
            nextNode.no = read(input, nextNode.no);
         }    
      return nextNode;
   }
   
   //Uses the given PrintStream to create a file using the data 
   //from the previously run QuestionNode game so it can be played again!
   //File will be in standard form: 
   //   Every Question will have a line printed above containing "Q:"
   //   Every Answer will have a line above containing "A:"
   public void write(PrintStream output){
      write(output, start);
   }
   
   //writes a new file containing data from the previous game
   private void write(PrintStream output, QuestionNode position){
      if(position.data != null){
         if(position.yes != null && position.no != null){
            output.println("Q:");
            output.println(position.data);
            write(output, position.yes);
            write(output, position.no);
         }else{
            output.println("A:");
            output.println(position.data);
         } 
      } 
   }
   
   //asks the user yes or no questions until the correct answer has been reached
   //if the QuestionNode tree does not contain the object you're thinking of,
   //prompts user to give information about that object so it can be guessed about
   //in the future!
   public void askQuestions(){
      start = askQuestions(start);   
   }
   
   //Asks questions about an object and returns a QuestionNode
   //containing corresponding information
   private QuestionNode askQuestions(QuestionNode node){
      String s = node.data; 
      //only tests if yes is null b/c standard form ensures all nodes have both
      // yes/no branches or none at all! 
      if(node.yes == null){
         s = ("Would your object happen to be " + node.data + "?");
      }        
      boolean didIt = yesTo(s);      
      if(didIt && node.yes != null){
         node.yes = askQuestions(node.yes);
      }else if(didIt && node.yes == null){
         System.out.println("Great, I got it right!");
      }else if(!didIt && node.yes != null){
         node.no = askQuestions(node.no);
      }else{
         return addNewObject(node);//adds the new node!
      }  
      return node;//keeps the node the same! 
   }
   
   //asks questions about your unguessed object and returns a QuestionNode
   //containing that information
   private QuestionNode addNewObject(QuestionNode node){
      System.out.print("What is the name of your object? ");
         String data = console.nextLine();
         System.out.println("Please give me a yes/no question that");
         System.out.println("distinguishes between your object");
         System.out.print("and mine--> ");
         String question = console.nextLine();
         QuestionNode newAnswer = new QuestionNode(data);
         
         if(yesTo("And what is the answer for your object?")){
            QuestionNode newQuestion = new QuestionNode(question, newAnswer, node);
            return newQuestion;
         }else{
            QuestionNode newQuestion = new QuestionNode(question, node, newAnswer); 
            return newQuestion;       
         }
   } 

   // post: asks the user a question, forcing an answer of "y" or "n";
   //       returns true if the answer was yes, returns false otherwise
   public boolean yesTo(String prompt) {
      System.out.print(prompt + " (y/n)? ");
      String response = console.nextLine().trim().toLowerCase();
      while (!response.equals("y") && !response.equals("n")) {
         System.out.println("Please answer y or n.");
         System.out.print(prompt + " (y/n)? ");
         response = console.nextLine().trim().toLowerCase();
      }
      return response.equals("y");
   }

   //Class represents a Question or Answer
   private static class QuestionNode {
   
      public String data;
      public QuestionNode yes; //reference to next question if answer was YES
      public QuestionNode no; //reference to next question if answer was NO
        
      //Constructs a QuestionNode with the given data.
      public QuestionNode(String data) {
          this(data, null, null);
      }
      //Constructs a QuestionNode with the given data & the next questions/answers
      public QuestionNode(String data, QuestionNode yes, QuestionNode no) {
         this.data = data;
         this.yes = yes;
         this.no = no;
      }
   }
}
