package game.models;

public enum Difficulty {
	EASY  {
        @Override
        public String toString() {
            return "easy";
        }
    },
	CASUAL  {
        @Override
        public String toString() {
            return "casual";
        }
    },
	HARD  {
        @Override
        public String toString() {
            return "hard";
        }
    }
}
