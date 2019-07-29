import java.util.ArrayList;
import java.util.Date;

public class line {
	int lineNumber;
	int B;
	boolean full;
	ArrayList<block> blockList;
boolean valid=false;
Date date;
String data;
int tag;
public int getLineNumber() {
	return this.lineNumber;
}
public void setLineNumber(int lineNumber) {
	this.lineNumber = lineNumber;
}
public int getB() {
	return B;
}
public void setB(int b) {
	B = b;
}
public boolean isFull() {
	return this.full;
}
public void setFull(boolean full) {
	this.full = full;
}
public int getTag() {
	return this.tag;
}
public void setTag(int tag) {
	this.tag = tag;
}
public ArrayList<block> getBlockList() {
	return this.blockList;
}
public void setBlockList(ArrayList<block> blockList) {
	this.blockList = blockList;
}



line(int lineNumber,int B){
	this.lineNumber=lineNumber;

	this.B=B;
	this.blockList = new ArrayList<block>(B);
	createBlocks(this.B);
}
void createBlocks(int B) {
	for(int i=0;i<B;i++) {
		this.blockList.add(new block(i));
	}
}

public boolean isValid() {
	return this.valid;
}

public void setValid(boolean valid) {
	this.valid = valid;
}

public java.util.Date getDate() {
	return this.date;
}

public void setDate(java.util.Date date) {
	this.date = date;
}

public String getData() {
	return this.data;
}

public void setData(String data) {
	this.data = data;
}


}
