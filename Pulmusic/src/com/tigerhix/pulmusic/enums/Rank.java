package com.tigerhix.pulmusic.enums;

public enum Rank {
	
	PERFECT(1, 1), EXCELLENT(1, .7f), GOOD(.7f, .3f), BAD(.3f, 0), MISS(0, 0);
	
	private float score;
	private float accuracy;
	
	private Rank(float score, float accuracy) {
		this.score = score;
		this.accuracy = accuracy;
	}
	
	public float getScoreScale() {
		return score;
	}
	
	public float getAccuracyScale() {
		return accuracy;
	}

}
