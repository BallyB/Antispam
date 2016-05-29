package antispam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Dictionary;
import java.util.Scanner;

public class Antispam {

	public static int TAILLE_DICTIONNAIRE;
	public String[] dictionnaire;
	
	
	public Antispam(){
		
		
	}
	
	
	
	
	
	
	public String[] charger_dictionnaire(String dico) throws IOException{
		
		BufferedReader br = null;
		FileReader fn;
		FileReader fn2;
		String res[] = null;
		int nbLines = 0;
		
			
		fn = new FileReader(dico);
		fn2 = new FileReader(dico);
		String ligne2;
		BufferedReader reader = new BufferedReader(fn2);
		while ((ligne2 = reader.readLine()) != null)
			if (ligne2.length() > 2) {
			nbLines++;
		}
		reader.close();
		fn2.close();
			
		TAILLE_DICTIONNAIRE = nbLines;
		res = new String[TAILLE_DICTIONNAIRE];
		br = new BufferedReader(fn);
		System.out.println("Lecture dictionnaire Ok");
			
		String ligne;
			int cmpt = 0;
			while((ligne = br.readLine()) != null){
				if (ligne.length() > 2) {
					res[cmpt] = ligne;
					cmpt++;
				}
					
			}
		br.close();
			
		System.out.println("Remplissage tableau dictionnaire Ok");
			
		
		
		return res;

		
		
		
	}
	
	public int[] lire_message(String mail){
		
		int[] X = new int[TAILLE_DICTIONNAIRE];
		BufferedReader br = null;
		String ligne;
		try {
			br = new BufferedReader(new FileReader(mail));
			//System.out.println("Lecture mail Ok");
		} catch (FileNotFoundException e) {
			System.out.println("ERREUR OUVERTURE MAIL");
			e.printStackTrace();
		}
		
		try {
			int cmpt = 0;
			while ((ligne = br.readLine()) != null) {
				ligne = nettoyer_ligne(ligne);

				// System.out.println(ligne);
				String[] mots = ligne.split(" ");
				
				for (int i = 0; i < mots.length; i++) {
				//	System.out.println("mon mot:" + mots[i] + ":"+i);
					
					int tmp = isInDictionnary(mots[i]);
					if (tmp != -1) {
					//	System.out.println("Mot dans le dico à l'indice "+tmp);
						X[tmp] = 1;
					}

				}

				cmpt++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return X;
		
		
	}
	
	private String nettoyer_ligne(String lignemail) {
		
		for (int j = 0; j < lignemail.length(); j++) {
			
			if(toASCII(lignemail.charAt(j)) > 32 && toASCII(lignemail.charAt(j)) < 65){
				StringBuffer buffer = new StringBuffer(lignemail);
				buffer.setCharAt(j, ' ');
			 
				lignemail = buffer.toString();
				
				
			}
			if(toASCII(lignemail.charAt(j)) > 90 && toASCII(lignemail.charAt(j)) < 97){
				StringBuffer buffer = new StringBuffer(lignemail);
				buffer.setCharAt(j, ' ');
			 
				lignemail = buffer.toString();
				
				
			}
			if(toASCII(lignemail.charAt(j)) > 122){
				StringBuffer buffer = new StringBuffer(lignemail);
				buffer.setCharAt(j, ' ');
			 
				lignemail = buffer.toString();
				
				
			}
		}
		return lignemail;
		
	}



	public int isInDictionnary(String mot){
		
		for (int i = 0; i < TAILLE_DICTIONNAIRE; i++) {
			
			if(dictionnaire[i].equalsIgnoreCase(mot)){
				//System.out.println("je compare :"+mot+":  avec :"+dictionnaire[i]+": à la pos "+i);
				return i;
			}
		}
		
		
		
		return -1;
		
	}
	
	
	public void afficheTab(int[] tab){
		for (int i = 0; i < tab.length; i++) {
			System.out.println("indice : "+i+" valeur : "+tab[i]);
		}
		
	}
	public static int toASCII(char lettre) 
	{ 
		return (int)lettre; 
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static void main(String[] args){
		int nbSpamTest = Integer.parseInt(args[1]);
		int nbHamTest = Integer.parseInt(args[2]);
		String dossierbasetest = args[0];
		Scanner sc = new Scanner(System.in);
	
		System.out.println("Combien de Spam dans la base d'apprentissage ? ");
		String str = sc.nextLine();
		System.out.println("Combien de Ham dans la base d'apprentissage ? ");
		String str2 = sc.nextLine();
		int nbSpamApp = Integer.parseInt(str);
		int nbHamApp = Integer.parseInt(str2);
		Antispam as = new Antispam();
		Classifieur c = new Classifieur(as);
		
			try {
				as.dictionnaire = as.charger_dictionnaire("dictionnaire1000en.txt");
				System.out.println(" Apprentissage . . .");
				int Epsilon = 1;
				double[] bjSpam = c.getBJSpam(as, nbSpamApp, Epsilon);
			//	System.out.println("TATA");
				/*for (int i = 0; i < bjSpam.length; i++) {
					System.out.println(" Spam :"+bjSpam[i]);
				}*/
				double[] bjHam = c.getBJHam(as, nbHamApp, Epsilon);
			/*	for (int i = 0; i < bjHam.length; i++) {
					System.out.println(" Ham :"+bjHam[i]);
				}*/
				int nbExemple = nbSpamTest+nbHamTest;
				double PspamApriori = (double)((double)nbSpamTest/(double)nbExemple);
				double PhamApriori = (double)((double)nbHamTest/(double)nbExemple);
				int sommeindicatricespam = 0;
				String prediction;
				String prediction2;
				System.out.println("Test :");
				for (int i = 0; i < nbSpamTest; i++) {
					double probabspam = c.Probabilite(dossierbasetest+"/spam/"+i+".txt",bjSpam);
					double probabham = c.Probabilite(dossierbasetest+"/spam/"+i+".txt",bjHam);
					
					double Pxx = (double)((double)((double)Math.exp(probabspam)*(double)Math.exp(PspamApriori))+(double)((double)Math.exp(probabham)*(double)Math.exp(PhamApriori)));
					Pxx = (double)((double)1/(double)Pxx);
					Pxx = (double)Math.log(Pxx);
					double probaPosterioriSpam =(double)((double)((double)probabspam+Math.log((double)PspamApriori))+(double)Pxx);
					double probaPosterioriHam =(double)((double)((double)probabham+Math.log((double)PhamApriori))+(double)Pxx);
					
					if(probaPosterioriHam> probaPosterioriSpam){
						sommeindicatricespam++;
						prediction = "HAM *** erreur ***";
					}else{
						prediction = "SPAM";
					}
					System.out.println("le SPAM numero "+i+" : P(Y=Spam| X=x) = "+Math.exp(probaPosterioriSpam)+", P(Y=Ham|X=x) = "+Math.exp(probaPosterioriHam)+"");
					System.out.println("    == > identifie comme un "+prediction);
					
				}
				int sommeindicatriceham = 0;
				for (int i = 0; i < nbHamTest; i++) {
					double probabspam = c.Probabilite(dossierbasetest+"/ham/"+i+".txt",bjSpam);
					double probabham = c.Probabilite(dossierbasetest+"/ham/"+i+".txt",bjHam);
					
					double Pxx = (double)((double)((double)Math.exp(probabspam)*(double)Math.exp(PspamApriori))+(double)((double)Math.exp(probabham)*(double)Math.exp(PhamApriori)));
					Pxx = (double)((double)1/(double)Pxx);
					Pxx = (double)Math.log(Pxx);
					double probaPosterioriSpam =(double)((double)((double)probabspam+Math.log((double)PspamApriori))+(double)Pxx);
					double probaPosterioriHam =(double)((double)((double)probabham+Math.log((double)PhamApriori))+(double)Pxx);
					if(probaPosterioriSpam> probaPosterioriHam){
						sommeindicatriceham++;
						prediction2 = "SPAM *** erreur ***";
					}else{
						prediction2 = "HAM";
					}
					System.out.println("le HAM numero "+i+" : P(Y=Spam| X=x) = "+Math.exp(probaPosterioriSpam)+", P(Y=Ham|X=x) = "+Math.exp(probaPosterioriHam)+"");
					System.out.println("    == > identifie comme un "+prediction2);
				}
				
				
				double Remp = (double)((double)sommeindicatricespam*100/(double)nbSpamTest);
				double Remp2 = (double)((double)sommeindicatriceham*100/(double)nbHamTest);
				System.out.println("Taux d'erreur sur les "+nbSpamTest+" SPAM :"+round(Remp,1)+"%");
				System.out.println("Taux d'erreur sur les "+nbHamTest+" HAM :"+round(Remp2,1)+"%");
				double Remptotal = (double)((double)((double)sommeindicatriceham+(double)sommeindicatricespam)*100/(double)nbExemple);
				System.out.println("Taux d'erreur global sur "+nbExemple+" mails :"+round(Remptotal,1)+"%");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
			
	
	}






	
}


