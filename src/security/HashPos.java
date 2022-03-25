package security;

public abstract class HashPos  {

	 static byte [] data;
}

// Refactoring - Replace conditional with polymorphism
//Create subclasses matching the branches of the conditional. 

 class Hashpos1to10 extends HashPos
 {
	 static long hashpos1to10(long hash, int pos) {
			hash += data[pos++];
			hash ^= (hash << 10);
			hash += (hash >>> 1);
			return hash;
		}
		
	 
 }

 class Hashpos11to18 extends HashPos
 {
	
	 static long hashpos11to18(long hash, int pos) {
			hash += (data[pos++] | (data[pos++] << 8));
			hash ^= (hash << 16);
			hash ^= (data[pos++] << 18);
			hash += (hash >>> 11);
			return hash;
		}
 }

 class Hashpos11to17 extends HashPos
 {
	 
	 static long hashpos11to17(long hash, int pos) {
			hash += (data[pos++] | (data[pos++] << 8));
			hash ^= (hash << 11);
			hash += (hash >>> 17);
			return hash;
		}
 }

 
	
	