public class Card
{
   // Instance variables
   private Rank rank;
   private String suit; 
   
   // Constructor variable
   public Card(Rank rank, String suit)
   {
      this.rank = rank;
      this.suit = suit;
   }
   
   public Rank getRank() // all methods have parenthasies
   {
      return rank;
   }
   
   // override toString
   public String toString()
   {
      return String.format("%s of %s", rank, suit);
   }
} // end of class