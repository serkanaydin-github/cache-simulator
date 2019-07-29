
public class block {
int blockNumber;
byte data;
block(int blockNumber){
	this.blockNumber=blockNumber;
}
public int getBlockNumber() {
	return this.blockNumber;
}
public void setBlockNumber(int blockNumber) {
	this.blockNumber = blockNumber;
}
public byte getData() {
	return this.data;
}
public void setData(byte data) {
	this.data = data;
}
}
