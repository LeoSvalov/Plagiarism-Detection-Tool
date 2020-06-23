class BubbleSort1{
    public static void main(String[] args) {
        BubbleSort ob = new BubbleSort(); 
        int array[] = {64, 34, 25, 12, 22, 11, 90}; 
        ob.sorting(array); 
    }
	void sorting(int array[]) 
    { 
        int n = array.length; 
        for (int m = 0; m < n-1; m++) 
            for (int t = 0; t < n-m-1; t++) 
                if (array[t] > array[t+1]) 
                { 
                    // swap array[j+1] and array[i] 
                    int swap = array[t]; 
                    array[t] = array[t+1]; 
                    array[t+1] = swap; 
                } 
    } 

}