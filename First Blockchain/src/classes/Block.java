package classes;

import java.util.Date;
import java.util.ArrayList;

import utils.StringUtil;

public class Block {
	
	public String hash; 
	public String previousHash; 
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	//private String data; //Data will be simple message
	private long timeStamp; //Number on milliseconds since 1/1/1970
	private int nonce;
	
	//Constructor
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	//Calculate hash from all parts of the block we don't want to be tampered with	
	public String calculateHash() {
		String calculatedHash = StringUtil.applySha256(
				previousHash +
				Long.toString(timeStamp) +
				Integer.toString(nonce) +
				merkleRoot
				);
		return calculatedHash;
	}
	
	/*
	 * takes the number of 0's a miner must solve for.
	 * Low difficulty = 1 or 2
	 * Testing difficulty = 4-6
	 */
	public void mineBlock(int difficulty) {
		//Create a string with difficulty * "0"
		String target = new String(new char[difficulty]).replace('\0', '0');
		while(!hash.substring(0,difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		System.out.println("Block mined!!! : " + hash);
	}
	
	//Add transaction to this block
	public boolean addTransaction(Transaction transaction) {
		//process transaction and check if valid, unless block is genesis block then ignore
		if(transaction == null) return false;
		if(previousHash != "0") {
			if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}
	
}
