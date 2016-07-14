package experimental;

import java.util.Arrays;

public class Hamming {

    private enum Action {
        encode, decode, distance
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: [encode|decode] <bits>");
            System.out.println("Usage: distance <bits1> <bits2> ...");
            System.exit(1);
        }

        Action action = Action.valueOf(args[0]);

        switch (action) {
            case encode:
                System.out.println(encode(args[1]));
                break;
            case decode:
                System.out.println(decode(args[1]));
                break;
            case distance:
                System.out.println(distance(Arrays.copyOfRange(args, 1, args.length)));
                break;
        }
    }

    public static String encode(String input) {
        int inputLength = input.length();
        byte[] inputBits = stringToBitArray(input);
        int signLength = 2;
        while (Math.pow(2, signLength) < signLength + inputLength + 1) {
            signLength++;
        }

        byte[] signedBits = new byte[inputLength + signLength];
        int j = 0;
        for (int i = 0; i < signedBits.length; i++) {
            if (isPowerOfTwo(i + 1)) {
                signedBits[i] = 0;
            } else {
                signedBits[i] = inputBits[j++];
            }
        }

        for (int i = 0, signI = 1; signI <= signedBits.length; signI = (int)Math.pow(2, ++i)) {
            int sign = 0;
            j = signI - 1;
            while (j < signedBits.length) {
                sign = (sign + signedBits[j]) % 2;
                if ((j + 2) % signI == 0) {
                    j += signI;
                }
                j++;
            }
            signedBits[signI - 1] = (byte) (sign % 2);
        }

        return bitArrayToString(signedBits);
    }

    public static String decode(String input) {
        int inputLength = input.length();
        byte[] inputBits = stringToBitArray(input);
        int signLength = (int) Math.ceil(Math.log(inputLength + 1) / Math.log(2));

        byte[] unsignedBits = new byte[inputLength - signLength];
        int j = 0;
        for (int i = 0; i < inputBits.length; i++) {
            if (!isPowerOfTwo(i + 1)) {
                unsignedBits[j++] = inputBits[i];
            }
        }

        return bitArrayToString(unsignedBits);
    }

    public static int distance(String[] inputs) {
        int totalDistance = 0;
        for (int i = 0; i < inputs.length - 1; i++) {
            for (int j = i + 1; j < inputs.length; j++) {
                int localDistance = distance(inputs[i], inputs[j]);
                if (localDistance > totalDistance) {
                    totalDistance = localDistance;
                }
            }
        }
        return totalDistance;
    }

    private static int distance(String string1, String string2) {
        if (string1.length() != string2.length()) {
            throw new RuntimeException("Distance can be calculated only with the same size inputs");
        }

        int distance = 0;
        int length = string1.length();
        for (int i = 0; i < length; i++) {
            if (string1.charAt(i) != string2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    private static boolean isPowerOfTwo(int number) {
        return (number & (number - 1)) == 0;
    }

    private static byte[] stringToBitArray(String string) {
        byte[] bits = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            byte bit = Byte.parseByte(String.valueOf(string.charAt(i)));
            if (bit != 0 && bit != 1) {
                throw new RuntimeException("Wrong bit sequence");
            }
            bits[i] = bit;
        }
        return bits;
    }

    private static String bitArrayToString(byte[] bits) {
        StringBuilder builder = new StringBuilder();
        for (byte bit : bits) {
            builder.append(bit);
        }
        return builder.toString();
    }
}
