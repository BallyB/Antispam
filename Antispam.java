package antispam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
				res[cmpt] = ligne;
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
			System.out.println("Lecture mail Ok");
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
			System.out.println(tab[i]);
		}
		
	}
	public static int toASCII(char lettre) 
	{ 
		return (int)lettre; 
	}
	
	public static void main(String[] args){
		Antispam as = new Antispam();
		as.dictionnaire = as.charger_dictionnaire("dictionnaire1000en.txt");
		//System.out.println(as.dictionnaire[0]);
		int[] X = as.lire_message("baseapp/ham/test.txt");
		//as.afficheTab(X);
	}
}
