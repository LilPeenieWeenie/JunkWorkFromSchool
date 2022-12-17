import java.io.*;
import java.util.*;
public class isogram {
    public static boolean  isIsogram(String str) {
      str = str.toLowerCase(); // make lowercase
      int length = str.length(); // find length so that have bounds
      
      char[] arr = str.toCharArray();
      
      Arrays.sort(arr);
      for(int i = 0; i < length - 1; i++){
        if (arr[i] == arr[i + 1])
          return false;
      }
 
      return true;
    } 
}