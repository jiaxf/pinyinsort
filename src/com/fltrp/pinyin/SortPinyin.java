package com.fltrp.pinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.fltrp.pinyin.common.Util;

/**
 * 
 * @author luoyi
 * @version 0.1 
 * 
 **********************************************************
 * 2012-3-29 modify by jiaxf 
 *     (1) 增加各方法的注释说明，便于方法的调用
 *      
 ***********************************************************
 * 
 */
public class SortPinyin {

	public static Marker niMarker = null;
	public static Marker nrMarker = null;

	public void init() {
		niMarker = new Marker(Marker.FORMAT_PINYIN_NUMERIC,
				Marker.TYPE_SPELL_IGNORECASE);
		nrMarker = new Marker(Marker.FORMAT_PINYIN_NUMERIC,
				Marker.TYPE_SPELL_REMAIN);
	}

	private java.util.Map<String, Integer> Number_BH = new java.util.Hashtable<String, Integer>();

	public SortPinyin() {
		this.setNumberBH();
		//this.init();
	}

	private void setNumberBH() {
		File file = new File("words.txt");
		Number_BH = this.readFileBH(file);
	}

	/**
	 * 输入按空格或换行分给的字符串音序排序方法,多音字返回多个注音
	 * @param str
	 * @return
	 */
	public String sortToString(String str) {
		String[] string = str.split("\n");
		java.util.List<String> strlist = new java.util.ArrayList<String>();

		for (int i = 0; i < string.length; i++) {
			String word = string[i];
			if (!word.equals("")) {
				String[] words = word.split(" ");
				for (int n = 0; n < words.length; n++) {
					String w = words[n].trim();
					if (!w.equals("")) {
						strlist.add(w);
					}
				}
			}
		}
		strlist = this.sortPY(strlist);

		String sortStr = "";
		for (int j = 0; j < strlist.size(); j++) {
			String s = strlist.get(j);
			sortStr += s + "\n";
		}

		return sortStr;
	}

	/**
	 * 输入按空格或换行分给的字符串音序排序方法,多音字返回第一个注音
	 * @param str
	 * @return
	 */
	public String sortToStringSimple(String str) {
		String[] string = str.split("\n");
		java.util.List<String> strlist = new java.util.ArrayList<String>();

		for (int i = 0; i < string.length; i++) {
			String word = string[i];
			if (!word.equals("")) {
				String[] words = word.split(" ");
				for (int n = 0; n < words.length; n++) {
					String w = words[n].trim();
					if (!w.equals("")) {
						strlist.add(w);
					}
				}
			}
		}
		strlist = this.sortPYSimple(strlist);

		String sortStr = "";
		for (int j = 0; j < strlist.size(); j++) {
			String s = strlist.get(j);
			sortStr += s + "\n";
		}

		return sortStr;
	}

	/**
	 * 将词条列表按音序排序 多音字显示多个拼音
	 * @param 待排序的字符串列表
	 * @return
	 */
	public java.util.List<String> sortPY(java.util.List<String> words) {
		java.util.List<String> wordpinyin = new java.util.ArrayList<String>();

		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			String wordpy = "";

			if (word.length() == 1) {
				wordpy = niMarker.markToString(word,
						Marker.TYPE_PINYIN_SHOW_ALL, Marker.TYPE_SHOW_WORD,
						false, Marker.NO_TOKEN);
			} else {
				wordpy = niMarker.markToString(word,
						Marker.TYPE_PINYIN_SHOW_FIRST, Marker.TYPE_SHOW_WORD,
						false, Marker.NO_TOKEN);
			}

			if (wordpy.contains("/t") && wordpy.contains(",")
					&& !wordpy.contains(" ")) {
				String[] wpys = getWpys(wordpy);
				for (int index = 1; index < wpys.length; index++) {
					wordpinyin.add(wpys[0] + "/" + wpys[index] + "/f");
				}
			} else {
				wordpinyin.add(wordpy);

			}

		}
		// 冒泡法对拼音进行排序
		for (int m = 0; m < wordpinyin.size(); m++) {
			String wordpy = wordpinyin.get(m);

			String w = getPinyin(wordpy);
			if (w == null) {
				continue;
			}

			for (int n = m + 1; n < wordpinyin.size(); n++) {
				String wordpy2 = wordpinyin.get(n);

				String w1 = getPinyin(wordpy);
				String w2 = getPinyin(wordpy2);
				if (w1 == null) {
					continue;
				}
				if (w2 == null) {
					continue;
				}

				int x = w1.compareToIgnoreCase(w2);

				if (x > 0) {
					wordpinyin.set(m, wordpy2);
					wordpinyin.set(n, wordpy);

					wordpy = wordpy2;
				}
			}
		}

		//笔画排序
		for (int j = 0; j < wordpinyin.size(); j++) {
			String wordpy = wordpinyin.get(j);

			String w = getPinyin(wordpy);
			if (w == null) {
				continue;
			}

			for (int k = j + 1; k < wordpinyin.size(); k++) {
				String wordpy2 = wordpinyin.get(k);

				String w1 = getPinyin(wordpy);
				String w2 = getPinyin(wordpy2);

				if (w1 == null) {
					continue;
				}
				if (w2 == null) {
					continue;
				}

				String ws1 = getword(wordpy);
				String ws2 = getword(wordpy2);

				int x = w1.compareToIgnoreCase(w2);

				if (x == 0) {
					int y = sortBH(ws1, ws2);
					if (y > 0) {
						wordpinyin.set(j, wordpy2);
						wordpinyin.set(k, wordpy);

						wordpy = wordpy2;

					}
				}
			}
		}

		wordpinyin = sortPYwords(wordpinyin);

		return wordpinyin;

	}

	
	/**
	 * 将词条列表按音序排序 多音字时只显示第一个拼音
	 * @param 待排序的字符串列表
	 * @return
	 */
	public java.util.List<String> sortPYSimple(java.util.List<String> words) {

		java.util.List<String> wordpinyin = new java.util.ArrayList<String>();

		//对词条进行注音
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			String wordpy = "";
			wordpy = niMarker.markToString(word, Marker.TYPE_PINYIN_SHOW_FIRST,
					Marker.TYPE_SHOW_WORD, false, Marker.NO_TOKEN);

			if (wordpy.contains("/t") && wordpy.contains(",")
					&& !wordpy.contains(" ")) {
				String[] wpys = getWpys(wordpy);
				for (int index = 1; index < wpys.length; index++) {
					wordpinyin.add(wpys[0] + "/" + wpys[index] + "/f");
				}
			} else {
				wordpinyin.add(wordpy);

			}

		}

		//冒泡法进行拼音排序
		for (int m = 0; m < wordpinyin.size(); m++) {
			String wordpy = wordpinyin.get(m);

			String w = getPinyin(wordpy);
			if (w == null) {
				continue;
			}

			for (int n = m + 1; n < wordpinyin.size(); n++) {
				String wordpy2 = wordpinyin.get(n);

				String w1 = getPinyin(wordpy);
				String w2 = getPinyin(wordpy2);
				if (w1 == null) {
					continue;
				}
				if (w2 == null) {
					continue;
				}

				int x = w1.compareToIgnoreCase(w2);

				if (x > 0) {
					wordpinyin.set(m, wordpy2);
					wordpinyin.set(n, wordpy);

					wordpy = wordpy2;
				}
			}
		}

		//按笔画进行排序
		for (int j = 0; j < wordpinyin.size(); j++) {
			String wordpy = wordpinyin.get(j);

			String w = getPinyin(wordpy);
			if (w == null) {
				continue;
			}

			for (int k = j + 1; k < wordpinyin.size(); k++) {
				String wordpy2 = wordpinyin.get(k);

				String w1 = getPinyin(wordpy);
				String w2 = getPinyin(wordpy2);

				if (w1 == null) {
					continue;
				}
				if (w2 == null) {
					continue;
				}

				String ws1 = getword(wordpy);
				String ws2 = getword(wordpy2);

				int x = w1.compareToIgnoreCase(w2);

				if (x == 0) {
					int y = sortBH(ws1, ws2);
					if (y > 0) {
						wordpinyin.set(j, wordpy2);
						wordpinyin.set(k, wordpy);

						wordpy = wordpy2;

					}
				}
			}
		}

		wordpinyin = sortPYwords(wordpinyin);

		return wordpinyin;

	}

	private String[] getWpys(String str) {
		String[] wpys = str.split("/");

		String[] wpys2 = wpys[1].split(",");

		String[] wpys3 = new String[wpys2.length + 1];
		wpys3[0] = wpys[0];

		for (int i = 0; i < wpys2.length; i++) {
			wpys3[i + 1] = wpys2[i];
		}

		return wpys3;
	}

	/**
	 * 获取标注的拼音
	 * @param str
	 * @return
	 */
	private String getPinyin(String str) {
		String pinyin = "";

		String[] words = str.split(" ");
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] py = word.split("/");
			if(py.length == 2){
				if (!py[1].matches("[a-z].*")) {
					continue;
				}
				pinyin += py[1];
			}
		}
		if (!pinyin.matches("[a-z].*")) {
			return null;
		}

		return pinyin;
	}

	/**
	 * 获取需要标音的串的第一个字符拼音
	 * @param str
	 * @return
	 */
	private String getPinyin2(String str) {
		String pinyin = "";

		String[] words = str.split(" ");
		String word = words[0];
		String[] py = word.split("/");

		pinyin = py[1];

		return pinyin;
	}


	/**
	 * 从标音后的串中获取需要标音的汉字
	 * @param str
	 * @return
	 */
	private String getword(String str) {
		String wordstr = "";

		String[] words = str.split(" ");
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] py = word.split("/");

			wordstr += py[0];
		}

		return wordstr;
	}

	/**
	 * 返回带有音调的拼音标注后的串
	 * @param str
	 * @return
	 */
	private String getpinyin(String str) {
		String wordstr = "";

		String[] words = str.split(" ");
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String[] py = word.split("/");

			py[1] = getFormatPinyin(py[1]);

			wordstr += py[1];
		}

		wordstr = "/" + wordstr + "/";

		return wordstr;
	}

	/**
	 * 拼音音标音调显示
	 * @param str
	 * @return
	 */
	private String getFormatPinyin(String str) {
		String[] words = str.split("(?=[1-5])");
		String word = "";
		for (int i = 0; i < words.length; i++) {
			String w = words[i];
			if (i < words.length - 1) {
				String s = words[i + 1].substring(0, 1);
				w = w + s;
				words[i + 1] = words[i + 1].substring(1);
			}

			if (!w.equals("")) {
				w = Util.turnToToneFormat(w);
			}

			word += w;
		}

		return word;
	}

	/**
	 * 两个字符串笔画数比较 小于返回－1;大于返回1
	 * @param w1
	 * @param w2
	 * @return
	 */
	private int sortBH(String w1, String w2) {
		int wlen1 = w1.length();
		int wlen2 = w2.length();
		if (wlen1 < wlen2) {
			for (int i = 0; i < wlen2; i++) {
				if (i >= wlen1) {
					return -1;
				}
				String s1 = w1.substring(i, i + 1);
				String s2 = w2.substring(i, i + 1);
				int n1 = Number_BH.get(s1);
				int n2 = Number_BH.get(s2);

				if (n1 < n2) {
					return -1;
				} else if (n1 > n2) {
					return 1;
				} else {
					continue;
				}

			}

		} else {

			for (int i = 0; i < wlen1; i++) {
				if (i >= wlen2) {
					return -1;
				}
				String s1 = w1.substring(i, i + 1);
				String s2 = w2.substring(i, i + 1);
				int n1 = Number_BH.get(s1);
				int n2 = Number_BH.get(s2);

				if (n1 < n2) {
					return -1;
				} else if (n1 > n2) {
					return 1;
				} else {
					continue;
				}
			}
		}

		return 0;
	}

	/**
	 * 读取文件 获取汉字对应的笔画数
	 * 
	 * @param file要读取的文件
	 * @return 读取后返回的字符串
	 */
	public java.util.Map<String, Integer> readFileBH(File file) {
		java.util.Map<String, Integer> num = new java.util.Hashtable<String, Integer>();
		try {
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file), "UTF-8");
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
	 * 汉字拼音串按汉字词头进行排序
	 * @param wordpinyin
	 * @return
	 */
	private java.util.List<String> sortPYwords(java.util.List<String> wordpinyin) {

		//按词头拼音排序 冒泡排序
		for (int i = 0; i < wordpinyin.size(); i++) {
			String wordpy = wordpinyin.get(i);
			String s1 = wordpy.substring(0, 1);
			String w1 = getPinyin2(wordpy);

			for (int j = i + 1; j < wordpinyin.size(); j++) {
				String wordpy2 = wordpinyin.get(j);

				String s2 = wordpy2.substring(0, 1);
				String w2 = getPinyin2(wordpy2);

				if (w1.equals(w2)) {
					if (s1.equals(s2)) {
						wordpinyin.remove(j);
						wordpinyin.add(i + 1, wordpy2);
						break;
					}
				}
			}
		}

		wordpinyin = trimRepeat(wordpinyin);
		return wordpinyin;
	}

	/**
	 * 从注音后的字符串中获取需要注音的汉字串
	 * @param str
	 * @return
	 */
	public String trimPinyin(String str) {
		String[] words = str.split("\n");

		String string = "";
		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			word = getword(word);

			string += word + "\n";
		}

		return string;
	}

	/**
	 * 按 汉字＋拼音 依次换行 的格式返回 标注拼音后结果
	 * @param str
	 * @return
	 */
	public String formatPinyin(String str) {
		String[] words = str.split("\n");

		String string = "";
		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			String pinyin = getpinyin(word);
			word = getword(word);

			word = word + pinyin;

			string += word + "\n";
		}

		return string;

	}
	
	/**
	 * 去掉重复的字
	 * @param words
	 * @return
	 */
	private java.util.List<String> trimRepeat(java.util.List<String> words) {
		java.util.List<String> word = new java.util.ArrayList<String>();

		for (int i = 0; i < words.size(); i++) {
			String w = words.get(i);
			if (word.contains(w)) {
				continue;
			}
			word.add(w);
		}

		return word;
	}

}
