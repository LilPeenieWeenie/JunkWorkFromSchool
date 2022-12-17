public class Challenge {
	public static int solutions(int a, int b, int c) {
      double a2 = a;
      double b2 = b;
      double c2 = c;
      if(Math.sqrt(b2) - (4 * a2 * c2) > 0)
         return 2;
      else if(Math.sqrt(b2) - (4 * a2 * c2) == 0)
         return 1; 
      else
         return 0;
      
  }
}