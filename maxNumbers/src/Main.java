import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		int max = 0;
		int numberOfChildren = Integer.parseInt(in.readLine());

		// Option 1
		for (int i = 0; i < numberOfChildren; i++) {
			String[] numbers = in.readLine().split(" ");
			for (int j = 0; j < numbers.length; j++) {
				int currentNumber = Integer.parseInt(numbers[j]);
				if (currentNumber > max)
					max = currentNumber;
			}
		}
		System.out.println(max);
	}

}
