package antispam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Scanner;

public class Antispam {

	public static final int TAILLE_DICTIONNAIRE = 1000;
	public String[] dictionnaire;
	
	
	public Antispam(){
		
		
	}
	
	
	
	
	
	
	public String[] charger_dictionnaire(String dico){
		String res[] = new String[TAILLE_DICTIONNAIRE];
		BufferedReader br = null;
		String ligne;
		try {
			br = new BufferedReader(new FileReader(dico));
			System.out.println("Lecture dictionnaire Ok");
		} catch (FileNotFoundException e) {
			System.out.println("ERREUR OUVERTURE FICHIER DICTIONNAIRE");
			e.printStackTrace();
		}
		
		try {
			int cmpt = 0;
			while((ligne = br.readLine()) != null){
				//System.out.println(ligne);
				if (ligne.length() > 2) {
					res[cmpt] = ligne;
					
				}else{
					res[cmpt] = "motassezlongpourcatchsegfault";
				}
				cmpt++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		as.dictionnaire = as.charger_dictionnaire("dictionnaire1000en.txt");
		//System.out.println(as.dictionnaire[0]);
		System.out.println(" Apprentissage . . .");
		int[] tab = as.lire_message("baseapp/spam/"+0+".txt");
	/*	int cpt=0;
		for (int i = 0; i < tab.length; i++) {
			System.out.println(" Le mot : "+as.dictionnaire[i]+" est visible "+tab[i]);
			
			cpt += tab[i];
		}
		System.out.println("cpt : "+cpt);*/
		int[] SommeDesPresencesSpam = c.getSommeVecteurSpam(as, nbSpamApp);
	//	as.afficheTab(SommeDesPresencesSpam);
		//System.out.println(" ABLE :"+SommeDesPresencesSpam[0]);
		int[] SommeDesPresencesHam = c.getSommeVecteurHam(as, nbSpamApp);
		int nbExemple = nbSpamTest+nbHamTest;
		double PspamApriori = (double)((double)nbSpamTest/(double)nbExemple);
		double PhamApriori = (double)((double)nbHamTest/(double)nbExemple);
		
		int sommeindicatricespam = 0;
		for (int i = 0; i < nbSpamTest; i++) {
			
			double probabspam = c.Probabilite(dossierbasetest+"/spam/"+i+".txt",nbSpamApp,SommeDesPresencesSpam);
			
			double probabham = c.Probabilite(dossierbasetest+"/ham/"+i+".txt",nbHamApp,SommeDesPresencesHam);
			double Pxx = (double)((double)((double)probabspam*(double)PspamApriori)+(double)((double)probabham*(double)PhamApriori));
			Pxx = (double)((double)1/(double)Pxx);
			System.out.println(" 1/P(X = x)"+Pxx);
			double probaPosterioriSpam =(double)((double)((double)probabspam*(double)PspamApriori)*(double)Pxx);
			double probaPosterioriHam =(double)((double)((double)probabham*(double)PhamApriori)*(double)Pxx);
			String prediction;
		//	System.out.println(" Proba a la fin : "+probaPosterioriSpam);
			if(probaPosterioriHam > probaPosterioriSpam){
				prediction = "HAM *** Erreur ***";
				sommeindicatricespam++;
				
			}else{
				prediction = "SPAM";
			}
			
			System.out.println("le SPAM numero "+i+" a ete identifie comme un "+prediction);
			
		}
		double Remp = (double)((double)sommeindicatricespam/(double)nbSpamTest);
		System.out.println(" Risque empirique SPAM :"+Remp);
		System.out.println(" Taux de bonne reconnaissance sur les SPAM :"+((1-Remp)*100)+"%");
		
		
		
		//	as.afficheTab(X);
	}






	
}


