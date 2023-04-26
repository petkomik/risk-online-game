package gameState;

public enum Period {
	COUNTRYPOSESSION {
        @Override
        public String toString() {
            return "Country Possesion";
        }
    },
    INITIALREINFORCEMENT {
        @Override
        public String toString() {
            return "Initial Reinforcement";
        }
    },
    MAINPERIOD {
        @Override
        public String toString() {
            return "Main Period";
        }
    }
}
