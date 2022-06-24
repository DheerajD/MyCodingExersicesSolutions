//More details and understaning of volatile and synchronised in link: 
//https://medium.com/google-developer-experts/on-properly-using-volatile-and-synchronized-702fc05faac2#id_token=eyJhbGciOiJSUzI1NiIsImtpZCI6IjJiMDllNzQ0ZDU4Yzk5NTVkNGYyNDBiNmE5MmY3YjM3ZmVhZDJmZjgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE2NTYxMDc0MDcsImF1ZCI6IjIxNjI5NjAzNTgzNC1rMWs2cWUwNjBzMnRwMmEyamFtNGxqZGNtczAwc3R0Zy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjExMjc2MjI5OTA1NTkzNTQwNDU5OSIsImVtYWlsIjoiZGhlZXJhai5kb29kaHlhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiIyMTYyOTYwMzU4MzQtazFrNnFlMDYwczJ0cDJhMmphbTRsamRjbXMwMHN0dGcuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJuYW1lIjoiRGhlZXJhaiBEb29kaHlhIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hLS9BT2gxNEdnTXphcC02RHllSmhINmkxbmdYb0N1NmhPRUhPMm45WDA2aldGeGpnPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IkRoZWVyYWoiLCJmYW1pbHlfbmFtZSI6IkRvb2RoeWEiLCJpYXQiOjE2NTYxMDc3MDcsImV4cCI6MTY1NjExMTMwNywianRpIjoiZjc1NWQwMDExNDkyMDg0NzY2ZGVmOTQ5ZTVhYWEzNGY2ODMzOTg4MiJ9.JEtrtdOFJEKtvvl7KnThpZxEe3UOHSiFF3ufo8SqqS_DXrmYpnWbMguuai9IxjJv9SwppMk1KxB2HO7UzxCzAmi730w75cYAwoxwLXfsOfsNpa-oeUvgZdD3VGsqISwPEK5faV6Q3oF3L3QYCIEjW13TnJM220dv_JzVkD5DOHsLI9OnhRob54SgdfpMqrlxnyXuxhhUFyRq1zKVnmwcIg5amOXcY51IlW5HcJfVOJG-Vk7bDsxbdNkME-PX2gMbc_-1CTNPmF3D0o10DWP_kWuW14IdDHfPNiFz4mEDj2iXOj1VDy7LVXYN61f1nvLbNZ3rFLMjt_4wvm07ywwQ_A
//https://jorosjavajams.wordpress.com/volatile-vs-synchronized/
  
import java.util.*;

class BatchSum {
    private int batchSize;
    volatile Queue<Integer> batch;    //We make this queue volatile so that all threads use this copy(present in main memory, rather than creating their own copies in there cache.)
    int count = 0;

    public void addIntegerTobatch(int num) {    //Not needed to synchronize. We have already synchronised incCount(). Since 'batch' is declaraed volatile, multiple threads won't create duplicate copies when "= new BlockingDeque<Integer>()" is called;
        if(count == 0){
            batch = new BlockingDeque<Integer>();
        }
        batch.add(num);
        incCount();
        if(getCount()==batchSize){
            processBatch();
        }  
    }

    public synchronized void incCount() {   //method made synchronised so that count variable is modified by only one thread at a time.
        count++;
    }

    public int getCount() {
        return count;
    }

    
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    private void processBatch() {
        int sum = 0;        
        Iterator itr = batch.iterator();
        while(itr.hasNext()) {
            int val = (int)itr.next();
            sum = sum + val;
        }
        
        callExternalApi(sum); //To make time efficient - make async, apply retry mechenism in case of failure.
        count = 0;
    }

    public void callExternalApi(int sum) {
        //result = result + sum;
    }
}


