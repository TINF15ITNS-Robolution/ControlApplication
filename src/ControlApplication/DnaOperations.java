package ControlApplication;


public class DnaOperations {

	public static double mutationPercentage = 0.05;

	/**
	 * vermischt die beiden übergebenden DNA-Sequenzen. Die neue zurückgegebende DNA
	 * erhält die DNA von dnaarray1 bis zum Punkt mid und ab dort die DNA von
	 * dnaarray2
	 * 
	 * @param dnaarray1
	 * @param dnaarray2
	 * @param mid
	 * @return
	 */
	public static int[] mixDNASequences(int[] dnaarray1, int[] dnaarray2, int mid) {
		int[] newDNA = new int[dnaarray1.length];
		for (int i = 0; i < newDNA.length; i++) {
			if (i < mid) {
				newDNA[i] = dnaarray1[i];
			} else {
				newDNA[i] = dnaarray2[i];
			}
		}

		return newDNA;
	}

	/**
	 * mutiert die DNA-Sequenz mit zufälligen Werten in Abhängigkeit der
	 * DnaOperations.mutationPercentage
	 * 
	 * @param dna
	 * @return
	 */
	public static int[] mutateDNASequences(int[] dna) {
		for (int i = 0; i < dna.length; i++) {
			if (Math.random() < mutationPercentage) {
				dna[i] = (int) (Math.random() * 2);
			}
		}

		return dna;
	}
}
