package common;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Message implements Serializable, Comparable{

	public enum Sortes{
		INIT,
		MSG,
		EXIT
	}

	private final Sortes sort;
	private final String str;
	private final String sender;
	private final long time;

	public Message(String sender, Sortes sort, String str){
		this.time = System.currentTimeMillis();
		this.sender = sender;
		this.sort = sort;
		this.str = str;
	}

	public Sortes getSort() {
		return sort;
	}

	public String getStr() {
		return str;
	}

	public String getSender() {
		return sender;
	}

	public long getTime() {
		return time;
	}

	@Override
	public  String toString(){
		if (sort == Sortes.INIT){
			return getStr();
		} else if (sort == Sortes.MSG){
			return "[" + new SimpleDateFormat("HH:mm:ss").format(getTime()) + "][" + getSender() + "]: " + getStr();
		} else if (sort == Sortes.EXIT){
			return "EXIT";
		} else{
			return "Wrong Message Format";
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Message message = (Message) o;

		if (time != message.time) return false;
		if (sort != message.sort) return false;
		if (str != null ? !str.equals(message.str) : message.str != null) return false;
		return !(sender != null ? !sender.equals(message.sender) : message.sender != null);
	}

	@Override
	public int hashCode() {
		int result = sort != null ? sort.hashCode() : 0;
		result = 31 * result + (str != null ? str.hashCode() : 0);
		result = 31 * result + (sender != null ? sender.hashCode() : 0);
		result = 31 * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public int compareTo(Object o){
		Message msg = (Message) o;
		return (this.getTime() < msg.getTime() ? -1 : (this.time == msg.getTime() ? 0 : 1));
	}
}
