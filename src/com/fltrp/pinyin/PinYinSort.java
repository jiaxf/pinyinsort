package com.fltrp.pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * <p>按拼音排序的方法调用类</P>
 * @author jiaxf
 *
 */
public class PinYinSort {
	
	
	/**
	 * 输入按空格或换行分给的字符串音序排序方法,多音字返回第一个注音
	 * @param 待排序字符串用空格或换行分割
	 * @return
	 */
	public static String sortToString(String str) {
		int pyNum = 1;
		String[] string = str.split("\n");
		java.util.List<PairList> sortlist = new java.util.ArrayList<PairList>();

		for (int i = 0; i < string.length; i++) {
			String word = string[i];
			if (!word.equals("")) {
				String[] words = word.split(" ");
				for (int n = 0; n < words.length; n++) {
					String w = words[n].trim();
					if (w.length() > 0) {
						PairList list = PinYinMarker.getPinYinList(w, pyNum);
						if(w.length() == 1){
							List<Pair> plist = list.getList();
							PairList temp = null;
							for(Pair p: plist){
								temp = new PairList();
								List<Pair> temp1 = new ArrayList<Pair>();
								temp1.add(p);
								temp.setList(temp1);
								temp.setCharacter(true);
								sortlist.add(temp);
							}
						}else{
							sortlist.add(list);
						}
					}
				}
			}
		}
		PairComparator comp = new PairComparator();
		Collections.sort(sortlist, comp);

		String sortStr = "";
		for (int j = 0; j < sortlist.size(); j++) {
			PairList pairList = sortlist.get(j);
			String wordStr = "";
			String pinyinStr = "";
			List<Pair> plist = pairList.getList();
			for(Pair p : plist){
				wordStr +=  p.getWord();
				pinyinStr += p.getPinyin();
			}
			if(pairList.isCharacter){
				sortStr += wordStr + "/" + pinyinStr + "/" + "\n";
			}else{
				sortStr += wordStr + "\n";
			}
		}
		return sortStr;
	}
	
	/**
	 * 将词条列表按音序排序
	 * @param 待排序的字符串列表
	 * @return
	 */
	public static List<String> sortPYSimple(java.util.List<String> list) {
		int pyNum = 1;
		java.util.List<PairList> sortlist = new java.util.ArrayList<PairList>();
		for(int i=0;i < list.size();i++){
			PairList pairList =  PinYinMarker.getPinYinList(list.get(i), pyNum);
			if(list.get(i).length() ==0 || pairList == null || pairList.getList().size() == 0){
				continue;
			}
			if(list.get(i).length() == 1){
				List<Pair> plist = pairList.getList();
				PairList temp = null;
				for(Pair p : plist){
					temp = new PairList();
					List<Pair> temp1 = new ArrayList<Pair>();
					temp1.add(p);
					temp.setList(temp1);
					temp.setCharacter(true);
					sortlist.add(temp);
				}
			}else{
				sortlist.add(pairList);
			}
		}
		PairComparator comp = new PairComparator();
		Collections.sort(sortlist, comp);
		
		ArrayList<String> resultList = new ArrayList<String>();
		//返回结果
		for(int i=0;i<sortlist.size();i++){
			PairList pairList = sortlist.get(i);
			String wordStr = "";
			String pinyinStr = "";
			List<Pair> plist = pairList.getList();
			for(Pair p : plist){
				wordStr +=  p.getWord();
				pinyinStr += p.getPinyin();
			}
			if(pairList.isCharacter){
				resultList.add( wordStr + "/" + pinyinStr + "/");
			}else{
				resultList.add(wordStr);
			}
		}
		return resultList;
	}
}
