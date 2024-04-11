import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Hill {

	// frekvenca črk od a do z
	public static final double[] frequency = {8.34, 1.54, 2.73, 4.14, 12.60, 2.03, 1.92, 6.11, 6.71, 0.23, 0.87, 4.24, 2.53, 6.80, 7.70, 1.66, 0.09, 5.68, 6.11, 9.37, 2.85, 1.06, 2.34, 0.20, 2.04, 0.06};
	
	// v Javi sta podatkovna tipa char in int enaka. če odštejemo 97 od vsakega, dobimo števila od 0 do 25
	// ta funkcija spremeni besedilo v seznam števil
	public static int[] numberize(String str) {
		int[] result = new int[str.length()];
		
		for (int i = 0; i < str.length(); i++) {
			result[i] = str.charAt(i) - 97;
		}
		
		return result;
	}
	
	// funkcija pretvori seznam števil v seznam dvo-dimenzionalnih vektorjev po vrsti
	public static int[][] pair(int[] tab) {
		int[][] result = new int[tab.length / 2][2];
		
		for (int i = 0; i < tab.length; i += 2) {
			result[i/2][0] = tab[i];
			result[i/2][1] = tab[i + 1];
		}
		
		return result;
	}
	
	
	// funkcija prešteje ponovitev vsake črke v kandidatu za besedilo
	public static double[] count(int[] text) {
		double[] counted = new double[26];
		
		int counter;
		for (int i = 0; i < 26; i++) {
			counter = 0;
			for (int j = 0; j < text.length; j++) {
				if (text[j] == i) {
					counter += 1;
				}
			}
			
			counted[i] = (double) counter;
		}
		
		
		return counted;
	}
	
	
	// izračuna hi-kvadrat karakteristiko med kandidatom za besedilo in frekvenco
	public static double chi(double[] counted, int textLength) {
		double chi = 0.0;
		double expected;
		
		for (int i = 0; i < counted.length; i++) {
			expected = frequency[i] * textLength / 100;
			chi += Math.pow(counted[i] - expected, 2) / expected;
		}
		
		return chi;
	}
	
	// če je determinantna matrike enaka 0, matrika ni obrnljiva in ne more biti ključ
	public static boolean invertible(int[][] matrix) {
		
		return (matrix[0][0]*matrix[1][1] % 26) - (matrix[0][1]*matrix[1][0] % 26) != 0;
	}
	
	public static void main(String[] args) {
		
		String cryptogram = "xctystngbaohxpaitrhoehqmoddrqaoewazewflcxipeoyfyroyyoossjiehoncrlpaidnehccrnfeysronotiofwrdaaijnkrwmsrvahaulxsdateeojppadeltrowswnteehtowitidatononadexctyatsdmeusagzauimechsrpdehodccqdqnatfcenwqdeseodpdtoeevofevtkeurcganwljntowmctroroelowvttiatvnlesrmcmpdeutybhbehsrqbxpeeclqdqnpukwdnlekpmrrowsivjfkogdtibgehusymvteecrlpuoqrfpryjifedarukexfdeyumezauiqeoftrehwsvnfeyblboftrehtiatvnlesrmcmpdeytvnheseramebdfokpzrtovtqeddfeuscrysonteehodivmlopdestjfhotomcfpfewmfctimetiewprkdpagivnvtqedddestifkospatmrtiewprkdpakivtwetewhidyulesticfrmyfuxctystmlkgchivgbccymtixcqeysqnelccxmuloxvnjiusjpwlicctroqmsrowodvssrva";
		int textLength = cryptogram.length();
		
		int[][] c = pair(numberize(cryptogram));
		
		File output = new File("Hill_output.txt");

		try {
			output.createNewFile();
			FileWriter Writer = new FileWriter(output);
			
			long startTime = System.nanoTime();
			for (int a11 = 0; a11 < 26; a11++) {
				for (int a12 = 0; a12 < 26; a12++) {
					for (int a21 = 0; a21 < 26; a21++) {
						for (int a22 = 0; a22 < 26; a22++) {
							
							int[][] matrix = {{a11, a12}, {a21, a22}};
							if (!invertible(matrix)) {
								continue; 
							}
							
							int[] temp = new int[textLength];
							
							int index = 0;
							for (int i = 0; i < c.length; i++) {
								temp[index] = (a11*c[i][0] % 26 + a12*c[i][1] % 26) % 26;
								temp[index+1] = (a21*c[i][0] % 26 + a22*c[i][1] % 26) % 26;
								index += 2;
							}
							
							double chi_sq = chi(count(temp), textLength);
							
							// če ima kandidat za besedilo hi-kvadrat karakteristiko manjšo od 150 potem verjetno ni besedilo v angleščini
							if (chi_sq <= 150.0) {
								for (int j = 0; j < temp.length; j++) {
									Writer.write((char) temp[j] + 97);
								}
								Writer.write("\n");
								Writer.write("\n");
							}
							
						}
					}
				}
			}
			
			// v konzolo izpiše čas trajanja generacije matrik in izpisovanje v datoteko Hill_output.txt
			System.out.println(System.nanoTime() - startTime);
			Writer.close();
		} catch (IOException e) {
			
		}
		
		
	}
}

