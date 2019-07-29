import java.util.ArrayList;

public class set {
	int E;
	int B;
 int setNumber;
ArrayList<line> lineList;
set(int E,int B,int setNumber){

	this.E=E;


	this.B=B;
	this.setNumber=setNumber;
	this.lineList = new ArrayList<line>(E);
	createLines(E);
}

public int getE() {
	return this.E;
}
public void setE(int e) {
	this.E = e;
}
public int getB() {
	return B;
}
public void setB(int b) {
	this.B = b;
}
public int getSetNumber() {
	return this.setNumber;
}
public void setSetNumber(int setNumber) {
	this.setNumber = setNumber;
}

public ArrayList<line> getLineList() {
	return lineList;
}
public void setLineList(ArrayList<line> lineList) {
	this.lineList = lineList;
}
void createLines(int E) {
	for(int i=0;i<E;i++) {
		lineList.add(new line(i,B));
	}
}

}
