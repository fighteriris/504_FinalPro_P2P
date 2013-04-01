package com.Crawler;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.sql.*;

public class Crawler {

	/**
	 * @param args
	 */
	//public class WebCrawler {  
	    ArrayList<String> allurlSet = new ArrayList<String>();//���е���ҳurl����Ҫ����Ч��ȥ�ؿ��Կ���HashSet  
	    ArrayList<String> notCrawlurlSet = new ArrayList<String>();//δ��������ҳurl  
	    HashMap<String, Integer> depth = new HashMap<String, Integer>();//������ҳ��url���  
	    int crawDepth  = 2;                                             //�������  
	    int threadCount = 10;                                           //�߳�����  
	    int count = 0; //��ʾ�ж��ٸ��̴߳���wait״̬  
	    public static final Object signal = new Object();   //�̼߳�ͨ�ű���  
	    public Hashtable webpage_hash = new Hashtable(); 
	    
	    public static void main(String[] args) {  
	        final Crawler wc = new Crawler(); 
	        
//	      wc.addUrl("http://www.126.com", 1);  
	        wc.addUrl("http://www.bu.edu", 1);  
	        long start= System.currentTimeMillis();  
	        System.out.println("��ʼ����.........................................");  
	        wc.begin();  
	          
	        while(true){  
	            if(wc.notCrawlurlSet.isEmpty()&& Thread.activeCount() == 1||wc.count==wc.threadCount){  
	                long end = System.currentTimeMillis();  
	                System.out.println("�ܹ�����"+wc.allurlSet.size()+"����ҳ");  
	                System.out.println("�ܹ���ʱ"+(end-start)/1000+"��");  
	                wc.SaveHash();
	                System.exit(1);  
//	              break;  
	            }  
	              
	        }
	    }  
	    private void begin() {  
	        for(int i=0;i<threadCount;i++){  
	            new Thread(new Runnable(){  
	                public void run() {  
//	                  System.out.println("��ǰ����"+Thread.currentThread().getName());  
//	                  while(!notCrawlurlSet.isEmpty()){ ----------------------------------��1��  
//	                      String tmp = getAUrl();  
//	                      crawler(tmp);  
//	                  }  
	                    while (true) {   
//	                      System.out.println("��ǰ����"+Thread.currentThread().getName());  
	                        String tmp = getAUrl();  
	                        if(tmp!=null){  
	                            crawler(tmp);  
	                        }else{  
	                            synchronized(signal) {  //------------------��2��  
	                                try {  
	                                    count++;  
	                                    System.out.println("��ǰ��"+count+"���߳��ڵȴ�");  
	                                    signal.wait();  
	                                } catch (InterruptedException e) {  
	                                    // TODO Auto-generated catch block  
	                                    e.printStackTrace();  
	                                }  
	                            }  
	                              
	                              
	                        }  
	                    }  
	                }  
	            },"thread-"+i).start();  
	        }  
	    }  
	    public synchronized  String getAUrl() {  
	        if(notCrawlurlSet.isEmpty())  
	            return null;  
	        String tmpAUrl;  
//	      synchronized(notCrawlurlSet){  
	            tmpAUrl= notCrawlurlSet.get(0);  
	            notCrawlurlSet.remove(0);  
//	      }  
	        return tmpAUrl;  
	    }  
	//  public synchronized  boolean isEmpty() {  
//	      boolean f = notCrawlurlSet.isEmpty();  
//	      return f;  
	//  }  
	      
	    public synchronized void  addUrl(String url,int d){  
	            notCrawlurlSet.add(url);  
	            allurlSet.add(url);  
	            depth.put(url, d);  
	    }  
	      
	    //����ҳsUrl  
	    public  void crawler(String sUrl){  
	        URL url;  
	        try {  
	                url = new URL(sUrl);  
//	              HttpURLConnection urlconnection = (HttpURLConnection)url.openConnection();   
	                URLConnection urlconnection = url.openConnection();  
	                urlconnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");  
	                InputStream is = url.openStream();  
	                BufferedReader bReader = new BufferedReader(new InputStreamReader(is));  
	                StringBuffer sb = new StringBuffer();//sbΪ��������ҳ����  
	                
	                String rLine = null;  
	                while((rLine=bReader.readLine())!=null){  
	                    sb.append(rLine);  
	                    sb.append("/r/n");  
	                }  
	                  
	                int d = depth.get(sUrl);  
	                System.out.println("����ҳ"+sUrl+"�ɹ������Ϊ"+d+" �����߳�"+Thread.currentThread().getName()+"����");  
	                if(d<crawDepth){  
	                    //������ҳ���ݣ�������ȡ����  
	                    parseContext(sb.toString(),d+1);  
	                }
	                WriteWeb(sUrl,sb);
	                //WritetoSql(sUrl,sb);
	                //WritetoHash(sUrl,sb);
//	              System.out.println(sb.toString());  
	  
	              
	        } catch (IOException e) {  
//	          crawlurlSet.add(sUrl);  
//	          notCrawlurlSet.remove(sUrl);  
	            e.printStackTrace();  
	        }  
	    }  
	    public static String getBASE64(String s){
	    	if(s ==null )
	    		return null;
	    	return (new sun.misc.BASE64Encoder()).encode( s.getBytes() ); 
	    	
	    }
	    public static String getFromBASE64(String s) { 
	    	if (s == null) return null; 
	    	BASE64Decoder decoder = new BASE64Decoder(); 
	    	try { 
	    	byte[] b = decoder.decodeBuffer(s); 
	    	return new String(b); 
	    	} catch (Exception e) { 
	    	return null; 
	    	} 
	    	}
	  //���浽hashtable
	    public void WritetoHash(String sUrl,StringBuffer sb){
	    	String str; 
	    	webpage_hash.put(sUrl, sb);	    
	    	
	    }
	  //���hashtable
	    public  void SaveHash(){
	    	FileOutputStream fos;
			try {
				fos = new FileOutputStream("tmp.xml");
				XMLEncoder e = new XMLEncoder(fos);
	    	e.writeObject(webpage_hash);
	    	e.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	
	    }
	  //���浽���ݿ�
	    public void WritetoSql(String sUrL, StringBuffer sb){
	    	String myurl = sUrL;
	    	if (!sUrL.contains("bu.edu"))
	    	 return;
	    	String data = sb.toString();
	    	String driver = "com.mysql.jdbc.Driver";
	    	String sqlurl = "jdbc:mysql://127.0.0.1:3306/p2psearch_webpage_test";
	    	String user = "root";
	    	String password = "000000";
	    	long time = System.currentTimeMillis();
	    	java.sql.Date date = new java.sql.Date(time);

	    	try {

	    		// ������������
	    		Class.forName(driver);
	    		// �������ݿ�
	    		Connection conn = DriverManager.getConnection(sqlurl, user, password);
	    		if(!conn.isClosed())
	    		System.out.println("Succeeded connecting to the Database!");
	    		// statement����ִ��SQL���
	    		Statement statement = conn.createStatement();
	    		// Ҫִ�е�SQL���
	    		String sql = "insert into webpage(PAGE_URL, PAGE_Content,PAGE_RANK) values(?,?,?)";
	    		PreparedStatement ps=conn.prepareStatement(sql); 
	    		ps.setString(1,myurl);
                ps.setString(2,data);
                ps.setInt(3,3);
                //ps.setDate(4, new Date(time));
                //ps.setInt(3,4);
                //ps.setDate(4,date);
                //statement.executeUpdate(sql);
                int result=ps.executeUpdate();
                if(result > 0)
                	System.out.println("Update");
                else
                	System.out.println("Unable to update the database");
                //ps.setString(4, user.getEmail());
	    		}catch(ClassNotFoundException e) {   
	    			System.out.println("Sorry,can`t find the Driver!");   
	    			e.printStackTrace();   
	    			} catch(SQLException e) {   
	    			e.printStackTrace();   
	    			} catch(Exception e) {   
	    			e.printStackTrace();   
	    			}   	    			   	    		
	    }
	    
	  //����html�ļ�
	    public void WriteWeb(String sUrL, StringBuffer sb){
	    	String url = sUrL;
	    	String tmp = getBASE64(url);
	    	String tmp_out = getFromBASE64(tmp);
	    	System.out.println(tmp);
	    	System.out.println(tmp_out);
	    	String path = "C:\\webpage";
	    	String filepath = path+"\\"+ tmp +".html";
	    	
	    	//File page = new File(path);
	    	try{
	    		 File f = new File(filepath);
	    		 BufferedWriter output = new BufferedWriter(new FileWriter(f)); 
	    		 //sb.toString();
	    		 output.write(sb.toString());
	    		 output.close();
	    		 //output.write(sb);
	    		 //output.writeFromBuffer();
	    	}catch(IOException e){
	    		e.printStackTrace();

	    	}
	    }
	    
	    
	    
	    //��context��ȡurl��ַ  
	    public  void parseContext(String context,int dep) {  
	        String regex = "<a href.*?/a>";  
//	      String regex = "<title>.*?</title>";  
	        String s = "fdfd<title>�� ��</title><a href=\"http://www.iteye.com/blogs/tag/Google\">Google</a>fdfd<>";  
	        // String regex ="http://.*?>";  
	        Pattern pt = Pattern.compile(regex);  
	        Matcher mt = pt.matcher(context);  
	        while (mt.find()) {  
//	          System.out.println(mt.group());  
	            Matcher myurl = Pattern.compile("href=\".*?\"").matcher(  
	                    mt.group());  
	            while(myurl.find()){  
	                String str = myurl.group().replaceAll("href=\"|\"", "");  
//	              System.out.println("��ַ��:"+ str);  
	                if(str.contains("http:")){ //ȡ��һЩ����url�ĵ�ַ  
	                    if(!allurlSet.contains(str)){  
	                        addUrl(str, dep);//����һ���µ�url  
	                        if(count>0){ //����еȴ����̣߳�����  
	                            synchronized(signal) {  //---------------------��2��  
	                                count--;  
	                                signal.notify();  
	                            }  
	                        }  
	                          
	                    }  
	                }  
	            }  
	        }  
	    }  
	}
	
