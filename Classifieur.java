package antispam;

public class Classifieur {

	public Antispam as;
	
	public Classifieur(Antispam anti){
		this.as = anti;
		
		
		
	}
	
	
	
/*	public String prediction(int i,double PspamApriori, double PhamApriori, int nbSpamApp, int nbHamApp){
		
		String prediction;
	 //	double probabspam = this.Probabilite(dossierbasetest+"/spam/"+i+".txt",nbSpamApp,SommeDesPresencesSpam);
		
	//	double probabham = this.Probabilite(dossierbasetest+"/spam/"+i+".txt",nbHamApp,SommeDesPresencesHam);
		double Pxx = (double)((double)((double)probabspam*(double)PspamApriori)+(double)((double)probabham*(double)PhamApriori));
		Pxx = (double)((double)1/(double)Pxx);

		
		double probaPosterioriSpam =(double)((double)((double)probabspam+Math.log((double)PspamApriori)));
		double probaPosterioriHam =(double)((double)((double)probabham+Math.log((double)PhamApriori)));
		System.out.println(" Posteriori SPAM "+probaPosterioriHam+" Posteriori HAM "+probaPosterioriHam);
		if(probaPosterioriHam > probaPosterioriSpam){
			prediction = "HAM";
			
			
		}else{
			prediction = "SPAM";
		}
		return prediction;
		
	}*/
	public double[] getBJSpam(Antispam as, int nbExapp, int Epsilon) {
	//	System.out.println("TOTO");
		int[] X = new int[as.TAILLE_DICTIONNAIRE];
		int[] tmp = new int[as.TAILLE_DICTIONNAIRE];
		for (int i = 0; i < nbExapp; i++) {
			tmp = as.lire_message("baseapp/spam/"+i+".txt");
			for (int j = 0; j < tmp.length; j++) {
			//	System.out.println(" J'ai "+X[j]+" et "+tmp[j]);
				X[j] += tmp[j];
			//	System.out.println("\n j'obtiens "+X[j]);
				
			}
			
		}
		double bj;
		double[] Xspam = new double[as.TAILLE_DICTIONNAIRE];
		for (int i = 0; i < Xspam.length; i++) {
			 bj =  (double) ((double) ((double)X[i]+(double)Epsilon)/ (double)((double) nbExapp+(2*Epsilon)));
			 Xspam[i] = bj;
			
		}
		return Xspam;
	}
	public double[] getBJHam(Antispam as, int nbExapp, int Epsilon) {
		int[] X = new int[as.TAILLE_DICTIONNAIRE];
		int[] tmp = new int[as.TAILLE_DICTIONNAIRE];
		for (int i = 0; i < nbExapp; i++) {
			tmp = as.lire_message("baseapp/ham/"+i+".txt");
			for (int j = 0; j < tmp.length; j++) {
				X[j] += tmp[j];
			}
			
		}
		double bj;
		double[] Xham = new double[as.TAILLE_DICTIONNAIRE];
		for (int i = 0; i < Xham.length; i++) {
			 bj =  (double) ((double) ((double)X[i]+(double)Epsilon)/ (double)((double) nbExapp+(2*Epsilon)));
			 Xham[i] = bj;
		}
		return Xham;
	}
	public double Probabilite(String fichier, double[] Bj){
		
		int[] d = as.lire_message(fichier);
		double proba = 0;
		for (int i = 0; i < d.length; i++) {
			double bj = Bj[i];
			double unmoinsbj = (double)((double)1 - (double)bj);
			double Puisbj;
			double Puisunmoinsbj;
			if (d[i] == 0) {
				Puisbj = 1;
				Puisunmoinsbj = unmoinsbj;

			}else{
				Puisbj = bj;
				
				Puisunmoinsbj = 1;
				
			}
		//	System.out.println(" \n Partie 1 : "+Puisbj+"  Partie 2 : "+Puisunmoinsbj);
			proba = (double) ((double)((double) proba +Math.log(((double) (((double) Puisbj * (double) Puisunmoinsbj))))));
			//proba = (double)((double)proba * (double)(((double)Puisbj * (double)Puisunmoinsbj)));
		//	System.out.println("\n proba à l'indice :"+i+" j'ai : "+proba);
		}
		return proba;
	}
		
		
}
