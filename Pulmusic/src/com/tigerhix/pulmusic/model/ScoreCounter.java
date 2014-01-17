package com.tigerhix.pulmusic.model;

import com.tigerhix.pulmusic.enums.Rank;

public class ScoreCounter {
	
	private Beatmap beatmap;
	
	private int score;
	private double accuracy;
	private int combo;
	private int maxCombo;
	
	private int notes;
	private int perfect;
	private int excellent;
	private int good;
	private int bad;
	private int miss;
	
	public ScoreCounter(Beatmap beatmap) {
		this.beatmap = beatmap;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public double getAccuracy() {
		return accuracy;
	}
	
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	public int getCombo() {
		return combo;
	}

	public void setCombo(int combo) {
		this.combo = combo;
	}

	public int getMaxCombo() {
		return maxCombo;
	}

	public void setMaxCombo(int maxCombo) {
		this.maxCombo = maxCombo;
	}

	public void add(Rank rank) {
		notes ++;
		switch (rank) {
		case PERFECT:
			perfect ++;
			combo ++;
			if (combo > maxCombo) maxCombo = combo;
			break;
		case EXCELLENT:
			excellent ++;
			combo ++;
			if (combo > maxCombo) maxCombo = combo;
			break;
		case GOOD:
			good ++;
			combo ++;
			if (combo > maxCombo) maxCombo = combo;
			break;
		case BAD:
			bad ++;
			combo = 0;
			break;
		case MISS:
			miss ++;
			combo = 0;
			break;
		default:
			break;
		}
	}
	
	public int getPerfect() {
		return perfect;
	}
	
	public void setPerfect(int perfect) {
		this.perfect = perfect;
	}
	
	public int getExcellent() {
		return excellent;
	}
	
	public void setExcellent(int excellent) {
		this.excellent = excellent;
	}
	
	public int getGood() {
		return good;
	}
	
	public void setGood(int good) {
		this.good = good;
	}
	
	public int getBad() {
		return bad;
	}
	
	public void setBad(int bad) {
		this.bad = bad;
	}
	
	public int getMiss() {
		return miss;
	}

	public void setMiss(int miss) {
		this.miss = miss;
	}

}
