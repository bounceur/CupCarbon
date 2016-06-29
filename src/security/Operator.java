package security;

public class Operator {
	long x,y;
	public Operator(long x, long y){
		this.x = x;
		this.y = y;
	}
public String mod(){
	return Long.toString(x%y);
}
}
