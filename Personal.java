import java.time.*;

public class Personal
   {
     // instance variables
     private String firstName;     // the person knows its birthday so, from the birthday that is already
     private String lastName;      // stored code in the get age function to use the localdate function
     private LocalDate birthdate;
     
     // constructor
     public Personal(String firstName, String lastName, LocalDate birthdate) // add birthdate to coonstrucront 
     {
         this.firstName = firstName;
         this.lastName = lastName;
         this.birthdate = birthdate;
     } // end construtor
     
     public int getAge()
     {
         
         return 0;
     }
     
     // override toString
     public String toString()
     {
         return String.format("%s %s(%d)", firstName, lastName, getAge());
                                     
     }
     
     
   } // end clas 