import java.util.ArrayList;

public class cache {
int hit=0;
int miss=0;
String name;
int E;
int S;
int B;
int Eviction=0;
ArrayList<set> setList; 
cache(String name,int E,int S,int B){
this.name=name;
	this.E=E;
	this.S=S;
	this.B=B;
	this.setList = new ArrayList<set>(S);
	createSets(E,S,B);
	
}
 void createSets(int E,int S,int B) {
	 for(int i=0;i<S;i++) {
		 this.setList.add( new set(E,B,i));
	 }
	

}
public void evicIncrmt() {
	this.Eviction++;
}
public int getHit() {
	return this.hit;
}
public void setHit(int hit) {
	this.hit = hit;
}
public int getMiss() {
	return this.miss;
}
public void setMiss(int miss) {
	this.miss = miss;
}
public String getName() {
	return this.name;
}
public void setName(String name) {
	this.name = name;
}
public int getE() {
	return this.E;
}
public void setE(int e) {
	this.E = e;
}
public int getS() {
	return this.S;
}
public void setS(int s) {
	this.S = s;
}
public int getB() {
	return B;
}
public void setB(int b) {
	this.B = b;
}
public ArrayList<set> getSetList() {
	return this.setList;
}

public int getEviction() {
	return this.Eviction;
}

public int checkEviction(int tag,int sInt) {
	for(int i=0;i<this.E;i++) {
		if(this.setList.get(sInt).getLineList().get(i).isValid()==false ||this.setList.get(sInt).getLineList().get(i).getTag()==tag) {
			return i;
		}
	}
	
	return -1;
}
public int evictionLineSelect(int sInt) {
	if(this.E==1) {
		return 1;
	}
	int min = 0;
	for(int i=0;i<this.E;i++) {
		if(this.setList.get(sInt).getLineList().get(i).getDate().compareTo(this.setList.get(sInt).getLineList().get(i).getDate()) ==-1) {
			min=i;
		}
	}
	
	return min;
}
}