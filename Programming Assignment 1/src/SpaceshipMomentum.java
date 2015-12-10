import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class will quickly calculate the ship's momentum based on the velocity
 * as well as the relativistic mass.
 * 
 * @input First line of the input is the number T of velocity and mass pairs.
 *        Next 3*T lines of input describe T input pairs. Each test case starts
 *        with a number B, the radix base, in the first line. Then follow the
 *        velocity V and the mass M, each value per line. The numbers T and B
 *        are to be in decimal form, while V and M are in base-B form.
 * @output There will be T lines of outputs, each line writes the momentum value
 *         P for each test case in order given in the input. P will be written
 *         in the correct format and in same radix base B indicated in each
 *         input.
 * @assumption All numbers V and M are given in the correct format (no
 *             unnecessary zeroes and no unnecessary radix point.) All inputs
 *             are given in advance and outputs will only be displayed after all
 *             T sets of inputs are entered. All numbers V and M are valid
 *             numbers in the specified base B (i.e. for a base 2, there will
 *             only be 0 and 1)
 * @constraints V and M are of at least 1 and at most 20005 digits. T is at
 *              least 1 and at most 20. Total length of all numbers in all T
 *              pairs is less than 40010 digits. Memory: 256M. Running time: 2s
 *              per T pair.B is of the range [2,36]
 * 
 *              <pre>
 * abnormal exit codes:
 * 1. Failed to read input.
 * 2. T value does not match lines of input
 * </pre>
 * @author Victor Hazali
 */
public class SpaceshipMomentum {

	/** Constant variables **/
	private static final boolean		DEBUG_MODE		= false;
	private static final InputStream	INPUT_STREAM	= System.in;
	private static final OutputStream	OUTPUT_STREAM	= System.out;
	// ASCII value for '.'
	private static final int			RADIX_PT		= -2;
	// To decide when to stop recursive call for Karatsuba Algorithm
	private static final int			CUT_OFF			= 3;
	// Index in int array storing length of number
	private static final int			LEN_POS			= 0;

	/** Output Strings **/
	private static final String			INPUT_ERROR		= "Failed to read input"
																+ "\n";
	private static final String			T_MISMATCH		= "Number of test cases does not match value T."
																+ "\n";

	/** Member Variables **/
	private static boolean				initialised		= false;
	private PrintWriter					_pw;
	private int							_pairs;
	private List<Integer>				_bases;
	private List<String>				_velocities;
	private List<String>				_masses;
	private List<String>				_results;

	/** Accessors and Modifiers **/

	public int getPairs() {
		return _pairs;
	}

	public void setPairs(int pairs) {
		_pairs = pairs;
	}

	public List<Integer> getBases() {
		return _bases;
	}

	public Integer getBase(int index) {
		return getBases().get(index);
	}

	public void setBases(List<Integer> base) {
		_bases = base;
	}

	public void addBase(int base) {
		getBases().add(base);
	}

	public List<String> getVelocities() {
		return _velocities;
	}

	public String getVelocity(int index) {
		return getVelocities().get(index);
	}

	public void setVelocities(List<String> velocity) {
		_velocities = velocity;
	}

	public void addVelocity(String velocity) {
		getVelocities().add(velocity);
	}

	public List<String> getMasses() {
		return _masses;
	}

	public String getMass(int index) {
		return getMasses().get(index);
	}

	public void setMasses(List<String> mass) {
		_masses = mass;
	}

	public void addMass(String mass) {
		getMasses().add(mass);
	}

	public List<String> getResults() {
		return _results;
	}

	public String getResult(int index) {
		return getResults().get(index);
	}

	public void setResults(List<String> result) {
		_results = result;
	}

	public void addResult(String result) {
		getResults().add(result);
	}

	/**
	 * Runs the program to calculate the spaceship's momentum based on inputs.
	 */
	public void run() {
		initialise();
		try {
			readInput();
			evaluateInputs();
			displayResults();
			_pw.close();
		} catch (IOException e) {
			if (DEBUG_MODE) {
				showToUser(INPUT_ERROR);
				e.printStackTrace();
			}
			System.exit(1);
		} catch (IllegalArgumentException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
			showToUser(T_MISMATCH);
			System.exit(2);
		}
	}

	/**
	 * Initializes all member variables. Number of pairs is set to 0, while the
	 * default constructor is called for the rest of the member variables.
	 */
	public void initialise() {
		if (!initialised) {
			_pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					OUTPUT_STREAM)));
			setPairs(0);
			setBases(new ArrayList<Integer>());
			setVelocities(new ArrayList<String>());
			setMasses(new ArrayList<String>());
			setResults(new ArrayList<String>());
			initialised = true;
		}
	}

	/**
	 * Reads the input used to calculate the momentum
	 * 
	 * @throws IOException
	 *             when the specified input stream cannot be read from.
	 * @throws IllegalArgumentException
	 *             when the number of input does not match the T value stated
	 */
	private void readInput() throws IOException, IllegalArgumentException {
		Scanner sc = new Scanner(INPUT_STREAM);
		setPairs(Integer.parseInt(sc.nextLine()));
		for (int i = 0; i < _pairs; i++) {
			if (!sc.hasNext()) {
				sc.close();
				throw new IllegalArgumentException(
						"insufficient input to evaluate\n");
			}
			addBase(Integer.parseInt(sc.nextLine()));
			addVelocity(sc.nextLine());
			addMass(sc.nextLine());
		}
		sc.close();
	}

	/**
	 * Evaluate each input set to calculate the spaceship's momentum
	 */
	private void evaluateInputs() {
		for (int i = 0; i < getPairs(); i++) {
			addResult(calculateMomentum(getBase(i), getVelocity(i), getMass(i)));
		}
	}

	/**
	 * Calculation of each result based on each set of radix base B, velocity V,
	 * and relativistic mass M.
	 * 
	 * @param base
	 *            radix base B of V and M
	 * @param velocity
	 *            velocity of spaceship V
	 * @param mass
	 *            relativistic mass of spaceship M
	 * @return String representing the spaceship's momentum, given by
	 *         multiplication of V and M
	 */
	private String calculateMomentum(Integer base, String velocity, String mass) {
		char[] V = new char[velocity.length() + 1], M = new char[mass.length() + 1];

		/* Handling exceptional cases */

		// for velocity
		if (velocity.length() == 1) {
			if (Integer.parseInt(velocity, base) == 1) {
				return mass;
			}
			if (Integer.parseInt(velocity, base) == 0) {
				return "0";
			}
		}

		// for mass
		if (mass.length() == 1) {
			if (Integer.parseInt(mass, base) == 1) {
				return velocity;
			}
			if (Integer.parseInt(mass, base) == 0) {
				return "0";
			}
		}

		// Creating reversed character arrays out of the input strings
		V = new StringBuilder(velocity).reverse().toString().toCharArray();
		M = new StringBuilder(mass).reverse().toString().toCharArray();

		// Multiplying to obtain result
		StringBuilder result = new StringBuilder(multiply(V, M, base).trim());

		// reversing to correct order
		result = result.reverse();

		return result.toString();
	}

	/**
	 * Multiplies the two int arrays to each other, and returns the resulting
	 * value in a string. Note that this method takes in arrays that are in
	 * reverse order. i.e. a string 1234 should be stored as [4,3,2,1]. The
	 * result is returned in the same reversed order. Therefore, for 12 * 13,
	 * the input arrays should be [2,1] and [3,1] and the answer, 156 will be
	 * returned as the string "651".
	 * 
	 * @param v
	 *            first array, should be the spaceship's velocity
	 * @param m
	 *            second array, should be the spaceship's mass
	 * @param base
	 *            the base that the two values, V and M are in
	 * @return a string containing the spaceship's momentum, stored in reverse
	 *         order
	 */
	private String multiply(char[] v, char[] m, int base) {

		// Change array into int
		int[] velocity = new int[v.length + 1], mass = new int[m.length + 1];
		int temp, velLen = 1, massLen = 1, vRadixPos = 0, mRadixPos = 0;
		boolean hasRadixPt = false;

		for (int i = 0; i < v.length; i++) {
			temp = charToInt(v[i]);

			if (isRadixPt(temp)) {
				hasRadixPt = true;
				vRadixPos = i;
				continue;
			}
			velocity[velLen] = temp;
			velLen++;
		}
		velocity[LEN_POS] = velLen - 1;

		for (int i = 0; i < m.length; i++) {
			temp = charToInt(m[i]);

			if (isRadixPt(temp)) {
				hasRadixPt = true;
				mRadixPos = i;
				continue;
			}

			mass[massLen] = charToInt(m[i]);
			massLen++;
		}
		mass[LEN_POS] = massLen - 1;

		return arrayToString(karatsubaMult(velocity, mass, base), hasRadixPt,
				vRadixPos, mRadixPos);
	}

	/**
	 * Executes the Karatsuba Multiplication algorithm. Method takes in two
	 * integer arrays, with each array index containing one digit and the length
	 * of the number indicated in the first index. The numbers should also have
	 * been stored in reverse order.
	 * 
	 * @param v
	 *            first number to be multiplied
	 * @param m
	 *            second number to be multiplied
	 * @param base
	 *            radix base of the numbers to be multiplied
	 * @return an integer array containing the product of v and m, stored in
	 *         reverse order and having the length stored in the first index.
	 *         Note that the result will be in the same radix base as the inputs
	 */
	private int[] karatsubaMult(int[] v, int[] m, int base) {
		// base case
		if (v[LEN_POS] < CUT_OFF || m[LEN_POS] < CUT_OFF) {
			return longMult(v, m, base);
		}

		// Splitting into halves:
		int len = Math.max(v[LEN_POS], m[LEN_POS]) / 2;
		int[] vLow, vHigh, mLow, mHigh;

		if (v[LEN_POS] <= len) {

			vLow = new int[v[LEN_POS] + 1];
			copyArray(vLow, v, 1, v[LEN_POS] + 1);
			vHigh = new int[2];
			vHigh[LEN_POS] = 1;

		} else {

			vLow = new int[len + 1];
			vHigh = new int[v[LEN_POS] - len + 1];
			copyArray(vLow, v, 1, len + 1);
			copyArray(vHigh, v, len + 1, v[LEN_POS] + 1);

		}

		if (m[LEN_POS] <= len) {

			mLow = new int[m[LEN_POS] + 1];
			copyArray(mLow, m, 1, m[LEN_POS] + 1);
			mHigh = new int[2];
			mHigh[LEN_POS] = 1;

		} else {

			mLow = new int[len + 1];
			mHigh = new int[m[LEN_POS] - len + 1];
			copyArray(mLow, m, 1, len + 1);
			copyArray(mHigh, m, len + 1, m[LEN_POS] + 1);

		}

		// Recursive calls:
		int[] z0, z1, z2;
		z0 = karatsubaMult(vLow, mLow, base);
		z2 = karatsubaMult(vHigh, mHigh, base);
		z1 = karatsubaMult(longAdd(vLow, vHigh, base),
				longAdd(mLow, mHigh, base), base);

		// Adding and subtracting
		// Formula: Z2 * B^(2*R) + (Z1-Z2-Z0)*B^R + Z0

		// Z2 * B^(2*R)
		int[] z2Shifted = shift(z2, 2 * len);

		// Z2 * B^(2*R) + Z0
		int[] z3 = longAdd(z2Shifted, z0, base);

		// (Z1-Z2-Z0)*B^R
		int[] z4 = shift(longSub(z1, longAdd(z2, z0, base), base), len);

		// Adding all together
		return longAdd(z3, z4, base);
	}

	/**
	 * Method to perform quadratic long multiplication. Method takes in two
	 * integer arrays, with each array index containing one digit and the length
	 * of the number indicated in the first index. The numbers should also have
	 * been stored in reverse order.
	 * 
	 * @param v
	 *            first number to be multiplied
	 * @param m
	 *            second number to be multiplied
	 * @param base
	 *            radix base of the numbers
	 * @return an integer array containing the product of v and m, stored in
	 *         reverse order and having the length stored in the first index.
	 *         Note that the result will be in the same radix base as the inputs
	 */
	private int[] longMult(int[] v, int[] m, int base) {

		int[] result = new int[v[LEN_POS] + m[LEN_POS] + 2];

		/* Handling exceptional cases */

		if (v[LEN_POS] == 1) {
			if (v[1] == 0) {
				result[LEN_POS] = 1;
				return result;
			}
			if (v[1] == 1) {
				return m;
			}
		}

		if (m[LEN_POS] == 1) {
			if (m[1] == 0) {
				result[LEN_POS] = 1;
				return result;
			}
			if (m[1] == 1) {
				return v;
			}
		}

		int vIndex = 1, mIndex = 1, resLen = 0;

		// variables to perform the multiplication
		int carry = 0, product = 0;

		// To loop through all digits of the velocity
		for (vIndex = 1; vIndex <= v[LEN_POS]; vIndex++) {

			// To multiply with all digits of the mass
			for (mIndex = 1; mIndex <= m[LEN_POS]; mIndex++) {

				// (vIndex+mIndex)-1 will give the location for the result to be
				// stored in
				product = (v[vIndex] * m[mIndex]) + carry
						+ result[(vIndex + mIndex) - 1];
				carry = 0;

				// Checking if there's carry
				if (product >= base) {
					carry = product / base;
					product = product % base;
				}

				// Storing into result array
				result[(vIndex + mIndex) - 1] = product;
			}
			// Storing last carry for this index of v
			if (carry != 0) {
				result[(vIndex + mIndex) - 1] += carry;
				carry = 0;
				resLen = vIndex + mIndex - 1;	// -1 since index starts from 1
			} else {
				resLen = vIndex + mIndex - 2;
				// -2 since index and out of range for m
			}
		}
		// Storing last carry of the whole number and length of number
		if (carry != 0) {
			result[v[LEN_POS] + m[LEN_POS]] += carry;
			resLen = v[LEN_POS] + m[LEN_POS];
		}
		result[LEN_POS] = resLen;

		return result;
	}

	/**
	 * Method performs long addition on two numbers. Method takes in two
	 * integer arrays, with each array index containing one digit and the length
	 * of the number indicated in the first index. The numbers should also have
	 * been stored in reverse order.
	 * 
	 * @param first
	 *            first number to be added
	 * @param second
	 *            second number to be added
	 * @param base
	 *            radix base of input
	 * @return an integer array containing the sum of first and second, stored
	 *         in
	 *         reverse order and having the length stored in the first index.
	 *         Note that the result will be in the same radix base as the inputs
	 */
	private int[] longAdd(int[] first, int[] second, int base) {
		int len = Math.max(first[LEN_POS], second[LEN_POS]) + 1;
		int shorter = Math.min(first[LEN_POS], second[LEN_POS]);
		int[] result = new int[len + 1];
		int carry = 0, sum = 0, index = 0;

		for (index = 1; index <= shorter; index++) {

			sum = first[index] + second[index] + carry;
			carry = 0;

			if (sum >= base) {
				carry = sum / base;
				sum = sum % base;
			}

			result[index] = sum;
		}

		if (first[LEN_POS] > second[LEN_POS]) {
			for (index = shorter + 1; index <= first[LEN_POS]; index++) {
				sum = first[index] + carry;
				carry = 0;

				if (sum >= base) {
					carry = sum / base;
					sum = sum % base;
				}

				result[index] = sum;
			}
		} else if (second[LEN_POS] > first[LEN_POS]) {
			for (index = shorter + 1; index <= second[LEN_POS]; index++) {
				sum = second[index] + carry;
				carry = 0;

				if (sum >= base) {
					carry = sum / base;
					sum = sum % base;
				}

				result[index] = sum;
			}
		}

		// for most significant carry to be included
		if (carry != 0) {
			result[index] = carry;
			index++;
		}

		result[LEN_POS] = index - 1;

		return result;
	}

	/**
	 * Subtracts the second array from the first array. The int array must
	 * contain numbers stored in reversed order. i.e. "100" is stored as
	 * [0,0,1].
	 * 
	 * @param first
	 *            contains the minuend (the number to be subtracted from)
	 * @param second
	 *            contains the subtrahend (the number to be subtracted)
	 * @param base
	 *            the radix base of the two numbers
	 * @return an int array containing the difference, stored in reverse
	 */
	private int[] longSub(int[] first, int[] second, int base) {
		int len = Math.max(first[LEN_POS], second[LEN_POS]);
		int shorter = Math.min(first[LEN_POS], second[LEN_POS]);
		int[] result = new int[len + 1];
		int carry = 0, difference = 0, index = 0;

		for (index = 1; index <= shorter; index++) {

			difference = first[index] - second[index] - carry;
			carry = 0;

			if (difference < 0) {
				carry = 1;
				difference = base + difference;
			}

			result[index] = difference;
		}

		if (first[LEN_POS] > second[LEN_POS]) {
			for (index = shorter + 1; index <= first[LEN_POS]; index++) {
				difference = first[index] - carry;
				carry = 0;

				if (difference < 0) {
					carry = 1;
					difference = base + difference;
				}
				result[index] = difference;
			}

		} else if (second[LEN_POS] > first[LEN_POS]) {
			for (index = shorter + 1; index <= second[LEN_POS]; index++) {
				difference = second[index] - carry;
				carry = 0;

				if (difference < 0) {
					carry = 1;
					difference = base + difference;
				}
				result[index] = difference;
			}
		}

		// Remove leading zeroes
		index--;
		while (result[index] == 0 && index > 1) {
			index--;
		}
		result[LEN_POS] = index;
		return result;
	}

	/**
	 * Method shifts the content of the array to the right by an offset value
	 * 
	 * @param arr
	 *            the original array
	 * @param offset
	 *            the amount to offset by
	 * @return a new array containing the necessary shifts to the original array
	 */
	private int[] shift(int[] arr, int offset) {
		int[] result = new int[arr[LEN_POS] + 1 + offset];
		for (int i = 1; i <= offset; i++) {
			result[i] = 0;
		}
		for (int i = 1; i <= arr[LEN_POS]; i++) {
			result[i + offset] = arr[i];
		}
		result[LEN_POS] = arr[LEN_POS] + offset;
		return result;
	}

	/**
	 * Method to convert a character array into a String object.
	 * 
	 * @param array
	 *            The array to be converted
	 * @param hasRadixPt
	 *            Boolean variable to indicate if there is a radix point to be
	 *            inserted
	 * @param vRadixPos
	 *            The position of the radix point in the velocity
	 * @param mRadixPos
	 *            The position of the radix point in the mass
	 * @return A String object with the same contents as the array and
	 *         additionally the radix point, in its correct position, if there
	 *         is any.
	 */
	public String arrayToString(int[] array, boolean hasRadixPt, int vRadixPos,
			int mRadixPos) {

		StringBuilder result = new StringBuilder();
		int radixPos = vRadixPos + mRadixPos;

		if (hasRadixPt) {
			for (int i = 1; i <= array[LEN_POS]; i++) {
				if (i == radixPos) {
					result.append(".");
				}
				result.append(intToChar(array[i]));
			}
		} else {
			for (int i = 1; i <= array[LEN_POS]; i++) {
				result.append(intToChar(array[i]));
			}
		}

		int trailing = 0;
		// Removing trailing zeroes
		if (hasRadixPt) {	// only required when there's a radix point
			while (trailing < result.length() && result.charAt(trailing) == '0') {
				trailing++;
			}
		}
		// Removing trailing radix point as it's meaningless
		if (result.charAt(trailing) == '.') {
			trailing++;
		}

		// Removing leading zeroes
		int leading = result.length() - 1;
		while (leading >= 0 && result.charAt(leading) == '0') {
			if (leading >= 1 && result.charAt(leading - 1) == '.') {
				break;
			}
			leading--;
		}

		return result.substring(trailing, leading + 1);
	}

	/**
	 * Checks if the value represents a radix point. This is decided if the
	 * value is equals to the ASCII value of the character '.', which is -2.
	 * 
	 * @param value
	 *            value to check
	 * @return true if value == RADIX_PT, false otherwise.
	 */
	private boolean isRadixPt(int value) {
		return (value == RADIX_PT);
	}

	/**
	 * Displays the results of the calculation to the user. Each result from a
	 * set of V,M and B are separated by a newline.
	 */
	private void displayResults() {
		for (int i = 0; i < getPairs(); i++) {
			showToUser(getResult(i) + "\n");
		}
	}

	/**
	 * Displays a message to the user on the default system output stream. This
	 * method does not add any additional formatting to the string. (i.e. no
	 * newline at the end of message)
	 * 
	 * @param message
	 *            String object containing the message to be displayed.
	 */
	private void showToUser(String message) {
		_pw.write(message);
	}

	/**
	 * Takes in a character and returns its int value based on radix system.
	 * i.e. char A will return 10 , B returns 11 etc.
	 * 
	 * @param c
	 *            char to convert
	 * @return Equivalent integer value
	 */
	private int charToInt(char c) {
		if (c <= '9') {
			return c - '0';
		}
		return c - 'A' + 10;
	}

	/**
	 * Takes in a digit and converts it into a character based on radix system.
	 * i.e. int 10 will return char A, 11 returns B etc.
	 * 
	 * @param digit
	 *            int to convert
	 * @return Equivalent char value
	 */
	private char intToChar(int digit) {
		if (digit <= 9) {
			return (char) (digit + '0');
		}
		return (char) (digit - 10 + 'A');
	}

	/**
	 * Copies the content of the original array to the destination array. Will
	 * copy all content in the range of [from,to)
	 * 
	 * @param destination
	 *            array to be copied into
	 * @param original
	 *            array to copy from
	 * @param from
	 *            index to start copying from
	 * @param to
	 *            index to stop copying from (exclusive)
	 */
	private void copyArray(int[] destination, int[] original, int from, int to) {
		int destLen = 1;
		for (int i = from; i < to; i++) {
			destination[destLen] = original[i];
			destLen++;
		}
		destLen--;
		while (destLen > 1 && destination[destLen] == 0) {
			destLen--;
		}
		destination[LEN_POS] = destLen;
	}

	public static void main(String[] args) {
		SpaceshipMomentum momentumCalc = new SpaceshipMomentum();
		momentumCalc.run();
	}

}
