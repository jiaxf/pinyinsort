package com.fltrp.pinyin.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fltrp.pinyin.Marker;
import com.fltrp.pinyin.PinYinMarker;
import com.fltrp.pinyin.PinYinSort;

public class PinYinSortTest {

	//测试拼音排序
	public static void main(String[] args) throws Exception{
		
		//System.out.println('阿' - '啊');
		//System.out.println("阿".compareToIgnoreCase("啊"));
		//System.out.println(PinYinSort.sortToString("我门 你们 他们 阿 朝阳 阿尔巴尼亚 我们 哀求 哀悼 阿拉伯 阿飞 暴雨 "));
		
		//System.out.println(PinYinSort.sortToString("阿 你们呢"));
		//System.out.println(new String("我".getBytes(),"gb2312"));
		//System.out.println("阿拉伯数字".charAt(0) - "哀伤".charAt(0));
		//System.out.println("ai1shang1".compareToIgnoreCase("wo3men1"));
		//System.out.println("哀伤".substring(0,1));
		//System.out.println("ai1shang1".compareTo("a"));
		//System.out.println("哀伤".compareTo("啊"));
		//System.out.println("哀伤".compareTo("我们"));
		//System.out.println("a1".compareToIgnoreCase("ai"));
		//String py1 = "ni21";
		//String py2 = "ni2men3";
		//int flag = py1.compareToIgnoreCase(py2);
		//System.out.println(flag);

		ArrayList list = new ArrayList();
		list.add("阿");
		list.add("阿尔巴尼亚");
		list.add("阿尔及利亚");
        list.add("我门");
        list.add("啊");
        list.add("哀");
        list.add("哀号");
        list.add("你们");
        list.add("哀伤");
        list.add("哎");
        list.add("艾");
        list.add("碍");
        list.add("发");
        list.add("发作");
        list.add("我们");		
        list.add("他们");
        list.add("一心一意");
        list.add("心不在焉");
        list.add("阿拉伯数字");
        list.add("爱不释手");
        list.add("北");
        list.add("保");
        list.add("剥");
        list.add("卑");
        list.add("卑鄙");
        list.add("北京");
        list.add("北半球");
              
        //List<String> list = readFile(new File("wordlist22.txt"));
        System.out.println("<-----------排序开始-------------->");

		java.util.Date begin = new java.util.Date();
		List wordlist = (ArrayList) PinYinSort.sortPYSimple(list);
		
        java.util.Date end = new java.util.Date();
		long t = end.getTime() - begin.getTime();
        long s = t / 1000;
        long m = s / 60;
        s = s % 60; 
        System.out.println("<-----------排序结束-------------->");
        System.out.println("排序耗时：" + m + "分" + s + "秒");
        
        for(int i = 0;i<list.size();i++){
        	System.out.println(list.get(i));
        	String py = PinYinMarker.pyMarker.markToPy(list.get(i).toString(), Marker.NO_TOKEN);
        	System.out.println(py);
        }
        
        FileWriter fileWriter = new FileWriter("sortwordlist22.txt");
        for(int i = 0;i  <wordlist.size();i++){
        	System.out.println(wordlist.get(i));
        	fileWriter.write(wordlist.get(i) + "\n");
        }
	}
	
	 public static java.util.List<String> readFile(File file) {
		java.util.List<String> wordList = new java.util.ArrayList<String>();
		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String str = "";
			while ((str = br.readLine()) != null) {
				str = str.split("——")[0];
				if (!(str.equals(""))) {
					wordList.add(str);
				}
			}
			br.close();
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return wordList;
	}
}
