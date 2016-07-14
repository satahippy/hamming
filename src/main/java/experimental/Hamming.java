package experimental;

public class Hamming {

    private enum Action {
        encode, decode
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: [encode|decode] <bits>");
            System.exit(1);
        }

        Action action = Action.valueOf(args[0]);
        String bits = args[1];

        switch (action) {
            case encode:
                System.out.println(encode(bits));
                break;
            case decode:
                System.out.println(decode(bits));
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
