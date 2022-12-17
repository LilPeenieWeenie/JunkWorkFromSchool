import java.util.*;
public class Deck
{
   // instance variables (Has a)
   private ArrayList<Card> deck = new ArrayList<>();
   private int currentCard = 0;
   
   // constructor
   public Deck()
   {
      Rank[] ranks = Rank.values();
      String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
      
      for(String suit: suits)
         for(Rank rank: ranks)
            deck.add(new Card(rank, suit));
            
      shuffle();
   } // end constructor
   
   public void shuffle()
   {
      Collections.shuffle(deck);
      currentCard = 0;
   }
   
   public Card dealCard()
   {
      if(currentCard > 52) 
         shuffle();
      return deck.get(currentCard++);
      
   }
   
   public Card[] dealHand(int numberOfCards)
   {
      Card[] hand = new Card[numberOfCards];
      for(int i = 0; i < numberOfCards; i++)
      {
         hand[i] = dealCard();
      }
      
      return hand;
   }
   
   // override toString
   public String toString()
   {
      return deck.toString();
   }
} // end class