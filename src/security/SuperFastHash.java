package security;

import static security.Hashpos11to17.hashpos11to17;
import static security.Hashpos11to18.hashpos11to18;
import static security.Hashpos1to10.hashpos1to10;

/**
 * @author Abdelkader Laouid
 * @author Massinissa Saoudi
 */

public class SuperFastHash 
{	
	 static byte [] data;
	 static String strData;
	 static long start;
	 static int offset;
	 static int len;
    public  SuperFastHash(String nonceij, long nonceji, int idmax, int idMin)
    {
    	strData = nonceij;
    	start = nonceji;
    	offset = idmax % 10;
    	len = idMin % 10;
    }
    public long calculate(){
        data = strData.getBytes();
    	long hash = start & 0xFFFFFFFF;
        long tmp;
        int rem;
        
        if (len < 1) return hash;
        
        rem = len & 3;
        len >>>= 2;
        
        int pos = offset;
        for (; len > 0; len--)
        {
            hash += (data[pos++] | (data[pos++] << 8));
            tmp = ((data[pos++] | (data[pos++] << 8)) << 11) ^ hash;
            hash = ((hash << 16) ^ tmp);
            hash += (hash >>> 11);
        }
        
        
     // Refactoring - Replace conditional with polymorphism
     // Converted the logic of switch case to extract method 
     // Moved below methods which has logic to new abstract class HashPos 
      

      		// The methods are not removed and just commented to show the changes in files
      		//Note -- you can remove below method as they are already implemented in HashPos class 
        	//You can remove the program and still the program will run	
      		
      	
        
//       	private long hashpos1to10(long hash, int pos) {
//       		hash += data[pos++];
//       		hash ^= (hash << 10);
//       		hash += (hash >>> 1);
//       		return hash;
//       	}
//       	private long hashpos11to17(long hash, int pos) {
//       		hash += (data[pos++] | (data[pos++] << 8));
//       		hash ^= (hash << 11);
//       		hash += (hash >>> 17);
//       		return hash;
//       	}
//       	private long hashpos11to16(long hash, int pos) {
//       		hash += (data[pos++] | (data[pos++] << 8));
//       		hash ^= (hash << 16);
//       		hash ^= (data[pos++] << 18);
//       		hash += (hash >>> 11);
//       		return hash;
//       	}
        
        switch (rem)
        {
            case 3:
            	//replace the conditional with the relevant method call. 
			hash = hashpos11to18(hash, pos);
                break;

            case 2:
            	//replace the conditional with the relevant method call. 
			hash = hashpos11to17(hash, pos);
                break;

            case 1:
            	//replace the conditional with the relevant method call. 
			hash = hashpos1to10(hash, pos);
                break;
        }
        
        

        hash ^= (hash << 3);
        hash += (hash >>> 5);
        hash ^= (hash << 4);
        hash += (hash >>> 17);
        hash ^= (hash << 25);
        hash += (hash >>> 6);
        String hashS = String.valueOf(hash);
        String newHashS = hashS.substring(0,16);
        long newHash = Long.parseLong(newHashS);
        if(newHash < 0) newHash = -newHash;
        return newHash & 0xFFFFFFFF;
    }
   
 
	
	
}