package com.fltrp.pinyin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PinYinMarker {
	
	public static java.util.Map<String, Integer> Number_BH = new java.util.Hashtable<String, Integer>();
	public static PyMarker pyMarker = new PyMarker(Marker.FORMAT_PINYIN_NUMERIC, Marker.TYPE_SPELL_IGNORECASE);
	public static String resourcesFile = "resources/words.txt";
	
	private void setNumberBH() {
		Number_BH = this.readFileBH();
	}
	
	public PinYinMarker() {
		this.setNumberBH();
	}
	
	/**
	 * 读取文件 获取汉字对应的笔画数
	 * 
	 * @param file要读取的文件
	 * @return 读取后返回的字符串
	 */
	private java.util.Map<String, Integer> readFileBH() {
		java.util.Map<String, Integer> num = new java.util.Hashtable<String, Integer>();
		try {
			InputStreamReader reader = new InputStreamReader(
					PinYinMarker.class.getResourceAsStream(resourcesFile), "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String str = "";
			String st = "";
			while ((str = br.readLine()) != null) {
				str = st + str;
				str = str.trim();
				if (!(str.equals(""))) {
					String word = str.substring(0, 1);
					String innum = str.substring(2);
					num.put(word, Integer.valueOf(innum));
				}
			}
			br.close();
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 两个字符串笔画数比较 小于返回-1;大于返回1;相同返回0
	 * @param w1
	 * @param w2
	 * @return
	 */
	public int compareBH(String w1, String w2) {
		w1 = w1.replaceAll("\\s*", "");
		w2 = w2.replaceAll("\\s*", "");
		int wlen1 = w1.length();
		int wlen2 = w2.length();
		int n1 = -999;
		int n2 = -999;
		int length  = wlen1 < wlen2 ? wlen1 : wlen2;
		for (int i = 0; i < length; i++) {
			String s1 = w1.substring(i, i + 1).trim();
			String s2 = w2.substring(i, i + 1).trim();
			if(s1.length() !=0){
				if(PinYinMarker.Number_BH.containsKey(s1)){
					n1 = PinYinMarker.Number_BH.get(s1);
				}else{
					n1 = 0;
				}
			}
			
			if(s2.length() !=0){
				if(PinYinMarker.Number_BH.containsKey(s2)){
					n2 = PinYinMarker.Number_BH.get(s2);
				}else{
					n2 = 0;
				}
			}
				
			if (n1 < n2) {
				return -1;
			} else if (n1 > n2) {
				return 1;
			} else {
				continue;
			}
		}
		return 0;
	}
	
	/**
	 * 标注拼音
	 * @param word
	 * @return 返回<word, pinyin>Pair对的列表集 多音字有多个Pair对
	 */
	public static PairList getPinYinList(String word, int pyNum){
		String phrasePY  = "";
		String charPY = "";
		//标注拼音 词组标注一个音 单字如果是多音字标注多个音
		PairList pairList  = new PairList();
		List<Pair> pinyinList = null;
		Pair pair = null;
		if(word.length() > 1){
			pinyinList = new ArrayList<Pair>();
			phrasePY = PinYinMarker.pyMarker.markToString(word, Marker.TYPE_PINYIN_SHOW_FIRST, Marker.TYPE_SHOW_WORD, true, Marker.NO_TOKEN);
			//对词组拼音进行处理
			phrasePY = phrasePY.replace("P/P/f", "").replace("2/2/f", "");
			phrasePY = phrasePY.replaceAll("/f\\b", "/").replaceAll("/t\\b", "/");
			phrasePY = phrasePY.replaceAll("[-\\']", "");
			String py[] = phrasePY.split("/");
			String pinyinStr = "";
			for(int j = 0;j < py.length;j++){
				if(j%2 == 1){
					pinyinStr += py[j];
				}
			}
			String p[] = pinyinStr.split("(?<=[1-5])");
			//构造返回<word , pinyin>Pair对
			for(int k = 0;k < p.length;k++){
				pair = new Pair();
				pair.setPinyin(p[k]);
				pair.setWord(String.valueOf(word.charAt(k)));
				pinyinList.add(pair);
			}
			pairList.setList(pinyinList);
			pairList.setCharacter(false);
		}else if(word.length() ==1){
			charPY = PinYinMarker.pyMarker.markToString(word, Marker.TYPE_PINYIN_SHOW_ALL, Marker.TYPE_SHOW_CHARCTER, true, Marker.NO_TOKEN);
			charPY = charPY.replace("P/P/f", "").replace("2/2/f", "");
			charPY = charPY.replaceAll("/f\\b", "/").replaceAll("/t\\b", "/");
			String py[] = charPY.split("/");
			String pinyinStr = "";
			if(py.length >= 2){
				pinyinStr = py[1];
			}
			pinyinList = new ArrayList<Pair>();
			for(String pyStr : pinyinStr.split(",")){
				if(pyNum == 1){
				pair = new Pair();
				pair.setPinyin(pyStr);
				pair.setWord(word);
					pair.setMulit(true);
				pinyinList.add(pair);
					break;
				}else{
					pair = new Pair();
					pair.setPinyin(pyStr);
					pair.setWord(word);
					pair.setMulit(true);
					pinyinList.add(pair);
				}
			}
			pairList.setList(pinyinList);
			pairList.setCharacter(true);
		}
		return pairList;
	}
	
	
}
