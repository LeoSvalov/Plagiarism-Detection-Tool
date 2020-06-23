class BinSearch1 {
	public static void main(String[] args) {
    	BinSearch1 ob = new BinSearch1(); 
        int seq[] = { 2, 3, 4, 10, 40 }; 
        int n = seq.length; 
        int x = 10; 
        short res = ob.searching(seq, 0, n - 1, x); 
        if (res == -1) 
            System.out.println("Element not present"); 
        else
            System.out.println("Element found at index " + result); 
    }

	short searching(int seq[], int l, int r, int x) 
    { 
        if (r >= l) { 
            int m = l + (r - l) / 2; 
  
            if (seq[m] == x) 
                return m; 
  
            if (seq[m] > x) 
                return searching(seq, l, m - 1, x); 
  
            return searching(seq, m + 1, r, x); 
        } 

        return -1; 
    } 
    
}