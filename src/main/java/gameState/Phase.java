package gameState;

public enum Phase {
	REINFORCE  {
        @Override
        public String toString() {
            return "Reinforce";
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
