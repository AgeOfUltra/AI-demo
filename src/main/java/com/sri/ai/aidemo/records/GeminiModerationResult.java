package com.sri.ai.aidemo.records;

public record GeminiModerationResult(
        boolean flagged,
        Categories categories,
        CategoryScores categoryScores) {

    public record Categories(
            boolean sexual, boolean hate, boolean harassment, boolean selfHarm,
            boolean sexualMinors, boolean hateThreatening, boolean violenceGraphic,
            boolean selfHarmIntent, boolean selfHarmInstructions,
            boolean harassmentThreatening, boolean violence) {

        public boolean any() {
            return sexual || hate || harassment || selfHarm || sexualMinors
                    || hateThreatening || violenceGraphic || selfHarmIntent
                    || selfHarmInstructions || harassmentThreatening || violence;
        }
    }

    public record CategoryScores(
            double sexual, double hate, double harassment, double selfHarm,
            double sexualMinors, double hateThreatening, double violenceGraphic,
            double selfHarmIntent, double selfHarmInstructions,
            double harassmentThreatening, double violence) {}
}
