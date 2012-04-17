package com.fltrp.pinyin;

import java.util.Comparator;


/**
 * <p>拼音排序的比较器,先按拼音字典序排，同音异调的按声调顺序标，同音同调时笔画少的在前</P>
 * @author jiaxf
 *
 */
public class PairComparator implements Comparator<PairList>{

	@Override
	public int compare(PairList list1, PairList list2){
		int resultFlag = -1;
		int length1 = list1.getList().size();
		int length2 = list2.getList().size();
		int length = length1 < length2 ? length1 : length2;
		for(int i = 0;i < length; i++){
			String w1 = list1.getList().get(i).getWord();
			String py1 = list1.getList().get(i).getPinyin();
			String w2 = list2.getList().get(i).getWord();
			String py2 = list2.getList().get(i).getPinyin();
			resultFlag = py1.compareToIgnoreCase(py2);
			if(resultFlag != 0){
				break;
			}else{
				//拼音相同时字符比较
				int flag = w2.compareTo(w1);
				if(flag != 0){
					resultFlag = flag;
					break;
				}else{
					//笔画比较
					int flag2 = PinYinMarker.compareBH(w1, w2);
					if(flag2 != 0){
						resultFlag = flag2;
						break;
					}else{
						if( length1 > length2){
							resultFlag = 1;
						}else{
							resultFlag = -1;
						}
					}
				}
			}
		}
		return resultFlag;
	}
}
