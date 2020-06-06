package fr.blooddonbeta;


public class LockedString
{
    String data ;

    boolean lock ;

    public  LockedString()
    {
        this.data = "";
        lock = true;
    }

    public void setData(String data)
    {
        if(lock)
        {
            this.data = data;
            lock = false;
        }
    }

    public void unlock()
    {
        lock = true;
    }


    public String getData()
    {
        return this.data;
    }
}
