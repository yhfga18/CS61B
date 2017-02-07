public class HorribleSteve {
	public static void main (String [] args) {
		int i = 0;
		for (int j = 0; i < 500; ++i, ++j) {
// <<<<<<< HEAD
			if (!(i == j)) {
	  	  break; // break exits the for loop!
			}
// =======
			// if (!Flik.isSameNumber(i, j)) {
// >>>>>>> afa0b9af581d5c3c084b21666b5a33496618ac47
		}
		System.out.println("i is " + i);
	}
}