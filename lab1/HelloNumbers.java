public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        int i = 1;
        while (i <= 10) {
            System.out.print(x + " ");
            x = x + i;
            i += 1;
        }
        System.out.println();
    }
}