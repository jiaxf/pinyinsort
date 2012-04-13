package com.fltrp.pinyin;

import java.util.List;

public class PairList {
	
	public List<Pair> list;;
	public boolean isCharacter;
	public List<Pair> getList() {
		return list;
	}
	public void setList(List<Pair> list) {
		this.list = list;
	}
	public boolean isCharacter() {
		return isCharacter;
	}
	public void setCharacter(boolean isCharacter) {
		this.isCharacter = isCharacter;
	}
}
