import java.util.*;
import java.io.*;
import java.net.*;
public class server
{
    public static void main(String[] args)
    {
        Scanner scr = new Scanner(System.in);
        String fileName = "fifa.txt";
        float number;
        try
        {
            ServerSocket ss=new ServerSocket(6666);
            Socket s=ss.accept();//establishes connection
            DataInputStream dis=new DataInputStream(s.getInputStream());
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String buff; 
            String teamName = dis.readUTF();
            StringBuilder sb= new StringBuilder();
            String temp;
//            temp = null;
            while((buff = br.readLine())!=null)
            {
                if(!"break".equals(buff))
                {
                    sb.append(buff);
                    sb.append("\n");
                }
                else
                {
//                    sb.append("\nBid On this Player "+"\n");
                    dout.writeUTF(sb.toString());
                    String name = findName(sb);
                    sb.delete(0, sb.length());
                    number=dis.readFloat();
                    if(number!=-1)
                        System.out.println(name+" sold for "+number+" to "+teamName);
                }
            }
            dout.flush();
            dout.close();
            dis.close();
            ss.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    public static String findName(StringBuilder sbr)
    {
        String[] lines = sbr.toString().split("\\n");
        String temp = lines[0];
        String replaceName = temp.replace("Name : ", "");
        return replaceName;
    }
}
