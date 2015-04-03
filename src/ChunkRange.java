
public class ChunkRange {
	public int begin, end;

	public ChunkRange(int b, int e) {
		this.begin = b;
		this.end = e;
	}

	public String toString() {
		return "<" + begin + "," + end + ">";
	}
	
	public boolean equals(ChunkRange c){
		return this.begin == c.begin && this.end == c.end;
	}

}
