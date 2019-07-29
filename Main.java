import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Main {
	static int L1s;
	static int L1E;
	static int L1b;
	static int L2s;
	static int L2E;
	static int L2b;
	static int L1S;
	static int L1B;
	static int L2S;
	static int L2B;
	static cache L1I;
	static cache L1D;
	static cache L2;
	static char operation;
	public static void main(String[] args) throws Exception {
		
L1s= Integer.parseInt(args[1]);
L1E= Integer.parseInt(args[3]);
L1b= Integer.parseInt(args[5]);
L2s= Integer.parseInt(args[7]);
L2E= Integer.parseInt(args[9]);
L2b= Integer.parseInt(args[11]);
String trace = args[13];
L1S= (int)Math.pow(2, L1s);
L1B= (int)Math.pow(2, L1b);
L2S= (int)Math.pow(2, L2s);
L2B= (int)Math.pow(2, L2b);
L1I = new cache("L1I",L1E,L1S,L1B);
L1D = new cache("L1D",L1E,L1S,L1B);
L2 = new cache("L2",L2E,L2S,L2B);

readTrace(trace);
printSummary(L1I,L1D,L2);

	}

	public static void readTrace(String adress) throws NumberFormatException, Exception {
	File trace = new File("./"+adress);
		Scanner input = new Scanner(trace);
		while(input.hasNextLine()){
		String line;
		line = input.nextLine();
	String[]	instruction=line.split("[\\s,]+");
	operation=instruction[0].charAt(0);
	int tag = 0;
	int sBits =0;
	int block = 0;
	int intAdress=0;
	String adress1 = instruction[1];
	 String byteAdress=new BigInteger(adress1, 16).toString(2);
	 while(byteAdress.length()!=32) {
		 byteAdress = "0"+byteAdress;
	 }
	 tag =Integer.parseInt(byteAdress.substring(0,byteAdress.length()-(L1s+L1b)),2);
	 try{
		 sBits =Integer.parseInt(byteAdress.substring(byteAdress.length()-(L1s+L1b),byteAdress.length()-1-L1b),2);
		 
	 }
	 catch(Exception ex) {
		 sBits=0;
	 }
	 intAdress =Integer.parseInt(byteAdress,2);
	 block = Integer.parseInt(byteAdress.substring(byteAdress.length()-1-L1b,byteAdress.length()-1),2);
	 if(Integer.parseInt(instruction[2])!=0 ) {
	if(operation=='I') {
		
		System.out.printf("I %s, %d\n", instruction[1],Integer.parseInt(instruction[2]));
	
		load(L1I,L2,byteAdress,intAdress,tag,sBits,block,L1B);
				
		System.out.println("Place in L2 set " +sBits + ", L1I");
		System.out.println();
	}
	else if(operation=='M') {
		System.out.printf("M %s, %d, %s\n", instruction[1],Integer.parseInt(instruction[2]),instruction[3]);
		List<String> strings = new ArrayList<String>(Integer.parseInt(instruction[2]));
		int index = 0;
		
		while (index < 	instruction[3].length()) {
		    strings.add(	instruction[3].substring(index, Math.min(index + 2,	instruction[3].length())));
		    index += 2;
		}
		byte[] bytes = new byte[Integer.parseInt(instruction[2])];
		
		for(int i=0;i<bytes.length;i++) {
			bytes[i]=hexStringToByte(strings.get(i));
		}
		load(L1D,L2,byteAdress,intAdress,tag,sBits,block,L1B);
		store(L1D,L2,byteAdress,intAdress,tag,sBits,bytes,Integer.parseInt(instruction[2]));
		System.out.println("Store in L2 set" +sBits + ", L1D" + ", RAM");
		System.out.println();

	}
	else if(operation=='L') {
		System.out.printf("L %s, %d\n", instruction[1],Integer.parseInt(instruction[2]));
		load(L1D,L2,byteAdress,intAdress,tag,sBits,block,L1B);
		System.out.println("Place in L2 set " +sBits + ", L1D" );
		System.out.println();
}
	else if(operation=='S') {
	
		System.out.printf("S %s, %d, %s\n", instruction[1],Integer.parseInt(instruction[2]),instruction[3]);
		List<String> strings = new ArrayList<String>(Integer.parseInt(instruction[2]));
		int index = 0;
		
		while (index < 	instruction[3].length()) {
		    strings.add(	instruction[3].substring(index, Math.min(index + 2,	instruction[3].length())));
		    index += 2;
		    
		}
byte[] bytes = new byte[Integer.parseInt(instruction[2])];
		
		for(int i=0;i<bytes.length;i++) {
			bytes[i]=hexStringToByte(strings.get(i));
		}
		store(L1D,L2,byteAdress,intAdress,tag,sBits,bytes,Integer.parseInt(instruction[2]));
		System.out.println();
}
	
		}
	 else {
		 System.out.println("" + operation +" " + instruction[1] + ", " + instruction[2]);
		 System.out.println("Requested operation can not be processed because the operation's data size is 0");
		 System.out.println();
		 
	 }
		}
	}
	public static byte hexStringToByte(String s) {
	    byte b;
	    
	      int index = 0;
	      int v = Integer.parseInt(s.substring(index, index + 2), 16);
	      b = (byte) v;
	    
	    return b;
	  }
	public static void load(cache L1,cache L2,String byteAdress,int adress,int tag,int sBits,int block,int size) throws Exception {
		int lineNumberL1 = 0;
		int lineNumberL2 = 0;
	boolean L1hit=false;
	boolean L2hit=false;
		for(int i=0;i<L1.getE();i++) {
			if(L1.getSetList().get(sBits).
					getLineList().get(i).getTag()==tag && L1.getSetList().get(sBits).
							getLineList().get(i).isValid()==true) {
				lineNumberL1=i;
			 L1.setHit(L1.getHit()+1);
			 L1hit=true;
			 break;
			}
		}
		for(int i=0;i<L2.getE();i++) {
			if(L2.getSetList().get(sBits).
					getLineList().get(i).getTag()==tag && L2.getSetList().get(sBits).
					getLineList().get(i).isValid()==true) {
				lineNumberL2=i;
			 L2.setHit(L2.getHit()+1);
			 L2hit=true;
			 break;
			}
		}	
		if(L1hit==false && L2hit ==false) {
			System.out.println("" + L1.getName() + " miss" + " L2 miss");
			L1.setMiss(L1.getMiss()+1);
			L2.setMiss(L2.getMiss()+1);
			lineNumberL2=fromRamToL2(L2,adress,tag,sBits);
			betweenCaches(L2,L1,tag,sBits,lineNumberL2);
		}
		else if(L1hit==false && L2hit ==true) {
			System.out.println("" + L1.getName() + " miss" + " L2 hit");
			L1.setMiss(L1.getMiss()+1);
			betweenCaches(L2,L1,tag,sBits,lineNumberL2);
		}
		else if(L1hit==true && L2hit==false) {
			System.out.println("" + L1.getName() + " hit" + " L2 miss");
			L2.setMiss(L2.getMiss()+1);
			betweenCaches(L1,L2,tag,sBits,lineNumberL1);
		}
		else if(L1hit==true&&L2hit ==true) {
			System.out.println("" + L1.getName() + " hit" + " L2 hit");
		}
		


			
	}
		
public static void store(cache L1,cache L2,String byteAdress ,int adress,int tag,int sBits,byte[] datas,int size) throws Exception {
	int lineNumberL1 = 0;
	int lineNumberL2 = 0;
boolean L1hit=false;
boolean L2hit=false;
	for(int i=0;i<L1.getE();i++) {
		if(L1.getSetList().get(sBits).
				getLineList().get(i).getTag()==tag && L1.getSetList().get(sBits).
						getLineList().get(i).isValid()==true) {
			lineNumberL1=i;
		 L1.setHit(L1.getHit()+1);
		 L1hit=true;
		 break;
		}
	}
	for(int i=0;i<L2.getE();i++) {
		if(L2.getSetList().get(sBits).
				getLineList().get(i).getTag()==tag && L2.getSetList().get(sBits).
				getLineList().get(i).isValid()==true) {
			lineNumberL2=i;
		 L2.setHit(L2.getHit()+1);
		 L2hit=true;
		 break;
		}
	}	
	if(L1hit==false && L2hit ==false) {
		
		if(operation!='M') {
			System.out.println("" + L1.getName() + " miss" + " L2 miss");
		System.out.println("Store in RAM");}
		L1.setMiss(L1.getMiss()+1);	
		L2.setMiss(L2.getMiss()+1);
		modifyRam(adress,datas,size);
		
	}
	else if(L1hit==false && L2hit ==true) {
		byte[] newDatas = new byte[L1B];
		System.out.println("" + L1.getName() + " miss" + " L2 hit");
		L1.setMiss(L1.getMiss()+1);
		if(operation!='M') {
			System.out.println("" + L1.getName() + " miss" + " L2 hit");
		System.out.println("Store and place in L2 set " +sBits + ", " + L1.getName() + ", RAM");}
		lineNumberL1=betweenCaches(L2,L1,tag,sBits,lineNumberL2);
		for(int i=0;i<datas.length;i++) {
			L1.getSetList().get(sBits).getLineList().get(lineNumberL1).getBlockList().get(i).setData(datas[i]);
		}
		L1.getSetList().get(sBits).getLineList().get(lineNumberL1).setDate(new Date());
		
		betweenCaches(L1,L2,tag,sBits,lineNumberL1);
		for(int i=0;i<L2B;i++) {
			newDatas[i]=			L2.getSetList().get(sBits).getLineList().get(lineNumberL2).getBlockList().get(i).getData();
		}
		modifyRam(adress,newDatas,L2B);
	}
	else if(L1hit==true && L2hit==false) {
		byte[] newDatas = new byte[L1B];
		if(operation!='M') {
			System.out.println("" + L1.getName() + " hit" + " L2 miss");
		System.out.println("Store and place in L2 set " +sBits + ", " + L1.getName()+ ", RAM" );}
		L2.setMiss(L2.getMiss()+1);
		for(int i=0;i<datas.length;i++) {
			L1.getSetList().get(sBits).getLineList().get(lineNumberL1).getBlockList().get(i).setData(datas[i]);
		}
		L1.getSetList().get(sBits).getLineList().get(lineNumberL1).setDate(new Date());
		
		lineNumberL2=betweenCaches(L1,L2,tag,sBits,lineNumberL1);
		for(int i=0;i<L2B;i++) {
			newDatas[i]=			L2.getSetList().get(sBits).getLineList().get(lineNumberL2).getBlockList().get(i).getData();
		}
		modifyRam(adress,newDatas,L2B);
		
	}
	else {
		byte[] newDatas = new byte[L1B];
		if(operation!='M') {
		
		System.out.println("" + L1.getName() + " hit" + " L2 hit");
		System.out.println("Store and place in L2 set " +sBits + ", " + L1.getName() + ", RAM" );}
		for(int i=0;i<datas.length;i++) {
			L1.getSetList().get(sBits).getLineList().get(lineNumberL1).getBlockList().get(i).setData(datas[i]);
		}
		L1.getSetList().get(sBits).getLineList().get(lineNumberL1).setDate(new Date());
		lineNumberL2=betweenCaches(L1,L2,tag,sBits,lineNumberL1);		
	
		for(int i=0;i<L2B;i++) {
			newDatas[i]=			L2.getSetList().get(sBits).getLineList().get(lineNumberL2).getBlockList().get(i).getData();
		}
		modifyRam(adress,newDatas,L2B);
	}
	
	
}
public static void modifyRam(int adress,byte datas[],int size) throws Exception {

	RandomAccessFile out=new RandomAccessFile("./RAM.dat","rw");
	
	out.seek(adress);
	int k=0;
for(int i=adress;k<size;i=i++) {
	out.seek(i);
	out.writeByte(datas[k]);
	k++;
}
		
		
	
	out.close();
}
public static int fromRamToL2(cache L2,int adress,int tag,int sBits) throws Exception {
	int lineNumber=-1;
	lineNumber =L2.checkEviction(tag, sBits);
	if(lineNumber==-1) {
			L2.evicIncrmt();
		lineNumber = L2.evictionLineSelect(sBits);
	}
int k=0;
	for(int i=0;i<L2B;i++) {
		L2.getSetList().get(sBits).
		getLineList().get(lineNumber).getBlockList().get(i).setData(readRam(adress,L2B)[k]);
		k++;
	}
	L2.getSetList().get(sBits).
	getLineList().get(lineNumber).setValid(true);
	L2.getSetList().get(sBits).
	getLineList().get(lineNumber).setTag(tag);
	Date date = new Date();
	L2.getSetList().get(sBits).
	getLineList().get(lineNumber).setDate(date);
	return lineNumber;
}	
public static int betweenCaches(cache from,cache to,int tag,int sBits,int fromLineNumber) {
	int toLineNumber;
	
	toLineNumber =to.checkEviction(tag, sBits);
	if(toLineNumber==-1) {
		
		to.evicIncrmt();
		toLineNumber = to.evictionLineSelect(sBits);
	}
	for(int i=0;i<to.getB();i++) {
		to.getSetList().get(sBits).getLineList().get(toLineNumber).getBlockList().get(i).setData(from.getSetList().get(sBits).getLineList().
				get(fromLineNumber).getBlockList().get(i).getData());
				
	}
	to.getSetList().get(sBits).getLineList().get(toLineNumber).setTag(tag);
	to.getSetList().get(sBits).getLineList().get(toLineNumber).setDate(new Date());
	to.getSetList().get(sBits).getLineList().get(toLineNumber).setValid(true);
	from.getSetList().get(sBits).getLineList().get(fromLineNumber).setDate(new Date());
	return toLineNumber;
}
public static byte[] readRam(int adress,int size) throws IOException {
	byte[] datas =new byte[size];
int newAdress=adress;
int k=0;
RandomAccessFile out=new RandomAccessFile("./RAM.dat","r");
int i=0;
out.seek(0);
try {
	
	for(i=newAdress;i<newAdress+size;i=i+1) {
		
		out.seek(i);
		datas[k]= out.readByte();
		
		
		k++;
	}
	out.close(); }
catch(Exception ex){
	ex.printStackTrace();
System.out.println(i);
	
}
	return datas;
}
public static void printSummary(cache L1I,cache L1D,cache L2) throws IOException{
	String outputS=String.format("L1I hits %d L1I misses %d L1I evictions %d\n",L1I.getHit(),L1I.getMiss(),L1I.getEviction())+
			String.format("L1D hits %d L1D misses %d L1D evictions %d\n",L1D.getHit(),L1D.getMiss(),L1D.getEviction())+
			String.format("L2 hits %d L2 misses %d L2 evictions %d\n",L2.getHit(),L2.getMiss(),L2.getEviction());
	System.out.println(outputS);

	File output = new File("./output.txt");
	FileWriter out = new FileWriter(output,false);
	out.write(outputS);
	out.close();
	System.out.println("Summary printed to ./output.txt");
}



}	
	
		
