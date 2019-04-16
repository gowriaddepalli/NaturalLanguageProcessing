
public class ViterbiAlgorithm {

	public static int[] viterbi(double[][] tp, double[][] ep,int[] sn, int[] observations)
	{

		double[][] Viterbimatrix = new double[observations.length][sn.length];
		int[][] finalmaximumpath = new int[sn.length][observations.length];

		for (int statenumber : sn)
		{
			Viterbimatrix[0][statenumber] = tp[0][statenumber] * ep[statenumber][observations[0]];
			finalmaximumpath[statenumber][0] = statenumber;
		}

		for (int obscol = 1; obscol < observations.length; ++obscol)
		{
			int[][] temp = new int[sn.length][observations.length];

			for (int currentstate : sn)
			{
				double maxprobibility = Integer.MIN_VALUE;
				int maximumstate;
				for (int previousstate : sn)
				{
					double probabilitycalculation = Viterbimatrix[obscol - 1][previousstate] * tp[previousstate][currentstate] * ep[currentstate][observations[obscol]];
					if (probabilitycalculation > maxprobibility)
					{
						maxprobibility = probabilitycalculation;
						maximumstate = previousstate;
						Viterbimatrix[obscol][currentstate] = maxprobibility;
						System.arraycopy(finalmaximumpath[maximumstate], 0, temp[currentstate], 0, obscol);
						temp[currentstate][obscol] = currentstate;
					}
				}
			}

			finalmaximumpath = temp;
		}

		double maxprobibility = Integer.MIN_VALUE;
		int maximumstate = 0;
		
		for (int currentstate : sn)
		{
			if (Viterbimatrix[observations.length - 1][currentstate] > maxprobibility)
			{
				maxprobibility = Viterbimatrix[observations.length - 1][currentstate];
				maximumstate = currentstate;
			}
		}
		return finalmaximumpath[maximumstate];
	}

}
