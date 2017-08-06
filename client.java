import java.util.*;
import java.io.*;
import java.net.*;
class bidPlayer
{
    Scanner sc=new Scanner(System.in);
    float tempSum;
    bidPlayer(float a)
    {
        tempSum=a;
    }
    private float getBase(String sb)
    {
        StringBuilder sbr = new StringBuilder();
        sbr.append(sb);
        String[] lines = sbr.toString().split("\\n");
        int x = lines.length;
        String temp = lines[x-1];
        String temp1=temp.replaceAll("[^0-9.]", "");
//        System.out.println();
        Float i = Float.parseFloat(temp1);
//        System.out.println(i);
//        System.out.println(); 
        return i;
    }
    public float placeBid(String a)
    {
        float temp=0;
        float base = getBase(a);
        float x ;
        while(true)
        {
            x=sc.nextFloat();
            if(x==-1)
            {
                temp=x;
                break;
            }
            else if(tempSum-x<0)
            {
                temp=-1;
                System.out.println("You will go Bankrupt");
                break;
            }
            else if(x>=base)
            {
                tempSum=tempSum-x;
                temp=x;
                break;
            }
            else
            {
                System.out.println("Your Bid lower than the base price");
            }
        }
        return temp;
    }
    public float returnSum()
    {
        return tempSum;
    }
}

public class client 
{
    
    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        bidPlayer bid;
        bid = new bidPlayer(100);
        float sum;
        String input = "fifaout.txt";
        FileWriter fw;
        fw = new FileWriter(input,true);
        BufferedWriter bw=new BufferedWriter(fw);
        Scanner scr = new Scanner(System.in);
        System.out.println("Enter Your Team Name ");
        String team = scr.nextLine();
        String t = team + ".txt";
        File f = new File(t);
        if(!f.exists())
            f.createNewFile();
        FileWriter fout = new FileWriter(f.getAbsoluteFile(),true);
        BufferedWriter bout = new BufferedWriter(fout);
        System.out.println("----------Player Details----------");
        float j ;
        try
        {
            Socket s=new Socket("localhost",6666);
            DataInputStream dis=new DataInputStream(s.getInputStream());
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(team);
            String str;
            int len;
            while(true)
            {
                if(dis.available()>=0)
                {
                    str =(String) dis.readUTF();
                    System.out.print(str);
                    System.out.println("\n----------Bid On This Player----------");
                    j=bid.placeBid(str);
                    sum=bid.returnSum();
                    if(j==-1)
                    {
                        System.out.println("Player Unsold\n");
                        System.out.println("--------------------------------------");
                    }
                    else
                    {
                        System.out.println("Player Sold for "+j);
                        System.out.println("Remaining in the Purse "+bid.returnSum()+"\n");
                        System.out.println("--------------------------------------\n");
                        StringBuilder sB=new StringBuilder();
                        sB.append(str);
                        String[] lines = sB.toString().split("\\n");
                        len = lines.length;
                        for (int i = 0; i < len; i++) 
                        {
                            bout.append(lines[i]);
                            bout.newLine();
                        }
                        bout.append("Bought : "+j);
                        bout.newLine();
                        bout.newLine();
                    }
                    dout.writeFloat(j);
                }
                else
                {
                    break;
                }
            }
            dis.close();
            dout.close();
            s.close();
        }
        catch (EOFException fg)
        {
            
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        finally
        {
            System.out.println("Auction Over");
            System.out.println("Purse Spent     : "+(100-bid.returnSum()));
            System.out.println("Purse Remaining : "+bid.returnSum());
            System.out.println("--------------------------------------");
            bout.append("Purse Spent     : "+bid.returnSum());
            bout.newLine();
            bout.append("Purse Remaining : "+(100-bid.returnSum()));
            bw.flush();
            bw.close();
            bout.flush();
            bout.close();
        }
    }
}