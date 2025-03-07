package playfaircipher;

import java.util.*;

public class PlayfairCipher {
    private final char[][] matrix = new char[5][5];
    private final String key;

    public PlayfairCipher(String key) {
        this.key = key.toUpperCase().replaceAll("J", "I");
        generateMatrix();
    }

    private void generateMatrix() {
        Set<Character> used = new LinkedHashSet<>();
        for (char c : key.toCharArray()) if (Character.isLetter(c)) used.add(c);
        for (char c = 'A'; c <= 'Z'; c++) if (c != 'J') used.add(c);

        Iterator<Character> iter = used.iterator();
        for (int i = 0; i < 5; i++) 
            for (int j = 0; j < 5; j++) 
                matrix[i][j] = iter.next();
    }

    private String prepareText(String text, boolean encrypt) {
        text = text.toUpperCase().replaceAll("J", "I").replaceAll("[^A-Z]", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length() && text.charAt(i) != text.charAt(i + 1)) ? text.charAt(i + 1) : 'X';
            sb.append(a).append(b);
        }
        return sb.toString();
    }

    private String processText(String text, boolean encrypt) {
        text = prepareText(text, encrypt);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            int[] pos1 = findPosition(text.charAt(i));
            int[] pos2 = findPosition(text.charAt(i + 1));
            if (pos1[0] == pos2[0]) { 
                result.append(matrix[pos1[0]][(pos1[1] + (encrypt ? 1 : 4)) % 5])
                      .append(matrix[pos2[0]][(pos2[1] + (encrypt ? 1 : 4)) % 5]);
            } else if (pos1[1] == pos2[1]) { 
                result.append(matrix[(pos1[0] + (encrypt ? 1 : 4)) % 5][pos1[1]])
                      .append(matrix[(pos2[0] + (encrypt ? 1 : 4)) % 5][pos2[1]]);
            } else { 
                result.append(matrix[pos1[0]][pos2[1]])
                      .append(matrix[pos2[0]][pos1[1]]);
            }
        }
        return result.toString();
    }

    private int[] findPosition(char c) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (matrix[i][j] == c) return new int[]{i, j};
        return null;
    }

    public String encrypt(String plaintext) {
        return processText(plaintext, true);
    }

    public String decrypt(String ciphertext) {
        return processText(ciphertext, false).replace("X", "");
    }

    public void displayMatrix() {
        for (char[] row : matrix) System.out.println(Arrays.toString(row));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();

        PlayfairCipher cipher = new PlayfairCipher(keyword);
        cipher.displayMatrix();

        System.out.print("Enter text to encrypt: ");
        String plaintext = scanner.nextLine();
        String encrypted = cipher.encrypt(plaintext);
        System.out.println("Encrypted: " + encrypted);

        System.out.print("Enter text to decrypt: ");
        String ciphertext = scanner.nextLine();
        String decrypted = cipher.decrypt(ciphertext);
        System.out.println("Decrypted: " + decrypted);

        scanner.close();
    }
}
