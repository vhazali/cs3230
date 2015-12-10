import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
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
 * 
 * @output There will be T lines of outputs, each line writes the momentum value
 *         P for each test case in order given in the input. P will be written
 *         in the correct format and in same radix base B indicated in each
 *         input.
 * 
 * @assumption All numbers V and M are given in the correct format (no
 *             unnecessary zeroes and no unnecessary radix point.) All inputs
 *             are given in advance and outputs will only be displayed after all
 *             T sets of inputs are entered. All numbers V and M are valid
 *             numbers in the specified base B (i.e. for a base 2, there will
 *             only be 0 and 1)
 * 
 * @constraints V and M are of at least 1 and at most 5005 digits. T is at least
 *              1 and at most 20. Total length of all numbers in all T pairs is
 *              less than 10010 digits. Memory: 256M. Running time: 1s per T
 *              pair.B is of the range [2,36]
 * 
 *              <pre>
 * abnormal exit codes:
 * 1. Failed to read input.
 * 2. T value does not match lines of input
 * </pre>
 * 
 * @author Victor Hazali
 * 
 */
public class PA1ABC {

	private static final int			RADIX_PT		= -2;			// ASCII
																		// value
																		// for
																		// '.'
	/** Constant variables **/
	private static final boolean		DEBUG_MODE		= true;
	private static final InputStream	INPUT_STREAM	= System.in;

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
					System.out)));
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

		/* capturing exceptional cases */

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

		// Multiplying results
		V = new StringBuilder(velocity).reverse().toString().toCharArray();
		M = new StringBuilder(mass).reverse().toString().toCharArray();
		StringBuilder result = new StringBuilder(multiply(V, M, base).trim());

		// reversing to correct order
		result = result.reverse();

		return result.toString();
	}

	/**
	 * Multiplies the two char arrays to each other, and returns the resulting
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

		int[] result = new int[v.length + m.length + 1];

		// Determines if there will be a radix point in the result
		boolean hasRadixPt = false;

		// variables to perform the multiplication
		int carry = 0, vInt = 0, mInt = 0, product = 0;

		// indices for radix point
		int mRadixPos = 0, vRadixPos = 0;
		// indices to put into the result array
		int rStart = 0, rOffset = 0;

		// To loop through all digits of the velocity
		for (int vIndex = 0; vIndex < v.length; vIndex++) {

			vInt = charToInt(v[vIndex]);

			if (isRadixPt(vInt)) {
				hasRadixPt = true;
				vRadixPos = vIndex;
				continue;	// if is a radix point, we don't multiply
			}

			rOffset = 0;

			for (int mIndex = 0; mIndex < m.length; mIndex++) {

				mInt = charToInt(m[mIndex]);

				if (isRadixPt(mInt)) {
					hasRadixPt = true;
					mRadixPos = mIndex;
					continue;// if is a radix point, we don't multiply
				}

				product = (vInt * mInt) + carry;
				product += result[rStart + rOffset];
				carry = 0;

				// Checking if there's carry
				if (product >= base) {
					carry = product / base;
					product = product % base;
				}

				// Storing into result array
				result[rStart + rOffset] = product;

				rOffset++;
			}
			// Storing last carry
			if (carry != 0) {
				result[rStart + rOffset] += carry;
				carry = 0;
			}
			rStart++;
		}
		// Storing last carry
		if (carry != 0) {
			result[v.length + m.length - 1] += carry;
		}

		return arrayToString(result, hasRadixPt, vRadixPos, mRadixPos);
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
			int index = 0;

			while (index < array.length) {
				if (index == radixPos) {
					result.append(".");
				}
				result.append(intToChar(array[index]));
				index++;
			}
		} else {
			for (int i = 0; i < array.length; i++) {
				result.append(intToChar(array[i]));
			}
		}

		int trailing = 0;
		// Removing trailing zeroes
		if (hasRadixPt) {
			while (trailing < result.length() && result.charAt(trailing) == '0') {
				trailing++;
			}
		}
		// Removing trailing radix point
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

	public static void main(String[] args) {
		PA1ABC momentumCalc = new PA1ABC();
		momentumCalc.run();
	}

}
