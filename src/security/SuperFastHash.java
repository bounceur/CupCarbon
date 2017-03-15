package security;

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
        
        switch (rem)
        {
            case 3:
                hash += (data[pos++] | (data[pos++] << 8));
                hash ^= (hash << 16);
                hash ^= (data[pos++] << 18);
                hash += (hash >>> 11);
                break;

            case 2:
                hash += (data[pos++] | (data[pos++] << 8));
                hash ^= (hash << 11);
                hash += (hash >>> 17);
                break;

            case 1:
                hash += data[pos++];
                hash ^= (hash << 10);
                hash += (hash >>> 1);
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