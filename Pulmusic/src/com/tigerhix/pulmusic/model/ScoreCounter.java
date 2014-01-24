package com.tigerhix.pulmusic.model;

import com.tigerhix.pulmusic.enums.Rank;

public class ScoreCounter {

	private int total;
	
	private double score;
	private double accuracy;
	private int combo;
	private int maxCombo;
	
	private int perfect;
	private int excellent;
	private int good;
	private int bad;
	private int miss;
	
	public ScoreCounter(Pattern pattern) {
		total = pattern.getNotes().size();
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
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
		score += (900000f / total * 1f) + (100000f / (total * (total - 1) / 2f) * (combo - 1));
		if (score >= 999995) {
			score = 1000000;
		}
		accuracy += 100f / total * rank.getAccuracyScale();
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
