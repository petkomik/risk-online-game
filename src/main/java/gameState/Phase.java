package gameState;

public enum Phase {
	DEPLOY  {
        @Override
        public String toString() {
            return "Deploy";
        }
    },
	ATTACK  {
        @Override
        public String toString() {
            return "Attack";
        }
    }, 
	FORTIFY  {
        @Override
        public String toString() {
            return "Fortify";
        }
    }
}
