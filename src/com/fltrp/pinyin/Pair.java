package com.fltrp.pinyin;

public class Pair {

	private String word;
	private String pinyin;
	private boolean isMulit;
	public boolean isMulit() {
		return isMulit;
	}
	public void setMulit(boolean isMulit) {
		this.isMulit = isMulit;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
}