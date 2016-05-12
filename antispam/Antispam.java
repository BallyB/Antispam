package antispam;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Antispam {

	public static final int TAILLE_DICTIONNAIRE = 1000;
	public  String[] dictionnaire;
	
	public Antispam(){
		
		
	}
	
	
	
	public static void main(String[] args){
		Antispam as = new Antispam();
		as.dictionnaire = as.charger_dictionnaire("dictionnaire1000en.txt");
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
	
	
	
	
}
