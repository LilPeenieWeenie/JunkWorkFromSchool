import java.time.*;

public class Person
   {
     // instance variables
     private String firstName;     // the person knows its birthday so, from the birthday that is already
     private String lastName;      // stored code in the get age function to use the localdate function
     private LocalDate birthdate;
     
     // constructor
     public Person(String firstName, String lastName, LocalDate birthdate) // add birthdate to coonstrucront 
     {
         this.firstName = firstName;
         this.lastName = lastName;
         this.birthdate = birthdate;
     } // end construtor
     
     public int getAge()
     {
         
         return Period.between(birthdate, LocalDate.now()).getYears();
     }
     
     // override toString
     public String toString()
     {
         return String.format("%s %s(%d)", firstName, lastName, getAge());
                                     
     }
     
     
   } // end clas 